package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamReposiotry extends JpaRepository<TeamEntity, Long> {
}
