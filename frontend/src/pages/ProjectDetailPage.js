import { Outlet, useLocation, useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";
import { useQuery } from "react-query";

import ManagementHeader from "../components/common/ManagementHeader";

import { isToken } from "../utils/localstroageHandler";
import { getIsLeaderData } from "../lib/apis/managementApi";
import { getProjectData } from "../lib/apis/projectManagementApi";

import { toast } from "react-toastify";

const ProjectDetailPage = () => {
  const { id } = useParams();

  const navigate = useNavigate();

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
    !isToken("access_token") && navigate("/", { replace: true });
  }, []);

  useEffect(() => {
    const pathRegex = new RegExp(`/management/${id}$`);

    if (data && location.pathname.match(pathRegex)) {
      navigate(`/management/${id}/overview`, { replace: true });
    }
  }, [id, navigate, location.pathname, data]);

  if (!data) {
    navigate(-1, { replace: true });
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
