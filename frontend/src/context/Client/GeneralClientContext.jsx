import { createContext, useContext } from "react";

const GeneralClientContext = createContext(null);
export const useGeneralClient = () => useContext(GeneralClientContext);

const GeneralClientProvider = ({ children, generalService, httpClient }) => {
  const generalHttpClient = httpClient;
  const getProfileAPI = generalService.getProfileAPI.bind(generalService);
  return (
    <GeneralClientContext.Provider value={{ getProfileAPI, generalHttpClient }}>
      {children}
    </GeneralClientContext.Provider>
  );
};

export default GeneralClientProvider;
