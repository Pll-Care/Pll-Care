package fullcare.backend.util.dto;

public enum TechStack {
    // ? Back 관련

    Spring("Spring", "svg"), SpringBoot("SpringBoot", "png"), Django("Django", "svg"), Flask("Flask", "png"),
    NodeJs("NodeJs", "svg"),
    // ? Front 관련
    React("React", "svg"), Vue("Vue", "svg"), JavaScript("JavaScript", "svg"), Redux("Redux", "png"),
    Angular("Angular", "png"), CSS3("CSS3", "svg"), Html5("Html5", "svg"), NestJs("NestJs", "svg"),
    NextJs("NextJs", "svg"), ReactNative("ReactNative", "png"), ReactQuery("ReactQuery", "png"), Typescript("Typescript", "svg"),

    // ? DevOps 관련
    AWS("AWS", "svg"),
    // ? DB 관련
    MySql("MySql", "svg"), MongoDb("MongoDb", "svg"),
    // ? 언어 관련
    CSHOP("C#", "png"), CPP("CPP", "png"), Python("Python", "svg"),
    //? 형상관리
    Git("Git", "svg"), Github("Github", "svg"),
    //? 기타
    Android("Android", "svg"), AndroidStudio("AndroidStudio", "svg"), Apple("Apple", "svg"),
    Blender("Blender", "svg"), Docker("Docker", "svg"), Express("Express", "svg"), Fastapi("Fastapi", "svg"),
    Figma("Figma", "svg"), Firebase("Firebase", "svg"), Flutter("Flutter", "svg"), Illustrator("Illustrator", "svg"),
    Java("Java", "svg"), Jira("Jira", "svg"), Kotlin("Kotlin", "svg"), Kubernetes("Kubernetes", "svg"),
    Linux("Linux", "svg"), Mobx("Mobx", "jpeg"), Photoshop("Photoshop", "svg"), Recoil("Recoil", "png"),
    Slack("Slack", "svg"), StyledComponent("StyledComponent", "png"), Svelte("Svelte", "svg"), Swift("Swift", "svg"),
    Tailwind("Tailwind", "png"), Unity("Unity", "svg"), Vscode("Vscode", "svg"), Xd("Xd", "svg"), Zeplin("Zeplin", "png");
    private final String value;
    private final String contentType;

    TechStack(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getValue() {
        return value;
    }


}
