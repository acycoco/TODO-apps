package com.example.todo.service.team;

import com.example.todo.dto.team.TeamJoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockTeamFacade {
    private final RedissonClient redissonClient;
    private final TeamService teamService;

    public void joinTeam(final Long userId, final TeamJoinDto teamJoinDto, final Long teamId){
        RLock lock = redissonClient.getLock(teamId.toString());

        try {
            boolean availabe = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!availabe){
                return;
            }

            teamService.joinTeam(userId, teamJoinDto, teamId);
        } catch (ResponseStatusException e) {
            log.info("Retry stopped : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
             // ResponseStatusException 발생 시 루프를 종료
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
