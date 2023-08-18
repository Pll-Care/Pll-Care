import { useRef } from "react";
import { Link } from "react-router-dom";

const ToggleMenuButton = ({
  isToggleMenuOpen,
  setIsToggleMenuOpen,
  isProfilePage,
}) => {
  const modalOutside = useRef();

  const handleToggleMenuButtonClick = (isBoolean) => {
    setIsToggleMenuOpen((_) => isBoolean);
  };

  const handleToggleButtonClose = (e) => {
    if (e.target === modalOutside.current) {
      setIsToggleMenuOpen(false);
    }
  };

  return (
    <div
      ref={modalOutside}
      className={isToggleMenuOpen ? "toggle-menu-wrapper" : ""}
      onClick={handleToggleButtonClose}
    >
      {isToggleMenuOpen ? (
        <div className="toggle-menu">
          <div className="toggle-menu-button-wrapper">
            <figure
              className="toggle-menu-button"
              onClick={() => handleToggleMenuButtonClick(false)}
            />
          </div>
          <div className="toggle-menu-items">
            <div>
              <Link
                to={"/management"}
                onClick={() => handleToggleMenuButtonClick(false)}
              >
                프로젝트 관리
              </Link>
            </div>
            <div>
              <Link
                to={"/recruitment"}
                onClick={() => handleToggleMenuButtonClick(false)}
              >
                인원 모집
              </Link>
            </div>
          </div>
        </div>
      ) : (
        <div className="toggle-menu-button-wrapper">
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
