import { useState } from "react";
import Button from "../../common/Button";
import { toast } from "react-toastify";

const InfoMySelf = () => {
  const [isModify, setIsModify] = useState(false);
  const [myIntro, setMyIntro] = useState("한 줄로 자기를 소개해봐요.");

  const completedModify = () => {
    if (myIntro.length < 1) {
      toast.error("1글자 이상은 반드시 입력해야합니다.");
    } else {
      setIsModify(false);
      toast.success("수정이 완료되었습니다.");
    }
  };

  return (
    <div className="profile_introduce_info_myself">
      {isModify ? (
        <>
          <input
            type="text"
            value={myIntro}
            placeholder="한 줄 자기소개를 입력해주세요."
            onChange={(e) => setMyIntro(e.target.value)}
          />
          <Button
            text="수정 완료"
            size="small"
            type="profile"
            onClick={completedModify}
          />
        </>
      ) : (
        <>
          <p>{myIntro}</p>
          <Button
            text="수정"
            size="small"
            type="profile"
            onClick={() => setIsModify(true)}
          />
        </>
      )}
    </div>
  );
};

export default InfoMySelf;
