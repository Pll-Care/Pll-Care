import { useParams } from "react-router";
import IntroduceHeader from "../components/Profile/IntroduceHeader";
import ProfileBody from "../components/Profile/ProfileBody";
import { ProfileProvider } from "../context/ProfileContext";
import PageRouterLayout from "../layout/PageRouterLayout";

const Profile = () => {
  const { id: memberId } = useParams();

  return (
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
  );
};

export default Profile;
