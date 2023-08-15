package com.example.todo.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamReposiotry extends JpaRepository<TeamEntity, Long> {
}
