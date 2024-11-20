package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.AtmCardCreateDto;
import com.wowo.wowo.data.dto.AtmCardDto;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.mapper.AtmCardMapper;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;
import com.wowo.wowo.repository.AtmCardRepository;
import com.wowo.wowo.repository.BankRepostitory;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.service.AtmCardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/card", produces = "application/json; charset=UTF-8")
@IsUser
@Tag(name = "Card", description = "Thẻ ngân hàng")
public class CardController {

    private final AtmCardMapper atmCardMapperImpl;
    private final AtmCardRepository atmCardRepository;
    private final UserRepository userRepository;
    private final BankRepostitory bankRepostitory;
    private final AtmCardService atmCardService;

    @PostMapping
    public AtmCardDto createCard(@RequestBody @NotNull @Validated AtmCardCreateDto atmCardDto,
            Authentication authentication) {
        if (atmCardDto == null) {
            throw new BadRequest("Dữ liệu không hợp lệ");
        }

        return atmCardService.createAtmCard(atmCardDto, authentication);
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

        final AtmCard atmCard = atmCardRepository.findById(Integer.valueOf(cardNumber))
                .orElseThrow(() -> new NotFoundException("Thẻ không tồn tại"));

        String userId = (String) authentication.getPrincipal();
        if (!atmCard.getOwner().getId().equals(userId)) return ResponseEntity.ok(
                new ResponseMessage("Không thể xóa thẻ của người khác"));

        atmCardRepository.delete(atmCard);

        return ResponseEntity.ok(new ResponseMessage("Xóa thẻ thành công"));
    }

}
