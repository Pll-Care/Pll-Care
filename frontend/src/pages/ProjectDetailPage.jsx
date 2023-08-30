/* eslint-disable react-hooks/exhaustive-deps */
import { Outlet, useParams } from "react-router-dom";
import ManagementHeader from "../components/common/Header/ManagementHeader";
import { ProjectDetailProvider } from "../context/ProjectDetailContext";
import PageRouterLayout from "../layout/PageRouterLayout";
import { useEffect } from "react";
import { isToken } from "../utils/localstorageHandler";
import { useRouter } from "../hooks/useRouter";

const ProjectDetailPage = () => {
  const { id: projectId } = useParams();
  const { routeOptionTo } = useRouter();

  useEffect(() => {
    !isToken("access_token") && routeOptionTo("/", { replace: true });
  }, []);

  return (
    <ProjectDetailProvider>
      <PageRouterLayout
        passUrl={`/management/${projectId}`}
        pageUrl={`/management/${projectId}/overview`}
      >
        <div className="management_project_layout">
          <ManagementHeader />
          <Outlet />
        </div>
      </PageRouterLayout>
    </ProjectDetailProvider>
  );
};

export default ProjectDetailPage;
