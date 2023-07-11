package fullcare.backend.util.service;

import fullcare.backend.profile.TechStack;
import fullcare.backend.profile.TechStackResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UtilService {
    public TechStackResponse findTechStack(String techStack) {
        TechStackResponse techStackResponse = new TechStackResponse();
        for (TechStack t : TechStack.values()) {
            System.out.println("t.getValue() = " + t.getValue());
            System.out.println("techStack = " + techStack);

            if (t.getValue().toLowerCase().startsWith(techStack.toLowerCase())) {
                techStackResponse.addTechStack(t.getValue());
            }
        }
        return techStackResponse;
    }
}
