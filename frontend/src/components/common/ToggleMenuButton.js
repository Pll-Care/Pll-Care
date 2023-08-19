import { useRef } from "react";
import { Link } from "react-router-dom";
import menuBarImgUrl from "../../assets/toggle-button-img.png";
import logoImgUrl from "../../assets/logo.svg";
import managementIconImgUrl from "../../assets/management-icon.svg";
import recruitmentIconImgUrl from "../../assets/recruitment-icon.svg";
import useLinkMenuClick from "../../hooks/useLinkMenuClick";

const ToggleMenuButton = ({ isToggleMenuOpen, setIsToggleMenuOpen, isProfilePage }) => {
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

   return (
    <div
      className={
        isToggleMenuOpen
          ? "toggle-menu-button-wrapper"
          : "toggle-menu-button-wrapper-close"
      }
      ref={modalOutside}
      onClick={handleToggleButtonClose}
    >
      <div className="toggle-menu-button">
        {isToggleMenuOpen ? (
          <div>
            <div className="toggle-menu-button-figure-wrapper">
              <figure
                style={{
                  backgroundImage: `url(${logoImgUrl})`,
                }}
                onClick={handleToggleMenuButtonClick}
              />
              <figure
                style={{
                  backgroundImage: `url(${menuBarImgUrl})`,
                }}
                onClick={handleToggleMenuButtonClick}
              />
            </div>
            <div className="toggle-menu-button-link-wrapper">
              <div>
                <figure
                  style={{
                    backgroundImage: `url(${managementIconImgUrl})`,
                  }}
                />
                <Link onClick={() => handleClickLinkMenu("/management")}>
                  프로젝트 관리
                </Link>
              </div>
              <div>
                <figure
                  style={{
                    backgroundImage: `url(${recruitmentIconImgUrl})`,
                  }}
                />
                <Link onClick={() => handleClickLinkMenu("/recruitment")}>
                  인원 모집
                </Link>
              </div>
            </div>
          </div>
        ) : (
          <div>
            <figure
              style={{
                backgroundImage: `url(${menuBarImgUrl})`,
              }}
              onClick={handleToggleMenuButtonClick}
            />
          </div>
        )}
      </div>
    </div>
  );
};


export default ToggleMenuButton;
