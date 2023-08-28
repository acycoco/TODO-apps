package com.example.todo.service.subscription;

import com.example.todo.domain.entity.SubscriptionEntity;
import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.repository.*;
import com.example.todo.dto.TeamSubscriptionResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamSubscriptionService {
    private final TeamSubscriptionRepository teamSubscriptionRepository;
    private final TeamReposiotry teamReposiotry;
    private final SubscriptionRepository subscriptionRepository;
    private final TeamActiveSubscriptionRepository teamActiveSubscriptionRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private void checkIsTeamMember(TeamEntity team, Long memberId){
        if (team.getMember().stream()
                .map(member -> member.getUser().getId())
                .noneMatch(userId -> userId.equals(memberId)))
            throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);
    }
    private void checkIsTeamManager(TeamEntity team, Long managerId){
        if (!team.getManager().getId().equals(managerId))
            throw new TodoAppException(ErrorCode.NOT_MATCH_MANAGERID);
    }

    private String generateMerchantUid(){
        Instant instant = Instant.now();
        Long timestamp = instant.toEpochMilli();
        return "order_" + timestamp;
    }
    @Transactional
    public TeamSubscriptionResponseDto createTeamSubscription(Long teamId, Long subscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_SUBSCRIPTION));

        //팀 매니저인지 확인
        checkIsTeamManager(team, Long.parseLong(authentication.getName()));

        //team_subscription PENDING 상태로 생성
        TeamSubscriptionEntity teamSubscription = TeamSubscriptionEntity.builder()
                .team(team)
                .subscription(subscription)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .subscriptionStatus(SubscriptionStatus.PENDING)
                .merchantUid(generateMerchantUid())
                .subscriptionPrice(subscription.getPrice())
                .build();


        return TeamSubscriptionResponseDto.fromEntity(teamSubscriptionRepository.save(teamSubscription));
    }

    @Transactional
    public Page<TeamSubscriptionResponseDto> readAllTeamSubscription(Long teamId, Integer page, Integer limit, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        //팀원인지 확인
        checkIsTeamMember(team, Long.parseLong(authentication.getName()));

        List<TeamSubscriptionEntity> teamSubscriptions = team.getTeamSubscriptions();
        Page<TeamSubscriptionEntity> teamSubscriptionPages = new PageImpl<>(teamSubscriptions, PageRequest.of(page, limit), teamSubscriptions.size());
        return teamSubscriptionPages.map(TeamSubscriptionResponseDto::fromEntity);
    }

    @Transactional
    public TeamSubscriptionResponseDto readTeamSubscription(Long teamId, Long teamSubscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        //팀원인지 확인
        checkIsTeamMember(team, Long.parseLong(authentication.getName()));

        TeamSubscriptionEntity teamSubscription = teamSubscriptionRepository.findById(teamSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM_SUBSCRIPTION));

        if (!teamId.equals(teamSubscription.getTeam().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_TEAM_SUBSCRIPTION);


        return TeamSubscriptionResponseDto.fromEntity(teamSubscription);
    }

    @Transactional
    public TeamSubscriptionResponseDto readTeamActiveSubscription(Long teamId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        //팀원인지 확인
        checkIsTeamMember(team, Long.parseLong(authentication.getName()));

        TeamActiveSubscriptionEntity teamActiveSubscription = teamActiveSubscriptionRepository.findByTeam(team)
                        .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));


        if (teamActiveSubscription.getTeamSubscription() == null){
            throw new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION);
        }
        return TeamSubscriptionResponseDto.fromActiveEntity(teamActiveSubscription);


    }

    @Transactional
    public TeamSubscriptionResponseDto updateTeamSubscription(Long teamId, Long teamSubscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        //팀 매니저인지 확인
        checkIsTeamManager(team, Long.parseLong(authentication.getName()));

        TeamSubscriptionEntity teamSubscription = teamSubscriptionRepository.findById(teamSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM_SUBSCRIPTION));

        if (!teamId.equals(teamSubscription.getTeam().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_TEAM_SUBSCRIPTION);

        //만료로 상태변경
        teamSubscription.changeSubscriptionStatus(SubscriptionStatus.EXPIRED);

        teamSubscription.unlinkTeamActiveSubscription();

        teamActiveSubscriptionRepository.deleteAllByTeamSubscription_SubscriptionStatus(SubscriptionStatus.EXPIRED);

//        if (teamActiveSubscription.getTeamSubscription().getStartDate()) 아니면 cascade로 만료된게 반영되게
        return TeamSubscriptionResponseDto.fromEntity(teamSubscriptionRepository.save(teamSubscription));
    }



}
