package com.example.todo.service.team;

import com.example.todo.dto.team.TeamJoinDto;
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
//        for (int retryCount = 0; retryCount < 3; retryCount++) {
        while (true) {
            try {
//                log.info("[RETRY_COUNT]: {}", retryCount);
                teamService.joinTeam(userId, teamJoinDto, teamId);
                break;
            } catch (ResponseStatusException e) {
                log.info("retry stop: {}", e.getMessage());
                break;
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
                Thread.sleep(50);
            }
        }
//        }
    }
}
