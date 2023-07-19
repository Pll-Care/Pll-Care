import { useEffect, useState } from "react";
import Button from "../../../common/Button";
import SearchStack from "./SearchStack";
import { useProfile } from "../../../../context/ProfileContext";
import { getPositionAPI, patchProfile } from "../../../../lib/apis/profileApi";
import { toast } from "react-toastify";

import Select from "../../../../components/common/Select";
import { positionSelect } from "../../../../utils/optionData";
import StackItem from "../../../common/StackItem";

const PositionBox = () => {
  const [isModify, setIsModify] = useState(false);
  const [positionAndStack, setPositionAndStack] = useState({
    position: "",
    stack: [],
  });
  const { isMyProfile, memberId } = useProfile();

  /*
  FIXME: 
  positionAndStack state은 수정 완료 시 전송할 데이터(ui에 뿌릴 데이터가 아니다)
  화면이 마운트 됬을 때 데이터를 fetch
  positionAndStack state에 넣어주기
  자식 컴포넌트에서 입력값이 변경될 때마다 state 변경시키기
  완료버튼 누르면 데이터 전송
  */

  useEffect(() => {
    const getPosition = async () => {
      const response = await getPositionAPI(memberId);
      if (response)
        setPositionAndStack((prev) => ({
          ...prev,
          position: response.recruitPosition,
          stack: response.techStack,
        }));
    };

    if (!isModify) getPosition();
  }, [memberId, isModify]);

  //TODO: 직무와 스텍 데이터 여기서 받기

  const submitModify = async () => {
    const submitStack = positionAndStack.stack.map((stack) => stack.name);

    const reqBody = {
      recruitPosition: positionAndStack.position,
      techStack: submitStack,
    };
    console.log(reqBody);

    if (!reqBody.recruitPosition || !reqBody.techStack) {
      toast.error("개발 직무와 기술 스택을 반드시 선택해야합니다.");
    } else {
      await patchProfile(memberId, reqBody);
      setIsModify(false);
      toast.success("수정되었습니다.");
    }
  };

  const changePosition = (event) => {
    setPositionAndStack((prev) => ({ ...prev, position: event.target.value }));
  };

  const changeStack = (stack) => {
    setPositionAndStack((prev) => ({
      ...prev,
      stack: [...prev.stack, stack],
    }));
  };

  const deleteStack = (stacks) => {
    setPositionAndStack((prev) => ({
      ...prev,
      stack: [...prev.stack, ...stacks],
    }));
  };
  return (
    <div className="profile_body_introduce_Box">
      <div className="profile_body_introduce_Box_title">
        <h2>개발 직무</h2>
        <div className="profile_body_introduce_Box_title_btnBox">
          {isMyProfile ? (
            isModify ? (
              <>
                <Button
                  text="취소"
                  size="small"
                  onClick={() => setIsModify(false)}
                />
                <Button
                  type="submit"
                  text="완료"
                  size="small"
                  onClick={submitModify}
                />
              </>
            ) : (
              <Button
                text="수정"
                size="small"
                onClick={() => setIsModify(true)}
              />
            )
          ) : null}
        </div>
      </div>
      {isModify ? (
        <Select
          options={positionSelect}
          type="large"
          onChange={changePosition}
        />
      ) : (
        <div className="profile_body_introduce_positionBox_position">
          <span>{positionAndStack.position}</span>
        </div>
      )}

      <div className="profile_body_introduce_Box_title profile_mr-t">
        <h2>기술 스택</h2>
      </div>

      {isModify ? (
        <SearchStack
          stackList={positionAndStack.stack}
          changeStack={changeStack}
          memberId={memberId}
          deleteStack={deleteStack}
        />
      ) : (
        <ul className="profile_body_introduce_positionBox_stack_bc_items">
          {positionAndStack.stack.length > 0 ? (
            positionAndStack.stack.map((stack, idx) => (
              <StackItem
                name={stack.name}
                imageUrl={stack.imageUrl}
                key={idx}
              />
            ))
          ) : (
            <div>기술 스택을 작성해주세요.</div>
          )}
        </ul>
      )}
    </div>
  );
};
export default PositionBox;

// TODO: 직무도 검색할 수 있는 API가 필요

const stackList = [
  "Pathon",
  "C++",
  "JavaScript",
  "Java",
  "React-Query",
  "Styled-Component",
  "Tailwind CSS",
];

const POSITION_DATA = "FrontEnd";

// const SkillItem = ({ name, imageUrl }) => {
//   return (

//   );
// };
