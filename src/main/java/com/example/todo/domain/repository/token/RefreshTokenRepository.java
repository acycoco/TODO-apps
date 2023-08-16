package com.example.todo.domain.repository.token;

import com.example.todo.domain.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
