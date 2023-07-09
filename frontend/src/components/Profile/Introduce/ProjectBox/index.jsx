/*
컴포넌트가 마운트 되었을 때 프로젝트 경험 API 호출 -> 수정에 대한 상태값이 변경될 때마다 API가 호출 되어야 한다.
API 데이터는 최신 프로트가 맨 앞으로 오도록 와야한다
데이터 ex)

{
  "projectExperiences": [
    {
      "title": "string",
      "description": "string",
      "startDate": "2023-07-09",
      "endDate": "2023-07-09",
      "techStack": "string",
      "projectId": 0
    }
  ],
  "myProfile": true
}



*/

import { useState } from "react";
import Button from "../../../common/Button";
import ProjectList from "./ProjectList";

const ProjectBox = () => {
  const [isModify, setIsModify] = useState(false);

  const submitModify = () => {};
  return (
    <div className="profile_body_introduce_Box">
      <div className="profile_body_introduce_Box_title">
        <h2>프로젝트 경험</h2>
        <div className="profile_body_introduce_Box_title_btnBox">
          {isModify ? (
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
          )}
        </div>
      </div>
      <div className="project">
        <ProjectList />
      </div>
    </div>
  );
};

export default ProjectBox;
