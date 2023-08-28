/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useRouter } from "../../hooks/useRouter";
import Button from "./Button";
import ToggleMenuButton from "./ToggleMenuButton";
import { authActions } from "../../redux/authSlice";
import profile_default from "../../assets/profile-default-img.png";
import profile_isProfile from "../../assets/ranking-img.png";
import logo from "../../assets/logo-with-text.svg";
import profileLogo from "../../assets/logo-with-text-profile.png";
import { useGeneralClient } from "../../context/Client/GeneralClientContext";
import { userInfoActions } from "../../redux/userInfoSlice";

import useLinkMenuClick from "../../hooks/useLinkMenuClick";

const headerMenu = [
  { id: 1, link: "/management", title: "프로젝트 관리" },
  { id: 2, link: "/recruitment", title: "인원 모집" },
];

const MainHeader = () => {
  const [isToggleMenuOpen, setIsToggleMenuOpen] = useState(false);
  const [isProfilePage, setIsProfilePage] = useState(false);

  const { replaceTo, currentPath, routeTo } = useRouter();
  const dispatch = useDispatch();
  const authState = useSelector((state) => state.auth.isLoggedIn);
  const userInfo = useSelector((state) => state.userInfo);

  const { getProfileAPI } = useGeneralClient();

  useEffect(() => {
    if (currentPath.includes("/profile")) {
      setIsProfilePage(true);
    } else {
      setIsProfilePage(false);
    }
  }, [currentPath]);

  useEffect(() => {
    const getUser = async () => {
      try {
        const response = await getProfileAPI();
        if (response.status === 200) {
          const payload = {
            memberId: response.data.memberId,
            imageUrl: response.data.imageUrl,
          };
          dispatch(userInfoActions.setUserInfo(payload));
        }
      } catch (error) {
        console.log(error);
      }
    };

    if (authState) getUser();
  }, [authState]);

  const handleLogout = () => {
    dispatch(authActions.logout());
    replaceTo("/");
  };

  const handleLogin = () => {
    dispatch(authActions.setIsLoginModalVisible(true));
  };

  const handleClickLinkMenu = useLinkMenuClick();

  return (
    <header className={isProfilePage ? "header header-profile-bg" : "header"}>
      <div
        className={
          isProfilePage ? "main-header header-profile-bg" : "main-header"
        }
      >
        <div className="main-header-left-col">
          <figure
            style={{
              backgroundImage: `url(${isProfilePage ? profileLogo : logo})`,
            }}
            className="main-header-logo-img"
            onClick={() => routeTo("/")}
          />
        </div>
        <div className="main-header-medium-col">
          <ul className="main-header-link">
            {headerMenu.map((menu) => (
              <li key={menu.id}>
                <p
                  className={
                    isProfilePage
                      ? "nav_item  header-profile-menu"
                      : currentPath.includes(menu.link)
                      ? "nav_item main-header-link-on"
                      : "nav_item main-header-link-off"
                  }
                  onClick={() => handleClickLinkMenu(menu.link)}
                >
                  {menu.title}
                </p>
              </li>
            ))}
          </ul>
        </div>
        {authState ? (
          <div className="main-header-right-col main-header-logout-col">
            {isProfilePage ? (
              <Button
                text={"Log out"}
                type={"positive"}
                color={"white"}
                onClick={handleLogout}
                isProfile={true}
              />
            ) : (
              <Button
                text={"Log out"}
                type={"positive"}
                color={"white"}
                onClick={handleLogout}
              />
            )}
            <div
              className={
                isProfilePage
                  ? "main-header-user-profile-img header-profile-image-bg"
                  : "main-header-user-profile-img header-image-bg"
              }
              onClick={() => routeTo(`/profile/${userInfo.memberId}`)}
            >
              <img
                className="main-header_img"
                src={
                  !userInfo.imageUrl
                    ? isProfilePage
                      ? profile_isProfile
                      : profile_default
                    : userInfo.imageUrl
                }
                alt={"유저 프로필"}
              />
            </div>
            <ToggleMenuButton
              isProfilePage={isProfilePage}
              isToggleMenuOpen={isToggleMenuOpen}
              setIsToggleMenuOpen={setIsToggleMenuOpen}
            />
          </div>
        ) : (
          <div className="main-header-right-col main-header-login-col">
            <Button
              text={"Log in"}
              color={"white"}
              type={"positive"}
              onClick={handleLogin}
            />
            <ToggleMenuButton
              isToggleMenuOpen={isToggleMenuOpen}
              setIsToggleMenuOpen={setIsToggleMenuOpen}
            />
          </div>
        )}
      </div>
    </header>
  );
};

export default MainHeader;