import { useParams } from "react-router";
import IntroduceHeader from "../components/Profile/IntroduceHeader";
import ProfileBody from "../components/Profile/ProfileBody";
import { ProfileProvider } from "../context/ProfileContext";
import PageRouterLayout from "../layout/PageRouterLayout";
import ProfileService from "../lib/service/ProfileService";
import ProfileClientProvider from "../context/Client/ProfileClientContext";
import { useGeneralClient } from "../context/Client/GeneralClientContext";

const Profile = () => {
  const { id: memberId } = useParams();
  const { generalHttpClient } = useGeneralClient();

  const profileService = new ProfileService(generalHttpClient, memberId);

  return (
    <ProfileClientProvider profileService={profileService}>
      <ProfileProvider>
        <PageRouterLayout
          pageUrl={`/profile/${memberId}/introduce`}
          passUrl={`/profile/${memberId}`}
        >
          <div>
            <IntroduceHeader />
            <ProfileBody />
          </div>
        </PageRouterLayout>
      </ProfileProvider>
    </ProfileClientProvider>
  );
};

export default Profile;
