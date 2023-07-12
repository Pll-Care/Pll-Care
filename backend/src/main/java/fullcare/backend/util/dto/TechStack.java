package fullcare.backend.util.dto;

import java.util.List;

public enum TechStack {
    // ? Back 관련
    SPRING("Spring"), SPRINGBOOT("SpringBoot"), DJANGO("Django"), FLASK("Flask"),
    // ? Front 관련
    REACT("React"), VUE("Vue"), JAVASCRIPT("JavaScript"),
    // ? DevOps 관련
    AWS("AWS"),
    // ? DB 관련
    MYSQL("MySql"),
    // ? 언어 관련
    CSHOP("C#"), CPP("C++"), PYTHON("Python"),
    //? 형상관리
    GIT("Git")
    ;
    private final String value;
    TechStack(String value){this.value = value;}
    public String getValue(){
        return value;
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
}
