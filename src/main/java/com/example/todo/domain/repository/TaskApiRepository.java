package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TaskApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskApiRepository extends JpaRepository<TaskApiEntity, Long> {
    List<TaskApiEntity> findAllByTeamId(Long teamId);
    List<TaskApiEntity> findAllByTeamIdAndUserId(Long teamId, Long userId);
}