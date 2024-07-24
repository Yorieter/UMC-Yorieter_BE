package umc.yorieter.config.security.jwt;

import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.web.dto.response.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {  // JWT를 생성하고 검증하는 역할을 하는 클래스

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 1440 * 21;  // 1440분 * 21 = 24시간 * 21 = 3주
    private final Key key;


    // 이 @Value는 'springframework.beans.factory.annotation.Value' 소속임.
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 토큰 생성
    public TokenDTO generateTokenDto(Authentication authentication) {  // 파라미터로 전달해주는 authentication은 현재 인증 성공한 사용자를 나타내는 Authentication 객체이다.
        // (참고로 이 메소드의 파라미터 인증객체의 name 안에는 로그인계정아이디가 아닌, CustomUserDetailsService의 createUserDetails메소드에서 진행하여 나온 String으로 변환된 사용자DB의PKid가 들어있다.)

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())  // Payload에 String으로 변환해둔 사용자DB의PKid와 권한 정보가 저장되어야만한다. (아이디)
                .claim(AUTHORITIES_KEY, authorities)  // Access Token은 Refresh Token과는 다르게, Payload에 사용자의 아이디와 권한 정보가 저장되어야만한다. (권한)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();  // 컴팩트화로써, 최종적으로 JWT를 문자열로 변환하는 역할임.

        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .username(null)  // 임시
                .email(null)  // 임시
                .build();
    }

    public Authentication getAuthentication(String accessToken) {  // Access Token의 Payload에 저장된 사용자의 아이디와 권한 정보를 토대로 인증하여 Authentication 객체를 만들어 반환하는 메소드
        Claims claims = parseClaims(accessToken);  // Access Token의 Payload에 저장된 Claim을 꺼내옴. (JWT 토큰에서 사용자의 아이디와 권한 정보를 획득할 목적)

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN, "권한 정보가 없는 토큰입니다.");
        }

        // 해당 계정이 갖고있는 권한 목록들을 리턴하는 역할임.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))  // "ROLE_USER,ROLE_ADMIN"과 같은 문자열을 ','기준으로 분리함.
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);  // domain의 User가 아닌, security.core.userdetails.User 이다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);

        return authentication;  // 이 인증된 객체는 Spring Security에서 사용자의 보안 컨텍스트를 관리하고 액세스 제어 검사를 수행하는 데 사용될 수 있다.
    }

    public boolean validateToken(String token) {  // 토큰의 key서명이 일치하고 유효한지 검사하는 메소드 (JWT를 검증하고 처리하는 단계)
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {  // Access Token의 Payload에 저장된 Claim을 꺼내오는 메소드 (JWT 토큰에서 사용자의 아이디와 권한 정보를 획득할 목적)
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}