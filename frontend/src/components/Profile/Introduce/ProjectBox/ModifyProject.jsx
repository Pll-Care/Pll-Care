import { useState } from "react";
import Button from "../../../common/Button";
import SearchStack from "../PositionBox/SearchStack";
import { filterSelectStack } from "../../../../utils/searchStack/handleStackList";
import { patchProfile } from "../../../../lib/apis/profileApi";
import { useProfile } from "../../../../context/ProfileContext";
import { toast } from "react-toastify";

const ModifyProject = ({
  title = "",
  description = "",
  startDate = "",
  endDate = "",
  techStack = [],
  changeModify,
  projectId,
}) => {
  const [submitData, setSubmitData] = useState({
    title,
    description,
    startDate,
    techStack,
    endDate,
  });

  const { memberId } = useProfile();

  const changeStack = (stack) => {
    setSubmitData((prev) => ({
      ...prev,
      techStack: [...prev.techStack, stack],
    }));
  };

  const deleteStack = (stack) => {
    const result = filterSelectStack(submitData.techStack, stack);
    setSubmitData((prev) => ({
      ...prev,
      techStack: [...result],
    }));
  };
  const clickButton = async () => {
    const submitStack = submitData.techStack.map((stack) => stack.name);
    const reqestData = projectId
      ? {
          projectId,
          projectExperiences: [{ ...submitData, techStack: submitStack }],
          delete: false,
        }
      : {
          projectExperiences: [{ ...submitData, techStack: submitStack }],
          delete: false,
        };

    if (!reqestData.projectExperiences[0].title) {
      toast.error("프로젝트 이름을 입력해주세요.");
      return;
    }
    if (!reqestData.projectExperiences[0].startDate) {
      toast.error("프로젝트 시작 날짜를 입력해주세요.");
      return;
    }
    if (!reqestData.projectExperiences[0].endDate) {
      toast.error("프로젝트 종료 날짜를 입력해주세요.");
      return;
    }
    if (reqestData.projectExperiences[0].techStack.length === 0) {
      toast.error("기술 스택을 입력해주세요.");
      return;
    }
    if (!reqestData.projectExperiences[0].description) {
      toast.error("프로젝트 한 줄 소개를 입력해주세요.");
      return;
    }

    const response = await patchProfile(memberId, reqestData);
    if (response.status === 200) {
      toast.success("수정되었습니다.");
      changeModify(false);
    }
  };

  return (
    <>
      <div className="project_list_item_box">
        <div className="project_list_item_title-date">
          <div className="fl">
            <div className="project_item_name">
              <span>프로젝트 명</span>
            </div>
            <div className="w-f">
              <input
                className="project_list_item_input w-f"
                defaultValue={title}
                placeholder="프로젝트 이름을 적어주세요"
                onChange={(event) =>
                  setSubmitData((prev) => ({
                    ...prev,
                    title: event.target.value,
                  }))
                }
              />
            </div>
          </div>
          <div className="fl">
            <div className="project_item_name">
              <span>진행 기간</span>
            </div>
            <div className="project_list_item_year">
              <input
                type="date"
                className="project_list_item_input"
                defaultValue={startDate}
                onChange={(event) =>
                  setSubmitData((prev) => ({
                    ...prev,
                    startDate: event.target.value,
                  }))
                }
              />
              <span>~</span>
              <input
                type="date"
                className="project_list_item_input"
                defaultValue={endDate}
                onChange={(event) =>
                  setSubmitData((prev) => ({
                    ...prev,
                    endDate: event.target.value,
                  }))
                }
              />
            </div>
          </div>
        </div>
        <div className="project_list_item_stack">
          <div className="project_item_name">
            <span>기술 스택</span>
          </div>
          <SearchStack
            stackList={techStack}
            changeStack={changeStack}
            deleteStack={deleteStack}
            type="three"
          />
        </div>
        <div className="project_list_item_description">
          <div className="project_item_name">
            <span>한 줄 소개</span>
          </div>
          <div className="w-f">
            <input
              type="text"
              placeholder="프로젝트를 소개해주세요."
              className="project_list_item_input"
              defaultValue={description}
              onChange={(event) =>
                setSubmitData((prev) => ({
                  ...prev,
                  description: event.target.value,
                }))
              }
            />
          </div>
        </div>
      </div>
      <div className="project_list_button">
        <Button
          buttonType="submit"
          text="완료"
          size="small"
          onClick={clickButton}
        />
      </div>
    </>
  );
};

export default ModifyProject;
