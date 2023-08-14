import { useEffect, useState } from "react";
import Button from "../../../common/Button";
import ProjectList from "./ProjectList";
import ModifyProject from "./ModifyProject";
import { useProfile } from "../../../../context/ProfileContext";
import { useQuery } from "react-query";
import { useProfileClient } from "../../../../context/Client/ProfileClientContext";

const QUERY_KEY = "experience-project";

const ProjectBox = () => {
  const [newProject, setNewProject] = useState(false);

  const { memberId, isMyProfile } = useProfile();
  const { getProjectExperienceAPI } = useProfileClient();

  const { data: experienceData, refetch } = useQuery(
    [memberId, QUERY_KEY],
    () => getProjectExperienceAPI()
  );

  useEffect(() => {
    refetch();
  }, [newProject, refetch]);

  const editNewProject = (isEdit) => {
    setNewProject(isEdit);
  };

  return (
    <div className="profile_body_introduce_Box">
      <div className="profile_body_introduce_Box_title">
        <h2>프로젝트 경험</h2>
        {isMyProfile && (
          <Button
            text="프로젝트 추가"
            type="positive"
            color="white"
            size="small"
            onClick={() => setNewProject((prev) => !prev)}
          />
        )}
      </div>
      {newProject && (
        <div className="project_list_item">
          <ModifyProject changeModify={editNewProject} type="추가" />
        </div>
      )}
      <div className="project">
        <ProjectList
          experienceData={experienceData?.data.data}
          refetch={refetch}
        />
      </div>
    </div>
  );
};

export default ProjectBox;
