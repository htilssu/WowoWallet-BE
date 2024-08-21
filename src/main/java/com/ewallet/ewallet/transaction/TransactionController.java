package com.ewallet.ewallet.transaction;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.dto.response.ReceiverDto;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.partner.PartnerRepository;
import com.ewallet.ewallet.repository.TransactionRepository;
import com.ewallet.ewallet.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transaction", produces = "application/json; charset=UTF-8")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionMapperImpl transactionMapperImpl;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        final TransactionResponse transactionResponse = transactionMapper.toResponse(transaction);

        return ResponseEntity.ok(transactionResponse);

    }

    @GetMapping("/history")
    public List<?> getAllTransaction(@RequestParam Map<String, String> allParams,
            Authentication authentication) {
        int offset = Integer.parseInt(allParams.get("offset"));
        int page = Integer.parseInt(allParams.get("page"));
        String id = ((String) authentication.getPrincipal());


        final List<TransactionResponse> listDto = transactionMapperImpl.toListDto(
                transactionRepository.findBySenderIdOrReceiverId(id, id,
                        Pageable.ofSize(offset).withPage(page)));
        for (TransactionResponse transactionRespons : listDto) {
            if (transactionRespons.getReceiverType().equals("user")) {
                final Optional<User> byId = userRepository.findById(
                        transactionRespons.getReceiverId());

                byId.ifPresent(user -> transactionRespons.setReceiverName(
                        user.getLastName() + " " + user.getFirstName()));
            }
            else {
                final Optional<Partner> byId = partnerRepository.findById(
                        transactionRespons.getReceiverId());
                byId.ifPresent(partner -> transactionRespons.setReceiverName(partner.getName()));
            }
        }


        return listDto;
    }

}
