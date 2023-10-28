import MemberRecruitmentWrite from "../components/RecruitmentWrite";
import { useGeneralClient } from "../context/Client/GeneralClientContext";
import ProfileClientProvider from "../context/Client/ProfileClientContext";
import { ProfileProvider } from "../context/ProfileContext";
import RecruitmentLayout from "../layout/RecruitmentLayout";
import ProfileService from "../lib/service/ProfileService";

const RecruitmentWritePage = () => {
  const { generalHttpClient } = useGeneralClient();

  const profileService = new ProfileService(generalHttpClient);

  return (
    <ProfileClientProvider profileService={profileService}>
      <ProfileProvider>
        <RecruitmentLayout>
          <MemberRecruitmentWrite />
        </RecruitmentLayout>
      </ProfileProvider>
    </ProfileClientProvider>
  );
};
export default RecruitmentWritePage;
