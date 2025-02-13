package com.momong.five_min_raid.global.auth;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.global.auth.dto.JwtTokenInfoDto;
import com.momong.five_min_raid.global.auth.exception.TokenValidateException;
import com.momong.five_min_raid.global.common.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;

    // Expiration Time
    private static final long MINUTE = 1000 * 60L;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long ACCESS_TOKEN_EXPIRED_DURATION = 12 * HOUR; // Access token 만료시간 : 12시간
    public static final long REFRESH_TOKEN_EXPIRED_DURATION = 7 * DAY; // Refresh token 만료시간 : 일주일

    private static final String TOKEN_TYPE_BEARER = "Bearer ";

    private static final String ROLE_CLAIM_KEY = "role";

    private Key secretKey;

    /**
     * 객체 초기화, jwt secret key를 Base64로 인코딩
     */
    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Jwt access token을 생성하여 반환한다.
     *
     * @param memberDto 회원 정보가 담긴 dto
     * @return 생성한 jwt token
     */
    public JwtTokenInfoDto createAccessToken(MemberDto memberDto) {
        return createJwtToken(memberDto, ACCESS_TOKEN_EXPIRED_DURATION);
    }

    /**
     * Jwt refresh token을 생성하여 반환한다.
     *
     * @param memberDto 회원 정보가 담긴 dto
     * @return 생성한 jwt token
     */
    public JwtTokenInfoDto createRefreshToken(MemberDto memberDto) {
        return createJwtToken(memberDto, REFRESH_TOKEN_EXPIRED_DURATION);
    }

    /**
     * JWT token에서 사용자 정보 조회 후 security login 과정(UsernamePasswordAuthenticationToken)을 수행한다.
     *
     * @param token Jwt token
     * @return Token을 통해 조회한 사용자 정보
     */
    public Authentication getAuthentication(String token) {
        UserDetails principal = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    /**
     * Request의 header에서 token을 읽어온다.
     *
     * @param request Request 객체
     * @return Header에서 추출한 token
     */
    public String findAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_TYPE_BEARER)) {
            return null;
        }
        return authorizationHeader.substring(TOKEN_TYPE_BEARER.length());
    }

    /**
     * 토큰의 유효성, 만료일자 검증
     *
     * @param token 검증하고자 하는 JWT token
     * @throws TokenValidateException Token 값이 잘못되거나 만료되어 유효하지 않은 경우
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException ex) {
            throw new TokenValidateException("The claimsJws argument does not represent an Claims JWS", ex);
        } catch (MalformedJwtException ex) {
            throw new TokenValidateException("The claimsJws string is not a valid JWS", ex);
        } catch (SignatureException ex) {
            throw new TokenValidateException("The claimsJws JWS signature validation fails", ex);
        } catch (ExpiredJwtException ex) {
            throw new TokenValidateException("The specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.", ex);
        } catch (IllegalArgumentException ex) {
            throw new TokenValidateException("The claimsJws string is null or empty or only whitespace", ex);
        }
    }

    /**
     * Subject(socialUid), 로그인 type, token 만료 시간을 전달받아 JWT token을 생성한다.
     * 현재 access token과 refresh token을 생성할 때 만료 시간 외의 정보는 동일하므로 method를 통일하였다.
     *
     * @param memberDto            회원 정보가 담긴 dto
     * @param tokenExpiredDuration Token 만료 시간
     * @return 생성된 jwt token과 만료 시각이 포함된 <code>JwtTokenInfoDto</code> 객체
     */
    private JwtTokenInfoDto createJwtToken(MemberDto memberDto, Long tokenExpiredDuration) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + tokenExpiredDuration);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(memberDto.getId()))
                .claim(ROLE_CLAIM_KEY, memberDto.getRoleTypes())
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return new JwtTokenInfoDto(token, new Timestamp(expiresAt.getTime()).toLocalDateTime());
    }

    /**
     * 토큰에서 회원 정보(username)를 추출한다. 이 때 username은 회원의 id(PK) 값.
     *
     * @param token Jwt token
     * @return 추출한 회원 정보(username == email)
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Claims 정보를 추출한다.
     *
     * @param token 정보를 추출하고자 하는 jwt token
     * @return token에서 추출한 Claims 정보
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
