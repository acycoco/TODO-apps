package com.example.todo.dto.team;

import com.example.todo.domain.entity.TeamEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamOverviewDto {
    private String teamName;
    private String teamManagerName;
    private String teamDesc;

    public static TeamOverviewDto fromEntity(TeamEntity teamEntity) {
        TeamOverviewDto teamOverviewDto = new TeamOverviewDto();
        teamOverviewDto.setTeamName(teamEntity.getName());
        teamOverviewDto.setTeamManagerName(teamEntity.getManager().getUsername());
        teamOverviewDto.setTeamDesc(teamEntity.getDescription());
        return teamOverviewDto;
    }
}
