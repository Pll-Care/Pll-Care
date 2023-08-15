import { useParams } from "react-router";
import IntroduceHeader from "../components/Profile/IntroduceHeader";
import ProfileBody from "../components/Profile/ProfileBody";
import { ProfileProvider } from "../context/ProfileContext";
import PageRouterLayout from "../layout/PageRouterLayout";
import HttpClient from "../lib/service/HttpClient";
import ProfileService from "../lib/service/ProfileService";
import ProfileClientProvider from "../context/Client/ProfileClientContext";

const Profile = () => {
  const { id: memberId } = useParams();

  const httpClient = new HttpClient();
  const profileService = new ProfileService(httpClient, memberId);

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
