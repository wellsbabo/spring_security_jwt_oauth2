package com.example.securityTest.common.oauth2.handler;

import com.example.securityTest.Role;
import com.example.securityTest.common.jwt.service.JwtService;
import com.example.securityTest.common.oauth2.CustomOAuth2User;
import com.example.securityTest.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인이 성공한다면, OAuth2LoginSuccessHandler의 로직이 실행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        try{
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if(oAuth2User.getRole() == Role.GUEST){
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("oauth2/sign-up");    // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                jwtService.sendAccessAndRefreshToken(response, accessToken, null);

                /**
                 * Role을 GUEST -> USER로 업데이트하는 로직
                 * 지금은 회원가입 추가 폼 입력 시 업데이트하는 컨트롤러를 만들지 않아서 저렇게 놔뒀습니다.
                 * 이후에 회원가입 추가 폼 입력 시 업데이트하는 컨트롤러, 서비스를 만들면 그 시점에 Role Update를 진행
                 */
//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();

            /**
             * authentication.getPrincipal()로 받아온 CustomOAuth2User의 getRole이 GUEST가 아니라면,
             * 추가 정보를 기입하고 이미 한 번 로그인 했던 유저이므로,
             * Token만 발급하여 헤더에 실어 보냅니다.
             */
            }else{
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }

        }catch (Exception e){
            throw e;
        }
    }


    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException{
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(),"Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(),"Bearer " + refreshToken);

//        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);  // 위에서 추가를 했는데 또 추가?? 없어도 되지않나
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
