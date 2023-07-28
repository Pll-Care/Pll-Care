import { useLocation } from "react-router-dom";
import BadgeBox from "./BadgeBox";
import FinalEvaluationBox from "./FinalEvaluationBox";
import { useQuery } from "react-query";
import { getEvaluationProjectDetailAPI } from "../../../lib/apis/profileApi";
import { useProfile } from "../../../context/ProfileContext";

const QUERY_KEY = "evaluate-project-detail";

const ProjectEvaluate = () => {
  const { state } = useLocation();
  const { projectId, title } = state;
  const { memberId } = useProfile();

  const { data } = useQuery([QUERY_KEY, memberId, projectId], () =>
    getEvaluationProjectDetailAPI(memberId, projectId)
  );

  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>{title}</h1>
      </div>
      <div className="projectEvaluate">
        <BadgeBox badgeDtos={data?.badgeDtos} />
        <FinalEvaluationBox finalEvals={data?.finalEvals} />
      </div>
    </div>
  );
};

export default ProjectEvaluate;
