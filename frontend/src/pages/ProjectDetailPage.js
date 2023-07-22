import { Outlet, useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";
import { useQuery } from "react-query";

import ManagementHeader from "../components/common/ManagementHeader";
import { isToken } from "../utils/localstroageHandler";
import { getIsLeaderData } from "../lib/apis/managementApi";

const ProjectDetailPage = () => {
  const { id } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    !isToken("access_token") && navigate("/", { replace: true });
  }, []);

  const { data: isLeader } = useQuery(["isProjectLeader", id], () =>
    getIsLeaderData(id)
  );

  return (
    <div className="management_project_layout">
      <ManagementHeader id={id} isLeader={isLeader} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
