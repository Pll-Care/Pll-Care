import { createContext, useContext } from "react";

const ManagementContext = createContext(null);
export const useManagementClient = () => useContext(ManagementContext);

const ManagementClientProvider = ({ children, managementService }) => {
  const getProjectList =
    managementService.getProjectList.bind(managementService);

  return (
    <ManagementContext.Provider
      value={{
        getProjectList,
      }}
    >
      {children}
    </ManagementContext.Provider>
  );
};

export default ManagementClientProvider;
