import { NavLink } from "react-router-dom";

import INTRODUCE from "../../assets/profile-default-img.png";
import EVALUATAE from "../../assets/layers-img.png";
import MY_PROJECT from "../../assets/star-img.png";

const navData = [
  {
    title: "개인정보",
    to: "/profile/10/introduce",
    child: <img src={INTRODUCE} alt="introduce" />,
  },
  {
    title: "평가",
    to: "/profile/10/evaluate",
    child: <img src={EVALUATAE} alt="evaluate" />,
  },
  {
    title: "내 프로젝트",
    to: "/profile/10/my",
    child: <img src={MY_PROJECT} alt="my_project" />,
  },
  {
    title: "좋아하는 프로젝트",
    to: "/profile/10/like",
    child: <img src={MY_PROJECT} alt="like_project" />,
  },
];

const ProfileNavbar = () => {
  return (
    <nav className="profile_nav">
      {navData.map((item) => (
        <NavLink
          key={item.title}
          to={item.to}
          className={({ isActive }) =>
            isActive
              ? "profile_nav_link profile_nav_image_active"
              : "profile_nav_link"
          }
        >
          {item.child}
        </NavLink>
      ))}
    </nav>
  );
};

export default ProfileNavbar;
