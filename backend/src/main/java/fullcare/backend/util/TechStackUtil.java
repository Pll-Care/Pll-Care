package fullcare.backend.util;

import fullcare.backend.util.dto.TechStack;
import fullcare.backend.util.dto.TechStackResponse;
import fullcare.backend.util.dto.TechStackDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TechStackUtil {
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


    public static String listToString(List<TechStack> techStacks){
        String string = techStacks.stream().toString();
        System.out.println("스트림 string = " + string);
        String s = null;
        for (int i=0; i< techStacks.size();i++){
            if(i==0){
                s = techStacks.size() == 1 ? techStacks.get(i).getValue() : techStacks.get(i).getValue() + "," ;
            }else if(i == techStacks.size() - 1){
                s += techStacks.get(i).getValue();
            }else{
                s += techStacks.get(i).getValue() + ",";
            }
        }
        return s;
    }

    public static List<String> stringToList(String techStack){
        return Arrays.stream(techStack.split(",")).collect(Collectors.toList());
    }
}
