import { createContext, useContext } from "react";

const HomeContext = createContext(null);
export const useHomeClient = () => useContext(HomeContext);

const HomeClientProvider = ({ homeService, children }) => {
  const getPopularProjects = homeService.getPopularProjects.bind(homeService);
  const getImminentProjects = homeService.getImminentProjects.bind(homeService);
  const getUpToDateProjects = homeService.getUpToDateProjects.bind(homeService);

  return (
    <HomeContext.Provider
      value={{
        getPopularProjects,
        getImminentProjects,
        getUpToDateProjects,
      }}
    >
      {children}
    </HomeContext.Provider>
  );
};

export default HomeClientProvider;
