import { useLocation } from "react-router-dom";
import BadgeBox from "./BadgeBox";
import FinalEvaluationBox from "./FinalEvaluationBox";
import { useQuery } from "react-query";
import { useProfile } from "../../../context/ProfileContext";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";

const QUERY_KEY = "evaluate-project-detail";

const ProjectEvaluate = () => {
  const { state } = useLocation();
  const { projectId, title } = state;
  const { memberId } = useProfile();
  const { getEvaluationProjectDetailAPI } = useProfileClient();

  const { data } = useQuery([memberId, QUERY_KEY, projectId], () =>
    getEvaluationProjectDetailAPI(projectId)
  );

  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>{title}</h1>
      </div>
      <div className="projectEvaluate">
        <BadgeBox badgeDtos={data?.data?.badgeDtos} />
        <FinalEvaluationBox finalEvals={data?.data?.finalEvals} />
      </div>
    </div>
  );
};

export default ProjectEvaluate;
