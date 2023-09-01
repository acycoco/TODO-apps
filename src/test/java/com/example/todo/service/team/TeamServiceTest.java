package com.example.todo.service.team;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.team.TeamCreateDto;
import com.example.todo.dto.team.TeamJoinDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    TeamService teamService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamReposiotry teamReposiotry;

    @Autowired
    UserRepository userRepository;

    User testUser;

//    @BeforeEach
//    void setUp() {
//        testUser = createUser();
//        for (int i = 0; i < 1000; i++) {
//            createUser();
//        }
//    }

    @DisplayName("새로 만들어진 팀에 회원 한 명이 가입하는 테스트")
    @Test
    void joinTeamWithOnePerson() {
        // given
        User user = createUser();
        createTeam(user.getId());

        User user1 = createUser();

        // when
        TeamJoinDto joinDto = TeamJoinDto.builder()
                .joinCode("참여코드")
                .build();
        teamService.joinTeam(user1.getId(), joinDto, 1L);
        List<MemberEntity> all = memberRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
    }

    @DisplayName("제한된 팀 숫자에 여러명이 동시에 가입하는 테스트")
    @Test
    void joinTeamWithRaceCondition() throws InterruptedException {
        // given
        User user = createUser();
        createTeam(user.getId());
        TeamJoinDto joinDto = TeamJoinDto.builder()
                .joinCode("참여코드")
                .build();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            User user1 = createUser();
//            long id = i + 3;
            executorService.submit(() -> {
                try {
                    teamService.joinTeam(user1.getId(), joinDto, 1L);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("e = " + e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<TeamEntity> all = teamReposiotry.findAll();
        List<MemberEntity> all1 = memberRepository.findAll();
        System.out.println("all1.size() = " + all1.size());

        // then
        assertThat(all.get(0).getParticipantNum()).isEqualTo(5);
    }

    private User createUser() {
        return userRepository.saveAndFlush(User.builder()
                .username("username")
                .password("password")
                .build());
    }

    private void createTeam(Long userId) {
        TeamCreateDto createDto = TeamCreateDto.builder()
                .name("구매팀")
                .joinCode("참여코드")
                .participantNum(5)
                .description("구매팀입니다.")
                .build();
        teamService.createTeam(userId, createDto);
    }

}