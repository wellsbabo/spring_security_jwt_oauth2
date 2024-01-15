package com.example.securityTest.repository;


import com.example.securityTest.SocialType;
import com.example.securityTest.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByRefreshToken(String refresToken);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
