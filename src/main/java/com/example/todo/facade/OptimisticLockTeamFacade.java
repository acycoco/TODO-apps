package com.example.todo.facade;

import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.service.team.TeamService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockTeamFacade {

    private final TeamService teamService;

    public void joinTeam(Long userId, TeamJoinDto teamJoinDto, Long teamId) throws InterruptedException {
        int maxRetries = 7; // 재시도 횟수
        int retries = 0;
        long initialWaitTime = 100; // 초기 대기 시간을 100ms로 설정함

        while (retries < maxRetries) {
            try {
                teamService.joinTeam(userId, teamJoinDto, teamId);
                break;
            } catch (ResponseStatusException e) {
                log.info("Retry stopped : {}", e.getMessage());
                break;
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
                log.warn("Optimistic lock exception occurred. Retrying...");
                Thread.sleep(initialWaitTime);
                initialWaitTime *= 2; //재시도할 때마다 대기 시간을 2배로 증가
                retries++;
            }
        }

        if (retries == maxRetries) {
            log.error("Max retry count reached. Failed to join the team.");
        }
    }
}
