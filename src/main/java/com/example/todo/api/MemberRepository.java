package com.example.todo.api;

import com.example.todo.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByTeamAndUser(TeamEntity team, User user);
}
