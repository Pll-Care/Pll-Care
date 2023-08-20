import { useRef } from "react";

import logoImgUrl from "../../assets/logo.svg";
import managementIconImgUrl from "../../assets/management-icon.svg";
import recruitmentIconImgUrl from "../../assets/recruitment-icon.svg";
import useLinkMenuClick from "../../hooks/useLinkMenuClick";

const ToggleMenuButton = ({
  isToggleMenuOpen,
  setIsToggleMenuOpen,
  isProfilePage,
}) => {
  const modalOutside = useRef(null);

  const handleToggleMenuButtonClick = (isBoolean) => {
    setIsToggleMenuOpen((_) => isBoolean);
  };

  const handleToggleButtonClose = (e) => {
    if (e.target === modalOutside.current) {
      setIsToggleMenuOpen(false);
    }
  };

  const handleClickLinkMenu = useLinkMenuClick();

  const handleClickLink = (menu) => {
    handleClickLinkMenu(menu);
    setIsToggleMenuOpen(false);
  };

  return (
    <div
      className={isToggleMenuOpen ? "toggle-menu-wrapper" : ""}
      ref={modalOutside}
      onClick={handleToggleButtonClose}
    >
      {isToggleMenuOpen ? (
        <div className="toggle-menu">
          <div className="toggle-menu-button-wrapper">
            <div className="toggle-menu-button-logo">
              <figure
                style={{
                  backgroundImage: `url(${logoImgUrl})`,
                }}
                onClick={() => handleToggleMenuButtonClick(false)}
              />
            </div>
            <div className="toggle-menu-button-menu toggle-menu-button-menu-open">
              <figure
                className="toggle-menu-button"
                onClick={() => handleToggleMenuButtonClick(false)}
              />
            </div>
          </div>
          <div className="toggle-menu-button-link-wrapper">
            <div
              className="toggle-menu-button-link"
              onClick={() => handleClickLink("/management")}
            >
              <figure
                className="toggle-menu-button-link-logo"
                style={{
                  backgroundImage: `url(${managementIconImgUrl})`,
                }}
              />
              <p>프로젝트 관리</p>
            </div>
            <div
              className="toggle-menu-button-link"
              onClick={() => handleClickLink("/recruitment")}
            >
              <figure
                className="toggle-menu-button-link-logo"
                style={{
                  backgroundImage: `url(${recruitmentIconImgUrl})`,
                }}
              />
              <p>인원 모집</p>
            </div>
          </div>
        </div>
      ) : (
        <div className="toggle-menu-button-menu">
          <figure
            className={
              isProfilePage
                ? "toggle-menu-button-profile"
                : "toggle-menu-button"
            }
            onClick={() => handleToggleMenuButtonClick(true)}
          />
        </div>
      )}
    </div>
  );
};

export default ToggleMenuButton;
