package com.momong.backend.global.exception.constant;

import com.momong.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error code 목록
 *
 * <ul>
 *     <li>1001 ~ 1999: 일반 예외. 아래 항목에 해당하지 않는 대부분의 예외가 여기에 해당한다.</li>
 *     <li>15XX: 인증 관련 예외</li>
 *     <li>2000 ~ 2499: 회원({@link Member}) 관련 예외</li>
 * </ul>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CustomExceptionType {

    /**
     * 로그인, 인증 관련 예외
     */
    ACCESS_DENIED(1500, "접근이 거부되었습니다."),
    UNAUTHORIZED(1501, "유효하지 않은 인증 정보로 인해 인증 과정에서 문제가 발생하였습니다."),
    TOKEN_VALIDATE(1502, "유효하지 않은 token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),
    ACCESS_TOKEN_VALIDATE(1503, "유효하지 않은 access token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),
    REFRESH_TOKEN_VALIDATE(1504, "유효하지 않은 refresh token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),

    /**
     * 회원({@link Member}) 관련 예외
     */
    MEMBER_NOT_FOUND(2000, "회원을 찾을 수 없습니다."),
    ;

    private final Integer code;
    private final String message;
}
