import { useParams } from "react-router";
import IntroduceHeader from "../components/Profile/IntroduceHeader";
import ProfileBody from "../components/Profile/ProfileBody";
import { ProfileProvider } from "../context/ProfileContext";
import PageRouterLayout from "../layout/PageRouterLayout";
import HttpClient from "../lib/service/HttpClient";
import ProfileService from "../lib/service/ProfileService";
import ProfileClientProvider from "../context/Client/ProfileClientContext";
import LocalTokenRepository from "../lib/repository/LocalTokenRepository";
import { baseURL } from "../utils/auth";

const Profile = () => {
  const { id: memberId } = useParams();

  const localTokenRepository = new LocalTokenRepository();
  const httpClient = new HttpClient(baseURL, localTokenRepository);
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
