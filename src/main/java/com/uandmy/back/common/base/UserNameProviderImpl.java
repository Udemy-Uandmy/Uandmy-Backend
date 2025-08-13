package com.uandmy.back.common.base;


import com.uandmy.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNameProviderImpl implements UserNameProvider {

    private final UserRepository userRepository;

    @Override
    public String getUsernameByUserId(String userId) {
        if(userId == null){
            return  null;
        }
        return userRepository.findActiveUserByUserId(userId)
                .map(user -> user.getUsername())
                .orElse(null);
    }
}
