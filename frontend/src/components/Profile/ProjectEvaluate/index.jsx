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
      <div>
        <h1 className="projectEvaluate_name">{title}</h1>
      </div>
      <div className="projectEvaluate">
        <BadgeBox badges={data?.data?.badges} />
        <FinalEvaluationBox finalEvals={data?.data?.finalEvals} />
      </div>
    </div>
  );
};

export default ProjectEvaluate;
