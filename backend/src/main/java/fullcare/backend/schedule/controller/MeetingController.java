package fullcare.backend.schedule.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth/meeting")
@RestController
@RequiredArgsConstructor
@Tag(name = "meeting", description = "미팅 API")
public class MeetingController {
}
