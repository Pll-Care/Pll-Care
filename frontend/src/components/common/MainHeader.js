import { Link } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import Login from "../Login/Login";
import Button from "./Button";
import ToggleMenuButton from "./ToggleMenuButton";

import { authActions } from "../../redux/authSlice";
import { useRouter } from "../../hooks/useRouter";
import profile_default from "../../assets/profile-default-img.png";
import profile_isProfile from "../../assets/ranking-img.png";

export const headerMenu = [
  { id: 1, link: "/management", title: "프로젝트 관리" },
  { id: 2, link: "/recruitment", title: "인원 모집" },
];

const MainHeader = () => {
  const [isLoginModalVisible, setIsLoginModalVisible] = useState(false);
  const [isToggleMenuOpen, setIsToggleMenuOpen] = useState(false);
  const [isProfilePage, setIsProfilePage] = useState(false);

  const { replaceTo, currentPath } = useRouter();
  const dispatch = useDispatch();
  const authState = useSelector((state) => state.auth.isLoggedIn);

  useEffect(() => {
    if (currentPath.includes("/profile")) setIsProfilePage(true);

    return () => {
      setIsProfilePage(false);
    };
  }, [currentPath]);

  const handleLogout = () => {
    dispatch(authActions.logout());
    setIsLoginModalVisible(false);
    replaceTo("/");
  };

  const handleLogin = () => {
    setIsLoginModalVisible(true);

    window.addEventListener("message", (event) => {
      if (event.data === "login") {
        dispatch(authActions.login());
        setIsLoginModalVisible(false);
      }
    });
  };

  const closeModal = useCallback(() => {
    setIsLoginModalVisible(false);
  }, []);

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
          <figure className="main-header-logo-img" />
          <ToggleMenuButton
            isToggleMenuOpen={isToggleMenuOpen}
            setIsToggleMenuOpen={setIsToggleMenuOpen}
          />
        </div>
        <div>
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
                <Link to={menu.link}>{menu.title}</Link>
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
              to={"/profile"}
            >
              <img
                src={isProfilePage ? profile_isProfile : profile_default}
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
      {isLoginModalVisible && (
        <Login
          isLoginModalVisible={isLoginModalVisible}
          closeModal={closeModal}
        />
      )}
    </>
  );
};

export default MainHeader;

//TODO: 로그인 시 나의 유저아이디를 발급 -> 발급 받은 아이디를 프로필로 이동 시 사용
