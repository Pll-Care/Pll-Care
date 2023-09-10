/* eslint-disable react-hooks/exhaustive-deps */
import { Outlet, useParams } from "react-router-dom";
import ManagementHeader from "../components/common/Header/ManagementHeader";
import { ProjectDetailProvider } from "../context/ProjectDetailContext";
import PageRouterLayout from "../layout/PageRouterLayout";
import { useEffect } from "react";
import { isToken } from "../utils/localstorageHandler";
import { useRouter } from "../hooks/useRouter";
import { useGeneralClient } from "../context/Client/GeneralClientContext";
import ManagementService from "../lib/service/ManagementService";
import ManagementClientProvider from "../context/Client/ManagementClientContext";

const ProjectDetailPage = () => {
  const { id: projectId } = useParams();
  const { routeOptionTo } = useRouter();
  const { generalHttpClient } = useGeneralClient();

  const managementService = new ManagementService(generalHttpClient);

  useEffect(() => {
    !isToken("access_token") && routeOptionTo("/", { replace: true });
  }, []);

  return (
    <ManagementClientProvider managementService={managementService}>
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
    </ManagementClientProvider>
  );
};

export default ProjectDetailPage;
