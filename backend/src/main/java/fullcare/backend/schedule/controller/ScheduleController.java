package fullcare.backend.schedule.controller;

import fullcare.backend.global.State;
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
    private final ProjectMemberService projectMemberService;

    @Operation(method = "post", summary = "일정 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping// 나중에 프로젝트 시작, 종료일정 밖에 있는 일정 생성을 못하게 해야함. 테스트 데이터 생성을 활용할 때는 사용 x
    public ResponseEntity create(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest, @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(scheduleCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        if(scheduleCreateRequest.getCategory().equals(ScheduleCategory.MILESTONE)){
            milestoneService.createMilestone(scheduleCreateRequest, member.getName());
        }else if(scheduleCreateRequest.getCategory().equals(ScheduleCategory.MEETING)){
            meetingService.createMeeting(scheduleCreateRequest, member.getName());
        }else{
            throw new RuntimeException("없는 카테고리입니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "get", summary = "일정 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상세 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ScheduleDetailResponse.class))})
    })
    @GetMapping("/{scheduleId}")
    public ResponseEntity find(@PathVariable Long scheduleId, @Valid @RequestParam(name="project_id") Long projectId, @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        ScheduleDetailResponse response = scheduleService.findSchedule(projectId, scheduleId, member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(method = "put", summary = "일정 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity update(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest, @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(scheduleUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        if (!scheduleService.updateSchedule(scheduleUpdateRequest, scheduleId)){
            throw new RuntimeException("해당 사용자는 일정을 변경할 수 없습니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "delete", summary = "일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 삭제 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity delete(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDeleteRequest scheduleDeleteRequest, @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(scheduleDeleteRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "전체 일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "전체 일정 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = CustomResponseDto.class))})
    })
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name="project_id") Long projectId){
        CustomResponseDto response = scheduleService.findScheduleList(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "달력 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "달력 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = ScheduleCalenderMonthResponse.class))})
    })
    @GetMapping("/calenderList")
    public ResponseEntity<?> calenderViewList(@Valid @RequestParam("project_id") Long projectId, @Valid @RequestParam int year,  @Valid @RequestParam int month , @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        ScheduleCalenderMonthResponse response = scheduleService.findScheduleCalenderList(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "월별 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "월별 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = ScheduleMonthResponse.class))})
    })
    @GetMapping("/monthList")
    public ResponseEntity<?> calenderList(CustomPageRequest pageRequest,
                                          @Valid @RequestParam("project_id") Long projectId,
                                          @Valid @RequestParam int year,
                                          @Valid @RequestParam int month,
                                          @Valid @RequestParam(required = false, defaultValue = "TBD,ONGOING") List<State> state  ,
                                          @CurrentLoginMember Member member){
//        List<State> states = new ArrayList<>();
//        if (state.equals(State.TBD)||state.equals(State.ONGOING)){ states.add(state); states.add(State.ONGOING);}else{states.add(State.COMPLETE);}
        PageRequest of = pageRequest.of("startDate");
        Pageable pageable = (Pageable) of;
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        Page<ScheduleMonthResponse> response = scheduleService.findScheduleMonthList(pageable, year, month, member, state);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping("/{scheduleId}/state")
    public ResponseEntity updateState( @PathVariable Long scheduleId,@Valid @RequestBody ScheduleStateUpdateRequest scheduleStateUpdateRequest, @CurrentLoginMember Member member){
        if (!(projectMemberService.validateProjectMember(scheduleStateUpdateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        scheduleService.updateState(scheduleStateUpdateRequest,  scheduleId);
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
