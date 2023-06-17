package fullcare.backend.schedule.controller;

import fullcare.backend.schedule.service.MilestoneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth/milestone")
@RestController
@RequiredArgsConstructor
@Tag(name = "milestone", description = "마일스톤 API")
public class MilestoneController {
    private final MilestoneService milestoneService;
}
