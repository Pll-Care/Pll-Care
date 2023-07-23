import { Link } from "react-router-dom";

import { googleAuthUrl, kakaoAuthUrl, naverAuthUrl } from "../../utils/auth";
import { useRef } from "react";

const Login = ({ closeModal }) => {
  const modalOutside = useRef();

  const left = (window.innerWidth - 472) / 2;

  const handleModalClose = (e) => {
    if (e.target === modalOutside.current) {
      closeModal();
    }
  };

  return (
    <div
      className="login-modal-wrapper"
      ref={modalOutside}
      onClick={handleModalClose}
    >
      <div className="login-modal">
        <div className="login-logo-img"></div>
        <div className="login-heading">
          <h1>Log in</h1>
        </div>
        <div className="login-text">
          <p>
            플케어에서 팀원을 만나고 <br />
            프로젝트를 경험하세요!
          </p>
        </div>
        <div className="login-button-wrapper">
          <Link
            className="login-button login-button-google"
            onClick={() => {
              window.open(
                googleAuthUrl,
              );
            }}
          />
          <Link
            className="login-button login-button-kakao"
            onClick={() => {
              window.open(
                kakaoAuthUrl,
              );
            }}
          />
          <Link
            className="login-button login-button-naver"
            onClick={() => {
              window.open(
                naverAuthUrl,
              );
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default Login;
