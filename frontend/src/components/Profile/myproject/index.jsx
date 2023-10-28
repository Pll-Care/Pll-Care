import { useProfile } from "../../../context/ProfileContext";
import RecruitmentProject from "./RecruitmentProject";
import ApplyProject from "./ApplyProject";

const MyProject = () => {
  const { memberId } = useProfile();

  return (
    <div className="project_container">
      <RecruitmentProject memberId={memberId} />
      <ApplyProject memberId={memberId} />
    </div>
  );
};

export default MyProject;
