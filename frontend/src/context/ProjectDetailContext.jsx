/* eslint-disable react-hooks/exhaustive-deps */
import { createContext, useContext, useEffect, useState } from "react";
import { useParams } from "react-router";
import { useRouter } from "../hooks/useRouter";
import { getIsLeaderData } from "../lib/apis/managementApi";
import { toast } from "react-toastify";

const ProjectDetailContext = createContext(null);
export const useProjectDetail = () => useContext(ProjectDetailContext);

export function ProjectDetailProvider({ children }) {
  const [isLeader, setIsLeader] = useState(false);
  const { id: projectId } = useParams();
  const { routeOptionTo } = useRouter();

  useEffect(() => {
    const getIsLeader = async () => {
      try {
        const response = await getIsLeaderData(projectId);
        if (response.status === 200) setIsLeader((_) => response.data.leader);
      } catch (error) {
        if (error.response.data.status === 404) {
          toast.error(error.response.data.message);
          routeOptionTo("/management", { replace: true });
        }
        console.log(error);
      }
    };

    getIsLeader();

    return () => {
      setIsLeader((_) => false);
    };
  }, [projectId]);

  return (
    <ProjectDetailContext.Provider value={{ isLeader, projectId }}>
      {children}
    </ProjectDetailContext.Provider>
  );
}
