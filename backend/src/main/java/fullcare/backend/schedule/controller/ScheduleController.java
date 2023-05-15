package fullcare.backend.schedule.controller;

import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.dto.request.*;
import fullcare.backend.schedule.dto.response.*;
import fullcare.backend.schedule.service.MeetingService;
import fullcare.backend.schedule.service.MilestoneService;
import fullcare.backend.schedule.service.ScheduleService;
import fullcare.backend.security.jwt.CurrentLoginMember;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/auth/schedule")
@RestController
@RequiredArgsConstructor
@Tag(name = "일정", description = "일정 관련 API")
public class ScheduleController {
    private final MeetingService meetingService;
    private final MilestoneService milestoneService;
    private final ScheduleService scheduleService;
    private final ProjectMemberService projectMemberService;

    @Operation(method = "get", summary = "프로젝트 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 멤버 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleServingResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> projectMemberList(@RequestParam(name = "project_id") Long projectId) {
        List<ScheduleServingResponse> response = scheduleService.findProjectMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 생성 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.개발일정)) {
            milestoneService.createMilestone(scheduleCreateRequest, member.getName());
        } else if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.미팅)) {
            meetingService.createMeeting(scheduleCreateRequest, member.getName());
        } else {
            throw new RuntimeException("없는 카테고리입니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "일정 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상세 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDetailResponse.class))})
    })
    @GetMapping("/{scheduleId}")
    public ResponseEntity find(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDetailRequest scheduleDetailRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleDetailRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        ScheduleDetailResponse response = scheduleService.findSchedule(scheduleDetailRequest.getProjectId(), scheduleId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "put", summary = "일정 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity update(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        if (!scheduleService.updateSchedule(scheduleUpdateRequest, scheduleId)) {
            throw new RuntimeException("해당 사용자는 일정을 변경할 수 없습니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "delete", summary = "일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 삭제 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity delete(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDeleteRequest scheduleDeleteRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleDeleteRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "전체 일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "전체 일정 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponseDto.class))})
    })
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name = "project_id") Long projectId) {
        CustomResponseDto<ScheduleListResponse> response = scheduleService.findScheduleList(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(method = "get", summary = "달력 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "달력 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleCalenderMonthResponse.class))})
    })
    @GetMapping("/calenderList")
    public ResponseEntity<?> calenderViewList(@Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleMonthRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        ScheduleCalenderMonthResponse response = scheduleService.findScheduleCalenderList(scheduleMonthRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "월별 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "월별 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleMonthResponse.class))})
    })
    @GetMapping("/monthList")
    public ResponseEntity<?> calenderList(CustomPageRequest pageRequest, @Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of("startDate");
        Pageable pageable = (Pageable) of;
        if (!(projectMemberService.validateProjectMember(scheduleMonthRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        Page<ScheduleMonthResponse> response = scheduleService.findScheduleMonthList(pageable, scheduleMonthRequest, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/{scheduleId}/state")
    public ResponseEntity updateState(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleStateUpdateRequest scheduleStateUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleStateUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        scheduleService.updateState(scheduleStateUpdateRequest, scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "개인 일정 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인 일정 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleMyListResponse.class))})

    })
    @GetMapping("/mine")
    public ResponseEntity findMine(@Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleMonthRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        ScheduleMyListResponse response = scheduleService.findMySchedule(scheduleMonthRequest, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
