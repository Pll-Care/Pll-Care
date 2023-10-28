import { useGeneralClient } from "../context/Client/GeneralClientContext";
import ManagementClientProvider from "../context/Client/ManagementClientContext";
import ManagementService from "../lib/service/ManagementService";

const ManagementLayout = ({ children }) => {
  const { generalHttpClient } = useGeneralClient();

  const managementService = new ManagementService(generalHttpClient);

  return (
    <ManagementClientProvider managementService={managementService}>
      {children}
    </ManagementClientProvider>
  );
};

export default ManagementLayout;
