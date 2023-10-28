import { createContext, useContext } from "react";

const GeneralClientContext = createContext(null);
export const useGeneralClient = () => useContext(GeneralClientContext);

const GeneralClientProvider = ({ children, generalService, httpClient }) => {
  const generalHttpClient = httpClient;
  const getProfileAPI = generalService.getProfileAPI.bind(generalService);
  const deleteImage = generalService.deleteImage.bind(generalService);
  const uploadImage = generalService.uploadImage.bind(generalService);

  return (
    <GeneralClientContext.Provider
      value={{ getProfileAPI, generalHttpClient, uploadImage, deleteImage }}
    >
      {children}
    </GeneralClientContext.Provider>
  );
};

export default GeneralClientProvider;
