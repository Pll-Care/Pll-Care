package fullcare.backend.profile;

public enum TechStack {
    // ? Back 관련
    SPRING("Spring"), SPRINGBOOT("SpringBoot"), DJANGO("Django"),
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
}
