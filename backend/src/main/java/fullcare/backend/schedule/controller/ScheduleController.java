package fullcare.backend.schedule.controller;

import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.dto.request.*;
import fullcare.backend.schedule.dto.response.CustomResponseDto;
import fullcare.backend.schedule.dto.response.ScheduleListResponse;
import fullcare.backend.schedule.dto.response.ScheduleMonthResponse;
import fullcare.backend.schedule.dto.response.ScheduleServingResponse;
import fullcare.backend.schedule.service.MeetingService;
import fullcare.backend.schedule.service.MilestoneService;
import fullcare.backend.schedule.service.ScheduleService;
import fullcare.backend.security.jwt.CurrentLoginUser;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/auth/schedule")
@RestController
@RequiredArgsConstructor
@Tag(name = "schedule", description = "일정 API")
public class ScheduleController {
    private final MeetingService meetingService;
    private final MilestoneService milestoneService;
    private final ScheduleService scheduleService;
    @Operation(method = "get", summary = "프로젝트 멤버")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 멤버 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleServingResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> serving(@RequestParam(name="project_id") Long projectId){
        List<ScheduleServingResponse> response = scheduleService.servingMember(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "post", summary = "일정 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 생성 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest, @CurrentLoginUser CustomOAuth2User user){
        if(scheduleCreateRequest.getCategory().equals(ScheduleCategory.개발일정)){
            milestoneService.createMilestone(scheduleCreateRequest, user.getUsername());
        }else if(scheduleCreateRequest.getCategory().equals(ScheduleCategory.미팅)){
            meetingService.createMeeting(scheduleCreateRequest, user.getUsername());
        }else{
            throw new RuntimeException("없는 카테고리입니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "put", summary = "일정 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PutMapping
    public ResponseEntity update(@Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest, @CurrentLoginUser CustomOAuth2User user){
        Long memberId = Long.parseLong(user.getName());
        if (!scheduleService.updateSchedule(scheduleUpdateRequest, memberId)){
            throw new RuntimeException("해당 사용자는 일정을 변경할 수 없습니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "delete", summary = "일정 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 삭제 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping
    public ResponseEntity delete(@Valid @RequestBody ScheduleDeleteRequest scheduleDeleteRequest, @CurrentLoginUser CustomOAuth2User user){
        Long memberId = Long.parseLong(user.getName());
        if (!scheduleService.deleteSchedule(scheduleDeleteRequest, memberId)){
            throw new RuntimeException("해당 사용자는 일정을 변경할 수 없습니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "일정 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json",  schema = @Schema(implementation = CustomResponseDto.class))})
    })
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name="project_id") Long projectId){
        CustomResponseDto<ScheduleListResponse> response = scheduleService.findScheduleList(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(method = "get", summary = "일정 월별 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 월별 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json",  schema = @Schema(implementation = ScheduleMonthResponse.class))})
    })
    @GetMapping("/monthList")
    public ResponseEntity<?> monthList(@Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest){
        ScheduleMonthResponse response = scheduleService.findScheduleMonthList(scheduleMonthRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @Operation(method = "post", summary = "일정 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "일정 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/state")
    public ResponseEntity updateState(@Valid @RequestBody ScheduleStateUpdateRequest scheduleStateUpdateRequest, @CurrentLoginUser CustomOAuth2User user){
        Long memberId = Long.parseLong(user.getName());
        if (!scheduleService.updateState(scheduleStateUpdateRequest, memberId)){
            throw new RuntimeException("해당 사용자는 일정 상태 변경을 할 수 없습니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/mine")
    public ResponseEntity find(@Valid @RequestBody ScheduleMonthRequest scheduleMonthRequest,@CurrentLoginUser CustomOAuth2User user){
        Long memberId = Long.parseLong(user.getName());
        scheduleService.findMySchedule(scheduleMonthRequest, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
