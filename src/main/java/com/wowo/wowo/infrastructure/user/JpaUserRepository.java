package com.wowo.wowo.infrastructure.user;

import com.wowo.wowo.domain.user.entity.UserAggregate;
import com.wowo.wowo.domain.user.repository.UserRepository;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import com.wowo.wowo.domain.user.valueobjects.UserProfile;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of UserRepository
 * Maps between domain model and persistence model
 */
@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {
    
    private final com.wowo.wowo.repository.UserRepository springUserRepository;
    
    @Override
    public Optional<UserAggregate> findById(UserId userId) {
        return springUserRepository.findById(userId.value())
            .map(this::toDomainModel);
    }
    
    @Override
    public Optional<UserAggregate> findByEmail(Email email) {
        return springUserRepository.findByEmail(email.value())
            .map(this::toDomainModel);
    }
    
    @Override
    public void save(UserAggregate userAggregate) {
        User user = toJpaEntity(userAggregate);
        springUserRepository.save(user);
    }
    
    @Override
    public List<UserAggregate> findAll(int page, int size) {
        return springUserRepository.findAll(PageRequest.of(page, size))
            .getContent()
            .stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(UserId userId) {
        springUserRepository.deleteById(userId.value());
    }
    
    /**
     * Convert JPA entity to domain model
     */
    private UserAggregate toDomainModel(User user) {
        UserProfile profile = new UserProfile(
            user.getFirstName(),
            user.getLastName(),
            "" // Phone number not in current User entity
        );
        
        Money totalMoney = new Money(user.getTotalMoney(), "VND");
        
        return new UserAggregate(
            new UserId(user.getId()),
            new Email(user.getEmail()),
            profile,
            user.getIsActive(),
            user.getIsVerified(),
            totalMoney,
            Instant.now(), // Created at not in current User entity
            Instant.now()  // Updated at not in current User entity
        );
    }
    
    /**
     * Convert domain model to JPA entity
     */
    private User toJpaEntity(UserAggregate userAggregate) {
        User user = User.builder()
            .id(userAggregate.getId().value())
            .email(userAggregate.getEmail().value())
            .firstName(userAggregate.getProfile().firstName())
            .lastName(userAggregate.getProfile().lastName())
            .isActive(userAggregate.isActive())
            .isVerified(userAggregate.isVerified())
            .totalMoney(userAggregate.getTotalMoney().getAmount())
            .build();
        
        return user;
    }
}