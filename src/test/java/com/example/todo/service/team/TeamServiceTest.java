package com.example.todo.service.team;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.team.TeamCreateDto;
import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.facade.OptimisticLockTeamFacade;
import com.example.todo.facade.RedissonLockTeamFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    TeamService teamService;

    @Autowired
    OptimisticLockTeamFacade optimisticLockTeamFacade;

    @Autowired
    RedissonLockTeamFacade redissonLockStockFacade;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamReposiotry teamReposiotry;

    @Autowired
    UserRepository userRepository;

//    User testUser;
//
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
        // 현재, 100번 동시에 요청이 된다고 가정하면 Member 엔티티 size 값이 5를 넘는 현상이 발생한다.
        // 그리고 100번이 아니고 한 30번? 정도만 하면 Team 엔티티 participantNum 컬럼 값이 5가 아니다.
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
                    redissonLockStockFacade.joinTeam(user1.getId(), joinDto, 1L);
//                    optimisticLockTeamFacade.joinTeam(user1.getId(), joinDto, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<TeamEntity> all = teamReposiotry.findAll();
        List<MemberEntity> members = memberRepository.findAll();
        System.out.println("members.size() = " + members.size());

        // then
        //manager까지 101명
        assertThat(all.get(0).getParticipantNum()).isEqualTo(101);
        assertThat(members.size()).isEqualTo(101);

        List<Long> list = new ArrayList<>();
        for (MemberEntity member : members) {
            list.add(member.getUser().getId());
        }
        Collections.sort(list);
        for (Long l : list) {
            System.out.println("l = " + l);
        }
    }

    @DisplayName("제한된 팀 숫자에 여러명이 동시에 탈퇴하는 테스트")
    @Test
    void leaveTeamWithRaceCondition() throws InterruptedException {

        // given
        User user = createUser();
        createTeam(user.getId());
        TeamJoinDto joinDto = TeamJoinDto.builder()
                .joinCode("참여코드")
                .build();

        int threadCount = 100;
        //user 100명 생성 및 팀가입
        for (int i = 0; i < threadCount; i++) {
            User user1 = createUser();
            redissonLockStockFacade.joinTeam(user1.getId(), joinDto, 1L);
        }

        //manager까지 101명 가입
        List<User> users = userRepository.findAll();
        List<TeamEntity> team = teamReposiotry.findAll();
        List<MemberEntity> members = memberRepository.findAll();

        int participantNumAfterTeamJoin = team.get(0).getParticipantNum();
        int memberAfterTeamJoin = members.size();

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Long> memberList = new ArrayList<>();
        for (MemberEntity member : members){
            memberList.add(member.getId());
        }

        for (Long userId : memberList){
            if (userId.equals(user.getId())){
                System.out.println("건너뜁니다");
                continue;
            }

            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.leaveTeam(userId, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();

        team = teamReposiotry.findAll();
        members = memberRepository.findAll();

        int participantNumAfterTeamLeave = team.get(0).getParticipantNum();
        int memberAfterTeamLeave = members.size();


        // then
        assertThat(users.size()).isEqualTo(101);
        assertThat(participantNumAfterTeamJoin).isEqualTo(101);
        assertThat(memberAfterTeamJoin).isEqualTo(101);

        //manager 제외하고 team leave
        assertThat(participantNumAfterTeamLeave).isEqualTo(1);
        assertThat(memberAfterTeamLeave).isEqualTo(1);
        assertThat(members.get(0).getId()).isEqualTo(1L);

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
                .participantNum(101)
                .description("구매팀입니다.")
                .build();
        teamService.createTeam(userId, createDto);
    }

}