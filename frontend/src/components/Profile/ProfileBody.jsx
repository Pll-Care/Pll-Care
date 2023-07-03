import { Link, Outlet } from "react-router-dom";

const ProfileBody = () => {
  return (
    <div className="profile_body">
      <div className="profile_body_container">
        <aside>
          <Link to={`/profile/10/introduce`}>introduce</Link>
          <Link to={`/profile/10/evaluate`}>evaluate</Link>
          <Link to={`/profile/10/my`}>my</Link>
          <Link to={`/profile/10/like`}>like</Link>
        </aside>
        <section>
          <Outlet />
        </section>
      </div>
    </div>
  );
};

export default ProfileBody;
