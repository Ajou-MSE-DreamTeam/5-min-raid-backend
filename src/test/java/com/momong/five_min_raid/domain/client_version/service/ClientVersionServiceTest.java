package com.momong.five_min_raid.domain.client_version.service;

import com.momong.five_min_raid.domain.client_version.entity.ClientVersion;
import com.momong.five_min_raid.domain.client_version.repository.ClientVersionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ClientVersionServiceTest {

    @InjectMocks
    private ClientVersionService sut;

    @Mock
    private ClientVersionRepository clientVersionRepository;

    @DisplayName("최신 클라이언트 버전을 조회하면, 조회된 버전이 반환된다.")
    @Test
    void given_whenGetLatestClientVersion_thenReturnVersion() throws Exception {
        // given
        String expectedResult = "1.0.0";
        given(clientVersionRepository.findTop1ByOrderByCreatedAtDesc())
                .willReturn(Optional.of(createClientVersion(expectedResult)));

        // when
        String actualResult = sut.getLatestClientVersion();

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
        then(clientVersionRepository).should().findTop1ByOrderByCreatedAtDesc();
        then(clientVersionRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("클라이언트 버전이 정의되지 않은 상태에서, 최신 클라이언트 버전을 조회하면, 예외가 발생한다.")
    @Test
    void givenIfNotDefineClientVersion_whenGetLatestClientVersion_thenThrowException() throws Exception {
        // given
        given(clientVersionRepository.findTop1ByOrderByCreatedAtDesc())
                .willReturn(Optional.empty());

        // when
        Throwable ex = catchThrowable(() -> sut.getLatestClientVersion());

        // then
        assertThat(ex).isInstanceOf(NoSuchElementException.class);
        then(clientVersionRepository).should().findTop1ByOrderByCreatedAtDesc();
        then(clientVersionRepository).shouldHaveNoMoreInteractions();
    }

    private ClientVersion createClientVersion(String version) throws Exception {
        Constructor<ClientVersion> constructor = ClientVersion.class.getDeclaredConstructor(Long.class, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(101L, version);
    }
}