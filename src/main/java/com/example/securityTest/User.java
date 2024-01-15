package com.example.securityTest;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    private String email;   // 이메일
    private String password;    // 비밀번호
    private String nickname;    // 닉네임
    private String imageUrl;    // 프로필 이미지
    private int age;
    private String city;    // 사는 도시

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;  //카카오, 네이버, 구글

    private String socialId;    //로그인한 소셜 타입의 식별자 값

    private String refreshToken;    // 리프레시 토큰

    // 유저 권한 설정 메서드
    public void authorizeUser(){
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메서드
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }
}
