package fullcare.backend.project.controller;

import fullcare.backend.global.State;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.CompletedProjectException;
import fullcare.backend.project.dto.request.*;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.project.dto.response.ProjectUpdateStateResponse;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
@RequestMapping("/api/auth/project")
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    // ! API 요청시에 필수적으로 필요한 값이라면 PathVariable, 필수가 아니거나 값이 많으면 RequestParam
    // * 새로운 프로젝트 생성

    // ! TODO project가 존재하지 않는 경우와 projectMember가  존재하지 않는 경우를 어떻게 나눠서 처리해야할 것인가?
    // ! TODO 소속된 멤버의 역할 수정과 프로젝트 지원자를 받는 API를 분리해야하는가?
    // ! 분리를 하고 페이지네이션으로 구현 필요
    // ! TODO 리더가 본인을 삭제하려고 할 때, 다른 누군가에게 리더 포지션을 위임해야함 -> memberout 코드 미완성
    // ! 스스로 프로젝트 나가는 api가 없음
    @Operation(method = "post", summary = "프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ProjectCreateRequest projectCreateRequest, @CurrentLoginMember Member member) {

        projectService.createProject(member.getId(), projectCreateRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // * 특정 프로젝트 내용 수정
    @Operation(method = "put", summary = "프로젝트 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}")
    public ResponseEntity update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트에 대한 수정 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 수정할 수 없습니다.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectService.updateProject(projectId, projectUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 삭제
    @Operation(method = "delete", summary = "프로젝트 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity delete(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트에 대한 삭제 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 삭제할 수 없습니다.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectService.deleteProject(projectId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 상태 수정
    @Operation(method = "put", summary = "프로젝트 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 상태 변경 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 상태 변경실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}/state")
    public ResponseEntity<ProjectUpdateStateResponse> updateState(@PathVariable Long projectId, @Valid @RequestBody ProjectStateUpdateRequest projectStateUpdateRequest, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트 상태 수정 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 상태를 수정할 수 없습니다.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectService.updateState(projectId, projectStateUpdateRequest);
        return new ResponseEntity(new ProjectUpdateStateResponse(projectId), HttpStatus.OK);
    }

    // * 사용자가 속한 프로젝트 목록
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<CustomPageImpl<ProjectListResponse>> list(CustomPageRequest pageRequest, @RequestParam List<State> state, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<ProjectListResponse> responses = projectService.findProjectList(pageable, member.getId(), state);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // * 프로젝트에 소속된 멤버의 역할 수정
    @Operation(method = "put", summary = "프로젝트 멤버 역할 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 역할 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 역할 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}/rolechange")
    public ResponseEntity updateProjectMemberRole(@PathVariable Long projectId, @RequestBody ProjectMemberRoleUpdateRequest projectMemberRoleUpdateRequest, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트 멤버 역할 수정 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 멤버 역할을 수정할 수 없습니다.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectMemberService.updateProjectMemberRole(projectId, projectMemberRoleUpdateRequest.getMemberId(), projectMemberRoleUpdateRequest.getProjectMemberRole());
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 멤버 추가(리더가 추가)
    @Operation(method = "post", summary = "프로젝트 멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 추가 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 추가 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/memberin")
    public ResponseEntity addProjectMember(@RequestBody ProjectMemberAddRequest request, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(request.getProjectId(), member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트 멤버 추가 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 멤버를 추가할 수 없습니다.");
            }

        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectMemberService.updateProjectMemberRole(request.getProjectId(), request.getMemberId(), new ProjectMemberRole(ProjectMemberRoleType.팀원, request.getPosition()));

        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 삭제(리더가 강퇴)
    @Operation(method = "post", summary = "프로젝트 멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/memberout")
    public ResponseEntity deleteProjectMember(@RequestBody ProjectMemberDeleteRequest request, @CurrentLoginMember Member member) {
        try {
            ProjectMember projectMember = projectMemberService.findProjectMember(request.getProjectId(), member.getId());

            if (!(projectMember.isLeader())) {
                throw new InvalidAccessException("프로젝트 멤버 삭제 권한이 없습니다.(리더가 아님)");
            } else if (projectMember.getProject().getState() == State.COMPLETE) {
                throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 멤버를 삭제할 수 없습니다.");
            }


            if (projectMember.getMember().getId() == member.getId()) {
                throw new InvalidAccessException("리더는 탈퇴전 다른 누군가에게 역할을 위임해야합니다.");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        projectMemberService.deleteProjectMember(request.getProjectId(), request.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트의 지원 목록
    @Operation(method = "get", summary = "프로젝트 지원 현황 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 현황 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 현황 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/applylist")
    public ResponseEntity<List<ProjectMemberListResponse>> findApplyList(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        List<ProjectMemberListResponse> response = projectService.findApplyList(projectId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // * 특정 프로젝트의 멤버 목록
    @Operation(method = "get", summary = "프로젝트 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/memberlist")
    public ResponseEntity<List<ProjectMemberListResponse>> projectMemberList(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        List<ProjectMemberListResponse> response = projectService.findProjectMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
