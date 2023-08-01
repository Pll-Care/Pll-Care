import IntroduceHeader from "../components/Profile/IntroduceHeader";
import ProfileBody from "../components/Profile/ProfileBody";
import { ProfileProvider } from "../context/ProfileContext";
import { getMemberId } from "../lib/apis/profileApi";

const Profile = () => {
  getMemberId();

  return (
    <ProfileProvider>
      <div>
        <IntroduceHeader />
        <ProfileBody />
      </div>
    </ProfileProvider>
  );
};

export default Profile;
