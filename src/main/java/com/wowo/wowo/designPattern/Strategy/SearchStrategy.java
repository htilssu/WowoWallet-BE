package com.wowo.wowo.designPattern.Strategy;
import com.wowo.wowo.model.User;
import java.util.List;

public interface SearchStrategy {
    List<User> execute(String keyword);
}
