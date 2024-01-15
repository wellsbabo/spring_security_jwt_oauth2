package com.example.securityTest.service;

import com.example.securityTest.Role;
import com.example.securityTest.User;
import com.example.securityTest.dto.UserSignUpDTO;
import com.example.securityTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDTO userSignUpDTO) throws Exception{
        if(userRepository.findByEmail(userSignUpDTO.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(userRepository.findByNickname(userSignUpDTO.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .email(userSignUpDTO.getEmail())
                .password(userSignUpDTO.getPassword())
                .nickname(userSignUpDTO.getNickname())
                .age(userSignUpDTO.getAge())
                .city(userSignUpDTO.getCity())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }
}
