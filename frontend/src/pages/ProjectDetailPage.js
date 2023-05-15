import { Outlet, useParams } from "react-router-dom";
import MainHeader from "../components/MainHeader";
import ManagementHeader from "../components/ManagementHeader";

const ProjectDetailPage = () => {
  const { id } = useParams(); 

  return (
    <div>
      <MainHeader />
      <ManagementHeader id={id} />
      <Outlet />
    </div>
  );
};

export default ProjectDetailPage;
