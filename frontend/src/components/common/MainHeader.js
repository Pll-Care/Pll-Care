import { Link } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useRouter } from "../../hooks/useRouter";

import Button from "./Button";
import ToggleMenuButton from "./ToggleMenuButton";

import { authActions } from "../../redux/authSlice";
import { getProfileImage } from "../../lib/apis/mainHeaderApi";
import { isToken } from "../../utils/localstroageHandler";

import profile_default from "../../assets/profile-default-img.png";
import profile_isProfile from "../../assets/ranking-img.png";
import logo from "../../assets/logo-with-text.png";

export const headerMenu = [
  { id: 1, link: "/management", title: "프로젝트 관리" },
  { id: 2, link: "/recruitment", title: "인원 모집" },
];

const MainHeader = () => {
  const [isToggleMenuOpen, setIsToggleMenuOpen] = useState(false);
  const [isProfilePage, setIsProfilePage] = useState(false);
  const [profileImage, setProfileImage] = useState({ id: "", imageUrl: "" });

  const { replaceTo, currentPath, routeTo } = useRouter();
  const dispatch = useDispatch();
  const authState = useSelector((state) => state.auth.isLoggedIn);

  useEffect(() => {
    if (currentPath.includes("/profile")) setIsProfilePage(true);
    const getProfile = async () => {
      const response = await getProfileImage();
      if (response) {
        setProfileImage(response);
      }
    };
    getProfile();
    return () => {
      setIsProfilePage(false);
      setProfileImage({ id: "", imageUrl: "" });
    };
  }, [currentPath]);

  const handleClickLinkMenu = useCallback((link) => {
    if (link === "/recruitment") {
      routeTo(link);
    } else if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      routeTo(link);
    }
  }, []);

  const handleLogout = () => {
    dispatch(authActions.logout());
    replaceTo("/");
  };
  
  const handleLogin = () => {
    dispatch(authActions.setIsLoginModalVisible(true));

    window.addEventListener("message", (event) => {
      if (event.data === "login") {
        dispatch(authActions.login());
      }
    });
  };

  return (
    <>
      <header
        className={
          isProfilePage
            ? "main-header profile_header_background"
            : "main-header"
        }
      >
        <div className="main-header-left-col">
          <figure
            style={{
              backgroundImage: `url(${logo})`,
            }}
            className="main-header-logo-img"
            onClick={() => routeTo("/")}
          />
          <ToggleMenuButton
            isToggleMenuOpen={isToggleMenuOpen}
            setIsToggleMenuOpen={setIsToggleMenuOpen}
          />
        </div>
        <div className="main-header-medium-col">
          <ul className="main-header-link">
            {headerMenu.map((menu) => (
              <li
                className={
                  isProfilePage
                    ? "nav_item profile_header_menu_color"
                    : "nav_item"
                }
                key={menu.id}
              >
                <Link onClick={() => handleClickLinkMenu(menu.link)}>
                  {menu.title}
                </Link>
              </li>
            ))}
          </ul>
        </div>
        {authState ? (
          <div className="main-header-right-col main-header-logout-col">
            <Button
              text={"log out"}
              type={"positive"}
              onClick={handleLogout}
              isProfile={true}
            />
            <Link
              className={
                isProfilePage
                  ? "main-header-user-profile-img profile_header_image_background"
                  : "main-header-user-profile-img "
              }
              to={`/profile/${profileImage.id}/introduce`}
            >
              <img
                src={
                  profileImage.imageUrl === ""
                    ? isProfilePage
                      ? profile_isProfile
                      : profile_default
                    : profileImage.imageUrl
                }
                alt="유저프로필"
              />
            </Link>
          </div>
        ) : (
          <div className="main-header-right-col main-header-login-col">
            <Button text={"log in"} type={"positive"} onClick={handleLogin} />
          </div>
        )}
      </header>
    </>
  );
};

export default MainHeader;

//TODO: 로그인 시 나의 유저아이디를 발급 -> 발급 받은 아이디를 프로필로 이동 시 사용
