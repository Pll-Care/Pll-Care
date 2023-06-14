import { Outlet, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { useEffect } from "react";

import ManagementHeader from "../components/shared/ManagementHeader";

const ProjectDetailPage = () => {
  const { id } = useParams(); 

  const authState = useSelector(state => state.auth.isLoggedIn);

  const navigate = useNavigate();

  useEffect(() => {
    !authState && navigate('/', { replace: true });
  }, []);

  return (
    <div>
      <ManagementHeader id={id} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
