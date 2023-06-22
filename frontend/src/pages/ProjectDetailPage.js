import { Outlet, useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";

import ManagementHeader from "../components/common/ManagementHeader";
import { isToken } from "../utils/localstroageHandler";

const ProjectDetailPage = () => {
  const { id } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    !isToken("access_token") && navigate("/", { replace: true });
  }, []);

  return (
    <div>
      <ManagementHeader id={id} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
