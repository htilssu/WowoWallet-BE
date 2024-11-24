package com.wowo.wowo.repository;

import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface AtmCardRepository extends JpaRepository<AtmCard, Integer> {

    @NonNull
    Optional<AtmCard> findByOwner(User owner);
    List<AtmCard> findDistinctByOwnerAllIgnoreCase(User owner, Sort sort);
    Optional<AtmCard> findByCardNumber(String cardNumber);
}