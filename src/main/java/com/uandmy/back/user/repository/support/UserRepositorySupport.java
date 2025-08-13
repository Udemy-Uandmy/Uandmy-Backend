package com.uandmy.back.user.repository.support;



import com.uandmy.back.user.dto.UserDetailDto;

import java.util.Optional;

public interface UserRepositorySupport {

    Optional<UserDetailDto> findActiveUserByUserId(String userId);

}
