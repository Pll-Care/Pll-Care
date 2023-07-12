package fullcare.backend.util;

import fullcare.backend.util.dto.TechStack;
import fullcare.backend.util.dto.TechStackResponse;
import fullcare.backend.util.dto.TechStackDto;

public class Util {
    public static TechStackResponse findTechStack(String techStack) {
        TechStackResponse techStackResponse = new TechStackResponse();
        for (TechStack t : TechStack.values()) {
//            System.out.println("t.getValue() = " + t.getValue());
//            System.out.println("techStack = " + techStack);
            if (t.getValue().toLowerCase().startsWith(techStack.toLowerCase())) {
                techStackResponse.addTechStack(new TechStackDto(t.getValue(), null));
            }
        }
        return techStackResponse;
    }
}
