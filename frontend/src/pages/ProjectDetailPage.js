import { Outlet, useLocation, useParams } from "react-router-dom";
import { useEffect } from "react";
import { useQuery } from "react-query";

import ManagementHeader from "../components/common/ManagementHeader";

import { isToken } from "../utils/localstroageHandler";
import { getIsLeaderData } from "../lib/apis/managementApi";
import { getProjectData } from "../lib/apis/projectManagementApi";

import { useRouter } from "../hooks/useRouter";

const ProjectDetailPage = () => {
  const { id } = useParams();

  const { replaceTo, currentPath } = useRouter();

  const location = useLocation();

  const { data } = useQuery(
    ["managementProject", id],
    () => getProjectData(id),
    {
      keepPreviousData: true,
    }
  );

  const { data: isLeader } = useQuery(
    ["isProjectLeader", id],
    () => getIsLeaderData(id),
    {
      keepPreviousData: true,
    }
  );

  useEffect(() => {
    !isToken("access_token") && replaceTo("/");
  }, []);

  useEffect(() => {
    const pathRegex = new RegExp(`/management/${id}$`);

    if (data && currentPath.match(pathRegex)) {
      replaceTo(`/management/${id}/overview`);
    }
  }, [id, location.pathname, data, replaceTo, currentPath]);

  if (!data) {
    replaceTo(-1);
    return null;
  }

  return (
    <div className="management_project_layout">
      <ManagementHeader id={id} isLeader={isLeader} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
