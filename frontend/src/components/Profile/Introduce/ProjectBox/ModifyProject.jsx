import { useCallback, useState } from "react";
import Button from "../../../common/Button";
import SearchStack from "../PositionBox/SearchStack";
import { filterSelectStack } from "../../../../utils/searchStack/handleStackList";
import { toast } from "react-toastify";
import ProfileInput from "../../../common/ProfileInput";
import Calendar from "../../../common/Calendar";
import { useProfileClient } from "../../../../context/Client/ProfileClientContext";

const ModifyProject = ({
  type = "",
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

  const { patchProfileAPI } = useProfileClient();

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

  const changeStartDate = useCallback((date) => {
    setSubmitData((prev) => ({
      ...prev,
      startDate: `${date.year}-${date.month}-${date.day}`,
    }));
  }, []);

  const changeEndDate = useCallback((date) => {
    setSubmitData((prev) => ({
      ...prev,
      endDate: `${date.year}-${date.month}-${date.day}`,
    }));
  }, []);

  const clickCompleteButton = async () => {
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

    const start = new Date(reqestData.projectExperiences[0].startDate);
    const end = new Date(reqestData.projectExperiences[0].endDate);

    if (Number(start) > Number(end)) {
      toast.error("시작 날짜와 종료 날짜를 다시 한번 확인해주세요. ");
      return;
    }

    const response = await patchProfileAPI(reqestData);
    if (response.status === 200) {
      toast.success(`${type}되었습니다.`);
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
              <ProfileInput
                value={submitData.title}
                onChange={(value) =>
                  setSubmitData((prev) => ({
                    ...prev,
                    title: value,
                  }))
                }
                placeholder="프로젝트 이름을 입력해주세요."
              />
            </div>
          </div>
          <div className="fl">
            <div className="project_item_name">
              <span>진행 기간</span>
            </div>
            <div className="project_list_item_year">
              <div className="project_list_item_year_position">
                <Calendar
                  date={startDate}
                  modifyDate={submitData.startDate}
                  onChangeDate={changeStartDate}
                />
              </div>
              <span>~</span>
              <div className="project_list_item_year_position">
                <Calendar
                  date={endDate}
                  modifyDate={submitData.endDate}
                  onChangeDate={changeEndDate}
                />
              </div>
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
            <ProfileInput
              value={submitData.description}
              onChange={(value) =>
                setSubmitData((prev) => ({
                  ...prev,
                  description: value,
                }))
              }
              placeholder="프로젝트롤 소개해주세요."
            />
          </div>
        </div>
      </div>
      <div className="project_list_button">
        <Button
          buttonType="submit"
          text="완료"
          size="small"
          onClick={clickCompleteButton}
        />
      </div>
    </>
  );
};

export default ModifyProject;
