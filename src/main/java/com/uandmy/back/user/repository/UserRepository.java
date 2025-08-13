package com.uandmy.back.user.repository;


import com.uandmy.back.common.base.BaseRepository;
import com.uandmy.back.user.entity.User;
import com.uandmy.back.user.repository.support.UserRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, UserRepositorySupport {

    Optional<User> findByUserId(String userId);
}