package fullcare.backend.schedule.controller;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.ScheduleErrorCode;
import fullcare.backend.global.exceptionhandling.exception.NotFoundCategoryException;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.ScheduleCondition;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.dto.request.ScheduleDeleteRequest;
import fullcare.backend.schedule.dto.request.ScheduleStateUpdateRequest;
import fullcare.backend.schedule.dto.request.ScheduleUpdateRequest;
import fullcare.backend.schedule.dto.response.*;
import fullcare.backend.schedule.service.MeetingService;
import fullcare.backend.schedule.service.MilestoneService;
import fullcare.backend.schedule.service.ScheduleService;
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

@RequestMapping("/api/auth/schedule")
@RestController
@RequiredArgsConstructor
@Tag(name = "일정", description = "일정 관련 API")
public class ScheduleController {
    private final MeetingService meetingService;
    private final MilestoneService milestoneService;
    private final ScheduleService scheduleService;

    // * READ API
    @Operation(method = "get", summary = "일정 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상세 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "일정 상세 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> find(@PathVariable Long scheduleId, @Valid @RequestParam(name = "project_id") Long projectId, @CurrentLoginMember Member member) {
        ScheduleDetailResponse response = scheduleService.findSchedule(scheduleId, projectId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "전체 일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "전체 일정 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "전체 일정 리스트 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<OverallScheduleResponse> list(@RequestParam(name = "project_id") Long projectId, @CurrentLoginMember Member member) {
        OverallScheduleResponse response = scheduleService.findScheduleList(projectId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "달력 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "달력 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "달력 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/calenderlist")
    public ResponseEntity<ScheduleCalenderMonthResponse> calenderViewList(@Valid @RequestParam("project_id") Long projectId, @CurrentLoginMember Member member) {
        ScheduleCalenderMonthResponse response = scheduleService.findScheduleCalenderList(projectId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "오늘 일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "오늘 일정 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "오늘 일정 리스트 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/daily")
    public ResponseEntity<List<ScheduleDailyResponse>> dailyList(@Valid @RequestParam("project_id") Long projectId, @CurrentLoginMember Member member) {
        List<ScheduleDailyResponse> response = scheduleService.findDailySchedule(projectId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "일정 필터 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 필터 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "일정 필터 리스트 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<CustomPageImpl<ScheduleSearchResponse>> searchList(CustomPageRequest pageRequest,
                                                                             ScheduleCondition scheduleCondition,
                                                                             @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of("startDate");
        Pageable pageable = (Pageable) of;
        CustomPageImpl<ScheduleSearchResponse> response = scheduleService.searchScheduleList(pageable, member, scheduleCondition);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // * CREATE API
    @Operation(method = "post", summary = "일정 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(description = "일정 생성 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping// 나중에 프로젝트 시작, 종료일정 밖에 있는 일정 생성을 못하게 해야함. 테스트 데이터 생성을 활용할 때는 사용 x
    public ResponseEntity create(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest, @CurrentLoginMember Member member) {

        if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.MILESTONE)) {
            milestoneService.createMilestone(scheduleCreateRequest, member.getId());
        } else if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.MEETING)) {
            meetingService.createMeeting(scheduleCreateRequest, member.getId());
        } else {
            throw new NotFoundCategoryException(ScheduleErrorCode.CATEGORY_NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // * UPDATE API
    @Operation(method = "put", summary = "일정 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(description = "일정 변경 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity update(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest, @CurrentLoginMember Member member) {
        scheduleService.updateSchedule(scheduleUpdateRequest, scheduleId, member.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(description = "일정 상태 변경 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{scheduleId}/state")
    public ResponseEntity updateState(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleStateUpdateRequest scheduleStateUpdateRequest, @CurrentLoginMember Member member) {
        scheduleService.updateState(scheduleStateUpdateRequest, scheduleId, member.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    // * DELETE API
    @Operation(method = "delete", summary = "일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 삭제 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(description = "일정 삭제 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity delete(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDeleteRequest scheduleDeleteRequest, @CurrentLoginMember Member member) {
        if (!scheduleService.validateDelete(scheduleId, scheduleDeleteRequest.getProjectId(), member.getId(), false)) {
            throw new UnauthorizedAccessException(ScheduleErrorCode.UNAUTHORIZED_DELETE);
        }
        scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }





}
