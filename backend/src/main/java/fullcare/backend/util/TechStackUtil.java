package fullcare.backend.util;

import fullcare.backend.s3.S3Service;
import fullcare.backend.util.dto.TechStack;
import fullcare.backend.util.dto.TechStackDto;
import fullcare.backend.util.dto.TechStackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechStackUtil {
    private final S3Service s3Service;
    public TechStackResponse findTechStack(String techStack) {
        TechStackResponse techStackResponse = new TechStackResponse();
        for (TechStack t : TechStack.values()) {

            if (t.getValue().toLowerCase().startsWith(techStack.toLowerCase())) {
//                System.out.println("t.getValue() = " + t.getValue());
//                System.out.println("techStack = " + techStack);
                techStackResponse.addTechStack(new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType())));
            }

        }
        return techStackResponse;
    }


    public static String listToString(List<TechStack> techStacks) {
//        String string = techStacks.stream().toString();
//        System.out.println("스트림 string = " + string);
        String s = null;
        for (int i = 0; i < techStacks.size(); i++) {
            if (i == 0) {
                s = techStacks.size() == 1 ? techStacks.get(i).getValue() : techStacks.get(i).getValue() + ",";
            } else if (i == techStacks.size() - 1) {
                s += techStacks.get(i).getValue();
            } else {
                s += techStacks.get(i).getValue() + ",";
            }
        }
        return s;
    }

    public static List<TechStack> stringToList(String techStack) {
        return Arrays.stream(techStack.split(",")).map(s -> TechStack.valueOf(s)).collect(Collectors.toList());
    }



}
