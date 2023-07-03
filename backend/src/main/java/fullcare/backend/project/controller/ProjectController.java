package fullcare.backend.project.controller;

import fullcare.backend.global.State;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.CompletedProjectException;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.*;
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
    // TODO 권한 검증과정에서 3번과 4번이 묘하게 겹치는 구간이 있음.
    // TODO POST? PUT? PATCH?

    // * 새로운 프로젝트 생성
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
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 수정 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 수정할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 수정 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트에 대한 수정 권한이 없습니다.(리더가 아님)");
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
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 삭제 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 삭제할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 삭제 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트에 대한 삭제 권한이 없습니다.(리더가 아님)");
        }

        projectService.deleteProject(projectId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 상태 수정
    @Operation(method = "post", summary = "프로젝트 완료 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 완료 처리 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 완료 처리 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{projectId}/complete")
    public ResponseEntity complete(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 상태 수정 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 완료할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 상태 수정 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트에 대한 완료 권한이 없습니다.(리더가 아님)");
        }

        projectService.updateState(projectId, State.COMPLETE);
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 사용자가 속한 프로젝트 목록
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
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
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 멤버 역할 수정 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 멤버 역할을 수정할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 멤버 역할 수정 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트 멤버 역할 수정 권한이 없습니다.(리더가 아님)");
        }

        projectMemberService.updateProjectMemberRole(projectId, projectMemberRoleUpdateRequest.getMemberId(), projectMemberRoleUpdateRequest.getProjectMemberRole());
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 멤버에게 리더 위임
    @Operation(method = "put", summary = "프로젝트 리더 위임")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 리더 위임 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 리더 위임 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}/leaderchange")
    public ResponseEntity changeProjectLeader(@PathVariable Long projectId, @RequestBody ProjectLeaderChangeRequest projectLeaderChangeRequest, @CurrentLoginMember Member member) {
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 리더 위임 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 리더를 변경할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 리더 위임 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트 리더 위임 권한이 없습니다.(리더가 아님)");
        }

        projectMemberService.changeLeader(projectId, projectLeaderChangeRequest.getMemberId(), member.getId());

        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 추가(리더가 추가)
    @Operation(method = "post", summary = "프로젝트 멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 추가 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 추가 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{projectId}/memberin")
    public ResponseEntity acceptProjectMember(@PathVariable Long projectId, @RequestBody ProjectMemberAddRequest request, @CurrentLoginMember Member member) {
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 멤버 추가 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 멤버를 추가할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 리더 위임 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트 멤버 추가 권한이 없습니다.(리더가 아님)");
        }

        projectMemberService.updateProjectMemberRole(projectId, request.getMemberId(), new ProjectMemberRole(ProjectMemberRoleType.팀원, request.getPosition()));

        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 멤버 탈퇴 (본인이 탈퇴)
    @Operation(method = "delete", summary = "프로젝트 멤버 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 탈퇴 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 탈퇴 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}/selfout")
    public ResponseEntity selfOutProjectMember(@PathVariable Long projectId, @RequestBody ProjectMemberDeleteRequest request, @CurrentLoginMember Member member) {
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 탈퇴 처리 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 프로젝트 멤버가 탈퇴할 수 없습니다.");
        }

        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.")); // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden

        // ! 리더인 경우 -> 자기를 제외한 모두를 탈퇴시킬 수 있음(본인이 탈퇴하려면 누군가에게 리더를 위임해야함)
        if (findProjectMember.isLeader()) {
            throw new InvalidAccessException("프로젝트 리더는 스스로 탈퇴할 수 없습니다.");
        } else if (findProjectMember.getMember().getId() != request.getMemberId()) {
            throw new InvalidAccessException("프로젝트 팀원은 타인을 탈퇴시킬 수 없습니다.");
        }

        projectMemberService.deleteProjectMember(projectId, request.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 삭제 (리더가 강퇴)
    @Operation(method = "delete", summary = "프로젝트 멤버 강퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 강퇴 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 강퇴 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}/memberout")
    public ResponseEntity kickOutProjectMember(@PathVariable Long projectId, @RequestBody ProjectMemberDeleteRequest request, @CurrentLoginMember Member member) {
        // ! 1. 존재하지 않는 프로젝트에 접근 -> 404 Not Found
        Project project = projectService.findProject(projectId);

        // ! 2. 완료된 프로젝트 -> 멤버 강퇴 불가 -> 403 Forbidden
        if (project.getState() == State.COMPLETE) {
            throw new CompletedProjectException("이미 완료된 프로젝트는 멤버를 강퇴할 수 없습니다.");
        }

        // ! 3. 프로젝트에 소속되지 않아서 접근 권한없음 -> 403 Forbidden
        ProjectMember findProjectMember = project.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(member.getId())).findAny()
                .orElseThrow(() -> new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다."));

        // ! 4. 프로젝트에 소속되어있지만 리더가 아니라서 강퇴 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException("프로젝트 멤버 강퇴 권한이 없습니다.(리더가 아님)");
        } else if (findProjectMember.getMember().getId() == request.getMemberId()) {
            throw new InvalidAccessException("프로젝트 리더는 자신을 강퇴할 수 없습니다.");
        }

        projectMemberService.deleteProjectMember(projectId, request.getMemberId());
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
