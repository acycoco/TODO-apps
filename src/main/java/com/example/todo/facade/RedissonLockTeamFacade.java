package com.example.todo.facade;

import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockTeamFacade {

    private final RedissonClient redissonClient;

    private final TeamService teamService;

    public void joinTeam(final Long userId, final TeamJoinDto teamJoinDto, final Long teamId) {
        RLock lock = redissonClient.getLock(teamId.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("lock 획득 실패");
                return;
            }

            teamService.joinTeam(userId, teamJoinDto, teamId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void leaveTeam(Long userId, Long teamId){
        RLock lock = redissonClient.getLock(teamId.toString());

        try {
            boolean availabe = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!availabe){
                log.info("lock 획득 실패");
                return;
            }

            teamService.leaveTeam(userId, teamId);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
