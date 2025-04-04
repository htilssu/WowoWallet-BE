package com.wowo.wowo.designPattern.Strategy;

import com.wowo.wowo.designPattern.Strategy.ConcreteStrategies.SearchByEmailStrategy;
import com.wowo.wowo.designPattern.Strategy.ConcreteStrategies.SearchByFullNameStrategy;
import com.wowo.wowo.designPattern.Strategy.ConcreteStrategies.SearchByUsernameStrategy;
import com.wowo.wowo.model.User;
import com.wowo.wowo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService2 {

    private final UserRepository userRepository;
    private final SearchByUsernameStrategy searchByUsernameStrategy;
    private final SearchByEmailStrategy searchByEmailStrategy;
    private final SearchByFullNameStrategy searchByFullNameStrategy;

    private SearchStrategy strategy; // Tương ứng với -strategy trong sơ đồ
    private final Map<String, SearchStrategy> strategies = new HashMap<>();

    // Khởi tạo các strategy trong constructor
    public UserService2(UserRepository userRepository, SearchByUsernameStrategy searchByUsernameStrategy,
                        SearchByEmailStrategy searchByEmailStrategy, SearchByFullNameStrategy searchByFullNameStrategy) {
        this.userRepository = userRepository;
        this.searchByUsernameStrategy = searchByUsernameStrategy;
        this.searchByEmailStrategy = searchByEmailStrategy;
        this.searchByFullNameStrategy = searchByFullNameStrategy;

        // Đăng ký các strategy
        strategies.put("username", searchByUsernameStrategy);
        strategies.put("email", searchByEmailStrategy);
        strategies.put("fullName", searchByFullNameStrategy);

        // Mặc định strategy
        this.strategy = searchByUsernameStrategy;
    }

    // Tương ứng với +setStrategy(strategy) trong sơ đồ
    public void setStrategy(String searchType) {
        this.strategy = strategies.getOrDefault(searchType.toLowerCase(), searchByUsernameStrategy);
    }

    // Tương ứng với +doSomething() trong sơ đồ
    public List<User> getUserByKeyword(String keyword, String searchType) {
        setStrategy(searchType); // Chọn strategy dựa trên searchType
        return strategy.execute(keyword);
    }
}