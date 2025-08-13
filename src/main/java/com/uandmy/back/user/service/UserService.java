package com.uandmy.back.user.service;
import com.uandmy.back.common.base.BaseService;
import com.uandmy.back.common.seurity.JwtTokenProvider;
import com.uandmy.back.user.dto.UserDetailDto;
import com.uandmy.back.user.dto.UserDto;
import com.uandmy.back.user.entity.User;
import com.uandmy.back.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserDto, User, Long> {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;
//    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void setRepository() {
        setRepository(userRepository);
    }

    public UserDetailDto findActiveUserByUserId(String userId) {
        return userRepository.findActiveUserByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않거나 비활성화 상태입니다."));
    }

//    public JwtResponse reissue(String refreshToken){
//
//        jwtTokenProvider.validateToken(refreshToken);
//        String id = Jwts.parser().setSigningKey(secretKey)
//                .parseClaimsJws(refreshToken).getBody().getSubject();
//        User user = userRepository.findByUserId(id)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        String redisRefreshToken = redisTemplate.opsForValue().get(id);
//        if(!refreshToken.equals(redisRefreshToken)) {
//            throw new RuntimeException("리프레쉬 토큰이 일치하지 않습니다");
//        }
//        String newAccessToken = jwtTokenProvider.generateToken(user.getUserId());
//        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
//
//        jwtTokenProvider.storeRefreshToken(user.getUserId(),newRefreshToken);
//        return JwtResponse.builder()
//                .token(newAccessToken)
//                .refreshToken(newRefreshToken)
//                .user(null)
//                .build();
//    }

    /*jpa where 조건*/
//    @Override
//    protected Predicate getPredicate(UserDto searchParam) {
//        BooleanBuilder where = new BooleanBuilder();
//        QUser user = QUser.user;
//
//        //사번
//        if (StringUtils.isNotBlank(searchParam.getUserId())) {
//            where.and(user.userId.containsIgnoreCase(searchParam.getUserId()));
//        }
//
//        //이름
//        if (StringUtils.isNotBlank(searchParam.getUsername())) {
//            where.and(user.username.containsIgnoreCase(searchParam.getUsername()));
//        }
//
//        return where;
//    }

}
