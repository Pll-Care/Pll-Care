import { NavLink } from "react-router-dom";

import INTRODUCE from "../../assets/profile-default-img.png";
import EVALUATAE from "../../assets/layers-img.png";
import MY_PROJECT from "../../assets/star-img.png";
import HEART from "../../assets/heart.png";
import { useProfile } from "../../context/ProfileContext";

const ProfileNavbar = () => {
  const { isMyProfile, memberId } = useProfile();

  return (
    <nav className="profile_nav">
      {navData
        .filter((element) => {
          return isMyProfile ? !!element : !element.isAdmin;
        })
        .map((element) => (
          <div key={element.title} className="profile_nav_box">
            <NavLink
              to={`/profile/${memberId}/${element.to}`}
              className={({ isActive }) =>
                isActive
                  ? "profile_nav_link profile_nav_image_active"
                  : "profile_nav_link"
              }
            >
              {element.child}
            </NavLink>
          </div>
        ))}
    </nav>
  );
};

export default ProfileNavbar;

const navData = [
  {
    title: "개인정보",
    to: "introduce",
    child: <img src={INTRODUCE} alt="introduce" />,
    isAdmin: false,
  },
  {
    title: "평가",
    to: "evaluate",
    child: <img src={EVALUATAE} alt="evaluate" />,
    isAdmin: false,
  },
  {
    title: "내 프로젝트",
    to: "my",
    child: <img src={MY_PROJECT} alt="my_project" />,
    isAdmin: true,
  },
  {
    title: "좋아하는 프로젝트",
    to: "like",
    child: <img src={HEART} alt="like_project" />,
    isAdmin: true,
  },
];
