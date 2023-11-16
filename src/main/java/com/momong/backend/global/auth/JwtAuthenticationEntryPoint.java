package com.momong.backend.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momong.backend.global.exception.constant.CustomExceptionType;
import com.momong.backend.global.exception.dto.response.ErrorResponse;
import com.momong.backend.global.exception.util.ExceptionUtils;
import com.momong.backend.global.logger.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증이 필요한 endpoint에 대해 인증되지 않았을 때 동작하는 handler.
     *
     * @param request                 that resulted in an <code>AuthenticationException</code>
     * @param response                so that the user agent can begin authentication
     * @param authenticationException that caused the invocation
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        log.warn(
                "[{}] JwtAuthenticationEntryPoint.commence() ex={}",
                LogUtils.getLogTraceId(),
                ExceptionUtils.getExceptionStackTrace(authenticationException)
        );

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        new ErrorResponse(
                                CustomExceptionType.ACCESS_DENIED.getCode(),
                                CustomExceptionType.ACCESS_DENIED.getMessage()
                        )
                )
        );
    }
}
