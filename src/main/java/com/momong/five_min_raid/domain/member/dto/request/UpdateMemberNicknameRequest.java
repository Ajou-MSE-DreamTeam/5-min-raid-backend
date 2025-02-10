package com.momong.five_min_raid.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdateMemberNicknameRequest {

    @Schema(description = "변경하고자 하는 닉네임", example = "nickname")
    @Length(min = 2, max = 12)
    @NotBlank
    private String nickname;
}
