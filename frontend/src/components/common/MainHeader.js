import { Link } from "react-router-dom";
import { useCallback, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import Login from "../Login/Login";
import Button from "./Button";
import ToggleMenuButton from "./ToggleMenuButton";

import { authActions } from "../../redux/authSlice";
import { useRouter } from "../../hooks/useRouter";

export const headerMenu = [
  { id: 1, link: "/management", title: "프로젝트 관리" },
  { id: 2, link: "/recruitment", title: "인원 모집" },
];

const MainHeader = () => {
  const [isLoginModalVisible, setIsLoginModalVisible] = useState(false);
  const [isToggleMenuOpen, setIsToggleMenuOpen] = useState(false);

  const { routeTo } = useRouter();
  const dispatch = useDispatch();
  const authState = useSelector((state) => state.auth.isLoggedIn);

  const handleLogout = () => {
    dispatch(authActions.logout());
    setIsLoginModalVisible(false);
    routeTo("/");
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
      <header className="main-header">
        <div className="main-header-left-col">
          <figure className="main-header-logo-img" onClick={() => routeTo("/")} />
          <ToggleMenuButton
            isToggleMenuOpen={isToggleMenuOpen}
            setIsToggleMenuOpen={setIsToggleMenuOpen}
          />
        </div>
        <div className="main-header-medium-col">
          <ul className="main-header-link">
            {headerMenu.map((menu) => (
              <li
                className={menu.id === 1 ? "nav_item" : "nav_item nav_divide"}
                key={menu.id}
              >
                {authState ? (
                  <Link to={menu.link}>{menu.title}</Link>
                ) : (
                  <Link onClick={handleLogin}>{menu.title}</Link>
                )}
              </li>
            ))}
          </ul>
        </div>
        {authState ? (
          <div className="main-header-right-col main-header-logout-col">
            <Button text={"log out"} type={"positive"} onClick={handleLogout} />
            <Link className="main-header-user-profile-img" to={"/profile"} />
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
