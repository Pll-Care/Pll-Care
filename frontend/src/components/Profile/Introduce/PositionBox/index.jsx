import { useEffect, useState } from "react";
import Button from "../../../common/Button";
import SearchStack from "./SearchStack";
import SearchPosition from "./SearchPosition";
import { useProfile } from "../../../../context/ProfileContext";
import { getPositionAPI } from "../../../../lib/apis/profileApi";

import Select from "../../../../components/common/Select";
import { positionSelect } from "../../../../utils/optionData";

const PositionBox = () => {
  const [isModify, setIsModify] = useState(false);
  const [positionAndStack, setPositionAndStack] = useState({
    position: POSITION_DATA,
    stack: [...stackList],
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
      console.log("Position API", response);
    };

    getPosition();
  }, [memberId]);

  //TODO: 직무와 스텍 데이터 여기서 받기

  const submitModify = () => {
    console.log(positionAndStack);
    setIsModify(false);
  };

  const changePosition = (event) => {
    setPositionAndStack((prev) => ({ ...prev, position: event.target.value }));
  };

  const changeStack = (stack) => {
    setPositionAndStack((prev) => ({ ...prev, stack }));
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
          <span>{POSITION_DATA}</span>
        </div>
      )}

      <div className="profile_body_introduce_Box_title profile_mr-t">
        <h2>기술 스택</h2>
      </div>

      {isModify ? (
        <SearchStack
          //FIXME: 기술스택 데이터 fetch 받아서 내려주기
          stackList={stackList}
          changeStack={changeStack}
          memberId={memberId}
        />
      ) : (
        //FIXME: fetch 받은 데이터로 map 돌리기
        <ul className="profile_body_introduce_positionBox_stack_bc_items">
          {stackList.map((skill) => (
            <SkillItem skill={skill} key={skill} />
          ))}
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

const SkillItem = ({ skill }) => {
  return (
    <li className="profile_body_introduce_positionBox_stack_bc_item">
      <span>{skill}</span>
    </li>
  );
};
