package fullcare.backend.schedule.controller;

import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.errorcode.ScheduleErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.global.exceptionhandling.exception.NotFoundCategoryException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.service.ProjectMemberService;
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

@RequestMapping("/api/auth/schedule")
@RestController
@RequiredArgsConstructor
@Tag(name = "일정", description = "일정 관련 API")
public class ScheduleController {
    private final MeetingService meetingService;
    private final MilestoneService milestoneService;
    private final ScheduleService scheduleService;
    private final ProjectMemberService projectMemberService;

    @Operation(method = "post", summary = "일정 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping// 나중에 프로젝트 시작, 종료일정 밖에 있는 일정 생성을 못하게 해야함. 테스트 데이터 생성을 활용할 때는 사용 x
    public ResponseEntity create(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.MILESTONE)) {
            milestoneService.createMilestone(scheduleCreateRequest, member);
        } else if (scheduleCreateRequest.getCategory().equals(ScheduleCategory.MEETING)) {
            meetingService.createMeeting(scheduleCreateRequest, member);
        } else {
            throw new NotFoundCategoryException(ScheduleErrorCode.CATEGORY_NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "일정 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상세 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> find(@PathVariable Long scheduleId, @Valid @RequestParam(name = "project_id") Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }
        ScheduleDetailResponse response = scheduleService.findSchedule(projectId, scheduleId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "put", summary = "일정 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity update(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }
        if (!scheduleService.updateSchedule(scheduleUpdateRequest, scheduleId)) {
            throw new InvalidAccessException(ScheduleErrorCode.INVALID_MODIFY);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "delete", summary = "일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 삭제 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity delete(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDeleteRequest scheduleDeleteRequest, @CurrentLoginMember Member member) {
        ProjectMember projectMember = projectMemberService.findProjectMember(scheduleDeleteRequest.getProjectId(), member.getId());

        //? 작성자 또는 팀 리더만 삭제 가능
        if (!scheduleService.validateDelete(scheduleId, scheduleDeleteRequest.getProjectId(), member.getId(), projectMember)) {
            throw new InvalidAccessException(ScheduleErrorCode.INVALID_DELETE);
        }
        scheduleService.deleteSchedule(scheduleId, scheduleDeleteRequest.getProjectId());
        return new ResponseEntity(HttpStatus.OK);
    }


    @Operation(method = "get", summary = "전체 일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "전체 일정 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list")
    public ResponseEntity<CustomResponseDto> list(@RequestParam(name = "project_id") Long projectId) {
        CustomResponseDto response = scheduleService.findScheduleList(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "달력 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "달력 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/calenderlist")
    public ResponseEntity<ScheduleCalenderMonthResponse> calenderViewList(@Valid @RequestParam("project_id") Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }
        ScheduleCalenderMonthResponse response = scheduleService.findScheduleCalenderList(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "월별 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "월별 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/monthlist")
    public ResponseEntity<CustomPageImpl<ScheduleMonthResponse>> calenderList(CustomPageRequest pageRequest,
                                                                              @Valid @RequestParam("project_id") Long projectId,
                                                                              @Valid @RequestParam int year,
                                                                              @Valid @RequestParam int month,
//                                                                        @Valid @RequestParam(required = false, defaultValue = "TBD,ONGOING") List<State> state  ,
                                                                              @CurrentLoginMember Member member) {
//        List<State> states = new ArrayList<>();
//        if (state.equals(State.TBD)||state.equals(State.ONGOING)){ states.add(state); states.add(State.ONGOING);}else{states.add(State.COMPLETE);}
        PageRequest of = pageRequest.of("startDate");
        Pageable pageable = (Pageable) of;
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }
        CustomPageImpl<ScheduleMonthResponse> response = scheduleService.findScheduleMonthList(pageable, year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "일정 필터 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 필터 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/search")
    public ResponseEntity<CustomPageImpl<ScheduleSearchResponse>> searchList(CustomPageRequest pageRequest,
                                                                             ScheduleCondition scheduleCondition,
                                                                             @CurrentLoginMember Member member) {
//        List<State> states = new ArrayList<>();
//        if (state.equals(State.TBD)||state.equals(State.ONGOING)){ states.add(state); states.add(State.ONGOING);}else{states.add(State.COMPLETE);}
        PageRequest of = pageRequest.of("startDate");
        Pageable pageable = (Pageable) of;
        if (!(projectMemberService.validateProjectMember(scheduleCondition.getProjectId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        CustomPageImpl<ScheduleSearchResponse> response = scheduleService.searchScheduleList(pageable, member, scheduleCondition);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping("/{scheduleId}/state")
    public ResponseEntity updateState(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleStateUpdateRequest scheduleStateUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(scheduleStateUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }
        scheduleService.updateState(scheduleStateUpdateRequest, scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @Operation(method = "get", summary = "개인 일정 조회")
//    @ApiResponses(value = {
//            @ApiResponse(description = "개인 일정 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = ScheduleMyListResponse.class))})
//
//    })
//    @GetMapping("/mine")
//    public ResponseEntity findMine(@Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest,@CurrentLoginMember Member member){
//        if (!(projectMemberService.validateProjectMember(scheduleMonthRequest.getProjectId(), member.getId()))) {
//            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
//        }
//        ScheduleMyListResponse response = scheduleService.findMySchedule(scheduleMonthRequest, member.getId());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


}
