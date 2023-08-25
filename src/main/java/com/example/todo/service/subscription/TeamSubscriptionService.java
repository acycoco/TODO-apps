package com.example.todo.service.subscription;

import com.example.todo.domain.entity.SubscriptionEntity;
import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.repository.SubscriptionRepository;
import com.example.todo.domain.repository.TeamActiveSubscriptionRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.TeamSubscriptionRepository;
import com.example.todo.dto.TeamSubscriptionResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public TeamSubscriptionResponseDto createTeamSubscription(Long teamId, Long subscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_SUBSCRIPTION));

        //팀 매니저 확인
        Long managerId = Long.parseLong(authentication.getName());
        if (!team.getManager().getId().equals(managerId))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERID);

        //team_subscription 활성화 상태로 생성
        TeamSubscriptionEntity teamSubscription = TeamSubscriptionEntity.builder()
                .team(team)
                .subscription(subscription)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .subscriptionStatus(SubscriptionStatus.ACTIVE)
                .build();

        //team_active_subscription에 활성화 중인 subscription 저장
        TeamActiveSubscriptionEntity teamActiveSubscription = TeamActiveSubscriptionEntity.builder()
                .team(team)
                .teamSubscription(teamSubscription)
                .build();

        teamActiveSubscriptionRepository.save(teamActiveSubscription);

//        team.getTeamSubscriptions().add(teamSubscription);
//        subscription.getTeamSubscriptions().add(teamSubscription);
//        teamReposiotry.save(team);
//        subscriptionRepository.save(subscription);
        return TeamSubscriptionResponseDto.fromEntity(teamSubscriptionRepository.save(teamSubscription));
    }

    @Transactional
    public Page<TeamSubscriptionResponseDto> readAllTeamSubscription(Long teamId, Integer page, Integer limit, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        List<TeamSubscriptionEntity> teamSubscriptions = team.getTeamSubscriptions();
        Page<TeamSubscriptionEntity> teamSubscriptionPages = new PageImpl<>(teamSubscriptions, PageRequest.of(page, limit), teamSubscriptions.size());
        return teamSubscriptionPages.map(TeamSubscriptionResponseDto::fromEntity);
    }

    @Transactional
    public TeamSubscriptionResponseDto readTeamSubscription(Long teamId, Long teamSubscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

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

        TeamActiveSubscriptionEntity teamActiveSubscription = teamActiveSubscriptionRepository.findByTeam(team)
                        .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));

        return TeamSubscriptionResponseDto.fromActiveEntity(teamActiveSubscription);
//        team.getTeamSubscriptions().stream()
//                .filter(subscription -> subscription.getSubscriptionStatus() == SubscriptionStatus.ACTIVE)
//                .findFirst()
//                .orElse(null);

    }

    @Transactional
    public TeamSubscriptionResponseDto updateTeamSubscription(Long teamId, Long teamSubscriptionId, Authentication authentication){
        TeamEntity team = teamReposiotry.findById(teamId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        TeamSubscriptionEntity teamSubscription = teamSubscriptionRepository.findById(teamSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM_SUBSCRIPTION));

        if (!teamId.equals(teamSubscription.getTeam().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_TEAM_SUBSCRIPTION);

        if (teamSubscription.getEndDate().isBefore(LocalDate.now())){
            teamSubscription.changeSubscriptionStatus(SubscriptionStatus.EXPIRED);
        }

        TeamActiveSubscriptionEntity teamActiveSubscription = teamActiveSubscriptionRepository.findByTeam(team)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));

//        if (teamActiveSubscription.getTeamSubscription().getStartDate()) 아니면 cascade로 만료된게 반영되게
        teamActiveSubscriptionRepository.delete(teamActiveSubscription);
        return TeamSubscriptionResponseDto.fromEntity(teamSubscriptionRepository.save(teamSubscription));
    }



}
