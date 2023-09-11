import { useGeneralClient } from "../context/Client/GeneralClientContext";
import HomeService from "../lib/service/HomeService";
import HomeClientProvider from "../context/Client/HomeClientContext";

const HomeLayout = ({ children }) => {
  const { generalHttpClient } = useGeneralClient();

  const homeService = new HomeService(generalHttpClient);
  return (
    <HomeClientProvider homeService={homeService}>
      {children}
    </HomeClientProvider>
  );
};

export default HomeLayout;
