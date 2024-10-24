package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.AtmCardDto;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.mapper.AtmCardMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.AtmCard;
import com.wowo.wowo.models.Bank;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.AtmCardRepository;
import com.wowo.wowo.repositories.BankRepostitory;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.util.ObjectUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/card", produces = "application/json; charset=UTF-8")
@Tag(name = "Card", description = "Thẻ ngân hàng")
public class CardController {

    private final AtmCardMapper atmCardMapperImpl;
    private final AtmCardRepository atmCardRepository;
    private final UserRepository userRepository;
    private final BankRepostitory bankRepostitory;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody AtmCardDto atmCardDto,
            Authentication authentication) {
        if (atmCardDto == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Dữ liệu không hợp lệ"));
        }
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(new ResponseMessage("Người dùng không tồn tại"));
        }
        final AtmCard atmCard = atmCardMapperImpl.toEntity(atmCardDto);
        atmCard.setOwner(user);


        try {
            atmCardRepository.save(atmCard);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseMessage("Thẻ đã tồn tại"));
        }

        return ResponseEntity.ok(ObjectUtil.mergeObjects(
                ObjectUtil.wrapObject("card", atmCardMapperImpl.toDto(atmCard)),
                new ResponseMessage("Tạo thẻ thành công")));

    }

    @GetMapping
    public List<AtmCardDto> getCard(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();

        final User userFound = userRepository.findById(userId).orElse(null);

        if (userFound == null) {
            return null;
        }

        final List<AtmCard> atmCardList = atmCardRepository.findDistinctByOwnerAllIgnoreCase(
                userFound,
                Sort.by(Sort.Order.desc("created")));

        return atmCardMapperImpl.usersToUserDTOs(atmCardList);

    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<?> deleteCard(@PathVariable String cardNumber,
            Authentication authentication) {
        if (cardNumber == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Dữ liệu không hợp lệ"));
        }

        final AtmCard atmCard = atmCardRepository.findByCardNumber(cardNumber);

        if (atmCard == null) {
            return ResponseEntity.ok(new ResponseMessage("Thẻ không tồn tại"));
        }
        String userId = (String) authentication.getPrincipal();
        if (!atmCard.getOwner().getId().equals(userId)) return ResponseEntity.ok(
                new ResponseMessage("Không thể xóa thẻ của người khác"));

        atmCardRepository.delete(atmCard);

        return ResponseEntity.ok(new ResponseMessage("Xóa thẻ thành công"));
    }

}
