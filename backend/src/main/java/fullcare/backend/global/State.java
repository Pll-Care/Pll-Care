package fullcare.backend.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter@AllArgsConstructor
public enum State {
    TBD, ONGOING, COMPLETE,
    TEMPORARY;// 평가 임시저장 상태
}
