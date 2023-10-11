package com.momong.backend.global.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error code 목록
 *
 * <ul>
 *     <li>1001 ~ 1999: 일반 예외. 아래 항목에 해당하지 않는 대부분의 예외가 여기에 해당한다.</li>
 * </ul>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CustomExceptionType {
    ;

    private final Integer code;
    private final String message;
}
