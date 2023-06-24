package fullcare.backend.project.controller;

import fullcare.backend.global.State;
import fullcare.backend.global.dto.FailureResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.dto.request.*;
import fullcare.backend.project.dto.response.ProjectApplyResponse;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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


    // * 특정 프로젝트의 멤버 목록
    @Operation(method = "get", summary = "프로젝트 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/memberlist")
    public ResponseEntity<List<ProjectMemberListResponse>> projectMemberList(@RequestParam(name = "project_id") Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        List<ProjectMemberListResponse> response = projectService.findProjectMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // * 사용자가 속한 프로젝트 목록
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping
    public ResponseEntity<CustomPageImpl<ProjectListResponse>> list(CustomPageRequest pageRequest, @Valid @RequestParam List<State> state, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<ProjectListResponse> responses = projectService.findMyProjectList(pageable, member.getId(), state);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // * 새로운 프로젝트 생성
    @Operation(method = "post", summary = "프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@RequestBody ProjectCreateRequest projectCreateRequest, @CurrentLoginMember Member member) {

        projectService.createProject(member.getId(), projectCreateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 내용 수정
    @Operation(method = "put", summary = "프로젝트 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping("/{projectId}")
    public ResponseEntity update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        projectService.update(projectId, projectUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 삭제
    @Operation(method = "delete", summary = "프로젝트 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @DeleteMapping("{projectId}")
    public ResponseEntity delete(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        // ! 삭제 권한이 있는지 검사

        projectService.deleteProject(projectId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 상태 수정
    @Operation(method = "put", summary = "프로젝트 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 상태 변경 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 상태 변경실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping("/{projectId}/state")
    public ResponseEntity updateState(@PathVariable Long projectId, @Valid @RequestBody ProjectStateUpdateRequest projectStateUpdateRequest, @CurrentLoginMember Member member) {
        ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());
        if (!projectMember.isLeader()) { //? 프로젝트 리더만 프로젝트 상태 변경 가능
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        projectService.updateState(projectId, projectStateUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 삭제(리더가 강퇴)
    @Operation(method = "post", summary = "프로젝트 멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping("/memberout")
    public ResponseEntity deleteProjectMember(@RequestBody ProjectMemberDeleteRequest request, @CurrentLoginMember Member member) {
        ProjectMember projectMember = projectMemberService.findProjectMember(request.getProjectId(), member.getId());

        if (projectMember.getProjectMemberRole().getRole() != ProjectMemberRoleType.리더 || !(projectMemberService.validateProjectMember(request.getProjectId(), request.getMemberId()))) {
            throw new InvalidAccessException("삭제 권한이 없습니다.");
        }

        projectMemberService.deleteProjectMember(request.getProjectId(), request.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 프로젝트 지원 현황 리스트
    @Operation(method = "get", summary = "프로젝트 지원 현황 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 현황 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 현황 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/{projectId}/applylist")
    public ResponseEntity<List<ProjectApplyResponse>> findApplyList(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        List<ProjectApplyResponse> response = projectService.findApplyList(projectId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "put", summary = "프로젝트 멤버 역할 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 역할 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 역할 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping("/{projectId}/pmrole")
    public ResponseEntity updateProjectMemberRole(@PathVariable Long projectId,@RequestBody ProjectMemberRoleUpdateRequest projectMemberRoleUpdateRequest, @CurrentLoginMember Member member) {
        ProjectMember projectMember = projectMemberService.findProjectMember(projectId, member.getId());
        if (!projectMember.isLeader()) { //? 프로젝트 리더만 프로젝트 멤버 역할 수정
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        projectMemberService.updateProjectMemberRole(projectMemberRoleUpdateRequest.getPmId(), projectMemberRoleUpdateRequest.getProjectMemberRole());
        return new ResponseEntity(HttpStatus.OK);
    }
    // ! 팀 탈퇴 기능 추가 필요
    // 프로젝트 생성 썸네일 -> [새로운 프로젝트 생성, 특정 프로젝트 내용 수정] 에 들어가야함
    // ? 프로젝트 멤버 영입 -> 그냥 모집글의 지원 기능으로 대체?
}
