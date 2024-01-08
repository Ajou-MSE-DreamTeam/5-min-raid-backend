package com.momong.five_min_raid.global.auth.repository;

import com.momong.five_min_raid.global.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
