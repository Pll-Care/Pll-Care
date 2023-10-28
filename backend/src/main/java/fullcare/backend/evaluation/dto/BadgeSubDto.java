package fullcare.backend.evaluation.dto;

import lombok.Data;

@Data
public class BadgeSubDto {
    private String support = "최고의 서포터";
    private long quantity1;
    private String leader = "탁월한 리더";
    private long quantity2;
    private String participant = "열정적인 참여자";
    private long quantity3;
    private String bank = "아이디어 뱅크";
    private long quantity4;

    public void updateSupport(String support, Long quantity1) {
        this.quantity1 = quantity1;
    }
    public void updateLeader(String leader, Long quantity2){
        this.quantity2 = quantity2;
    }
    public void updateParticipant(String participant, Long quantity3){
        this.quantity3 = quantity3;
    }
    public void updateBank(String bank, Long quantity4){
        this.quantity4 = quantity4;
    }
}
