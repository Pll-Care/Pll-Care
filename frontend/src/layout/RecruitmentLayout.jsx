import { useGeneralClient } from "../context/Client/GeneralClientContext";
import RecruitmentClientProvider from "../context/Client/RecruitmentClientContext";
import RecruitmentService from "../lib/service/RecruitmentService";

const RecruitmentLayout = ({ children }) => {
  const { generalHttpClient } = useGeneralClient();

  const recruitmentService = new RecruitmentService(generalHttpClient);
  return (
    <RecruitmentClientProvider recruitmentService={recruitmentService}>
      {children}
    </RecruitmentClientProvider>
  );
};
export default RecruitmentLayout;
