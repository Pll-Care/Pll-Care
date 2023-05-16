import { Outlet, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { useEffect } from "react";

import MainHeader from "../components/MainHeader";
import ManagementHeader from "../components/ManagementHeader";

const ProjectDetailPage = () => {
  const { id } = useParams(); 

  const authState = useSelector(state => state.auth.isLoggedIn);

  const navigate = useNavigate();

  useEffect(() => {
    !authState && navigate('/', { replace: true });
  }, []);

  return (
    <div>
      <MainHeader />
      <ManagementHeader id={id} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
