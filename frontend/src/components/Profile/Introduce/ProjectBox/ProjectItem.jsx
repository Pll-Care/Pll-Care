import { useEffect, useState } from "react";
import Button from "../../../common/Button";
import StackItem from "../../../common/StackItem";
import ModifyProject from "./ModifyProject";
import { useProfile } from "../../../../context/ProfileContext";
import { useProfileClient } from "../../../../context/Client/ProfileClientContext";

const ProjectItem = ({
  title,
  description,
  startDate,
  endDate,
  techStack,
  projectId,
  refetch,
}) => {
  const [isModify, setIsmodify] = useState(false);
  const { patchProfileAPI } = useProfileClient();

  const changeModify = (modify) => {
    setIsmodify(modify);
  };

  const deleProject = async () => {
    const reqBody = {
      projectId: projectId,
      delete: true,
    };
    const response = await patchProfileAPI(reqBody);
    if (response.status === 200) refetch();
  };

  useEffect(() => {
    refetch();
  }, [isModify, refetch]);

  return (
    <div className="project_list_item">
      {isModify ? (
        <ModifyProject
          type="수정"
          title={title}
          description={description}
          startDate={startDate}
          endDate={endDate}
          techStack={techStack}
          projectId={projectId}
          changeModify={changeModify}
        />
      ) : (
        <ShowProjectItem
          title={title}
          description={description}
          startDate={startDate}
          endDate={endDate}
          techStack={techStack}
          projectId={projectId}
          changeModify={changeModify}
          deleProject={deleProject}
        />
      )}
    </div>
  );
};

export default ProjectItem;

const ShowProjectItem = ({
  title,
  description,
  startDate,
  endDate,
  techStack,
  projectId,
  changeModify,
  deleProject,
}) => {
  const { isMyProfile } = useProfile();

  const start = startDate.replaceAll("-", ".");
  const end = endDate.replaceAll("-", ".");
  return (
    <>
      <div className="project_list_item_box">
        <div className="project_list_item_title-date">
          <div className="fl">
            <div className="project_item_name">
              <span>프로젝트 명</span>
            </div>
            <div className="project_list_item_text">
              <span>{title}</span>
            </div>
          </div>
          <div className="fl">
            <div className="project_item_name">
              <span>진행 기간</span>
            </div>
            <div className="project_list_item_year project_list_item_text">
              <span>{`${start} ~ ${end}`}</span>
            </div>
          </div>
        </div>
        <div className="project_list_item_stack">
          <div className="project_item_name">
            <span>기술 스택</span>
          </div>
          <div className="project_list_item_stack-list">
            <ul>
              {techStack?.map((stack) => (
                <StackItem
                  key={`${projectId}-${stack.name}`}
                  imageUrl={stack.imageUrl}
                  name={stack.name}
                />
              ))}
            </ul>
          </div>
        </div>
        <div className="project_list_item_description">
          <div className="project_item_name">
            <span>한 줄 소개</span>
          </div>
          <div className="project_list_item_text">
            <span>{description}</span>
          </div>
        </div>
      </div>
      <div className="project_list_button">
        {isMyProfile && (
          <>
            <Button
              text="수정"
              size="small"
              onClick={() => changeModify(true)}
            />
            <Button text="삭제" size="small" onClick={() => deleProject()} />
          </>
        )}
      </div>
    </>
  );
};
