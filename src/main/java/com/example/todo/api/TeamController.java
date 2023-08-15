package com.example.todo.api;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    @GetMapping
    public String getTeamGeneratePage(Authentication authentication) {
        return "team-generate.html";
    }

    @GetMapping("/search-page")
    public String getTeamSearchPage() {
        return "team-generate.html";
    }

    @PostMapping
    public ResponseDto createTeam(Authentication authentication,
                                  TeamCreateDto teamCreateDto) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("새로운 팀 등록이 완료되었습니다.");
        return responseDto;

    }


    @PostMapping("/{teamId}/member")
    public ResponseDto joinTeam(Authentication authentication,
                                TeamJoinDto teamJoinDto,
                                @PathVariable("teamId") Long teamId) {

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀에 가입이 완료되었습니다.");
        return responseDto;
    }


    @PutMapping("/{teamId}")
    public ResponseDto updateTeamDetails(Authentication authentication,
                                         TeamUpdateDto teamUpdateDto,
                                         @PathVariable("teamId") Long teamId) {

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 정보 수정이 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}")
    public ResponseDto deleteTeam(Authentication authentication,
                                  @PathVariable("teamId") Long teamId) {

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 삭제가 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}/member")
    public ResponseDto leaveTeam(Authentication authentication,
                                 @PathVariable("teamId") Long teamId) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀을 탈퇴하였습니다.");
        return responseDto;
    }
}
