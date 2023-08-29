/* eslint-disable react-hooks/exhaustive-deps */
import { createContext, useContext, useEffect, useState } from "react";
import { useParams } from "react-router";
import { getIsLeaderData } from "../lib/apis/managementApi";
import { useRouter } from "../hooks/useRouter";
import { toast } from "react-toastify";

const ProjectDetailContext = createContext(null);
export const useProjectDetail = () => useContext(ProjectDetailContext);

export function ProjectDetailProvider({ children }) {
  const [isLeader, setIsLeader] = useState(false);
  const { id: projectId } = useParams();

  useEffect(() => {
    const getIsLeader = async () => {
      try {
        const isLeaderData = await getIsLeaderData(projectId);
        setIsLeader((_) => isLeaderData);
      } catch (error) {
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
