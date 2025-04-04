package com.wowo.wowo.designPattern.Strategy.ConcreteStrategies;


import com.wowo.wowo.designPattern.Strategy.SearchStrategy;
import com.wowo.wowo.model.User;
import com.wowo.wowo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchByEmailStrategy implements SearchStrategy {

    private final UserRepository userRepository;

    @Override
    public List<User> execute(String keyword) {
        return userRepository.findByEmailContainingIgnoreCase(keyword);
    }
}
