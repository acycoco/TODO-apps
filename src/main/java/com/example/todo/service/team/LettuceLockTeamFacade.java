package com.example.todo.service.team;

import com.example.todo.domain.repository.RedisLockRepository;
import com.example.todo.dto.team.TeamJoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LettuceLockTeamFacade {
    private final RedisLockRepository redisLockRepository;
    private final TeamService teamService;

    public void joinTeam(final Long userId, final TeamJoinDto teamJoinDto, final Long teamId) throws InterruptedException {
        int maxRetries = 7;
        long initialWaitTime = 100; // 초기 대기 시간 100ms

        int retries = 0;
        while (retries < maxRetries) {
            if (redisLockRepository.lock(teamId)) {
                try {
                    teamService.joinTeam(userId, teamJoinDto, teamId);
                    return; // 성공적으로 팀에 가입했으므로 반복 종료
                } catch (ResponseStatusException e) {
                    log.info("Retry stopped : {}", e.getMessage());
                    break; // ResponseStatusException 발생 시 루프를 종료
                } finally {
                    redisLockRepository.unlock(teamId);
                }
            }

            // Lock 실패한 경우, 대기 시간을 2배로 늘린다.
            Thread.sleep(initialWaitTime);
            initialWaitTime *= 2;
            retries++;
        }

        if (retries == maxRetries) {
            log.error("Max retry count reached. Failed to join the team.");
        }

        // 최대 재시도 횟수를 초과한 경우 예외를 던질 수 있음
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "팀 가입 시도가 실패했습니다.");
    }
}