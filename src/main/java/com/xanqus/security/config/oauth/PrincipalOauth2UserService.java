package com.xanqus.security.config.oauth;

import com.xanqus.security.config.auth.PrincipalDetails;
import com.xanqus.security.model.User;
import com.xanqus.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        //System.out.println("getAccessToken: " + userRequest.getAccessToken());
        //System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());
        OAuth2User oauth2user = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oauth2user.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("xanqus");
        String email = oauth2user.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity =  userRepository.findByUsername(username);

        if(userEntity==null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oauth2user.getAttributes());
    }
}
