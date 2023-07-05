import { Outlet } from "react-router-dom";
import ProfileNavbar from "./ProfileNavbar";

const ProfileBody = () => {
  return (
    <div className="profile_body">
      <div className="profile_body_container">
        <ProfileNavbar />
        <section className="profile_body_sec">
          <Outlet />
        </section>
      </div>
    </div>
  );
};

export default ProfileBody;
