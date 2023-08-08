import { Link } from "react-router-dom";
import { useDispatch } from "react-redux";

import { authActions } from "../../redux/authSlice";

import { googleAuthUrl, kakaoAuthUrl, naverAuthUrl } from "../../utils/auth";

import logo from "../../assets/logo.png";
import ModalContainer from "../common/ModalContainer";

const Login = () => {
  const dispatch = useDispatch();

  const handleModalVisible = (visible) => {
    dispatch(authActions.setIsLoginModalVisible(visible));
  };

  return (
    <ModalContainer
      width={380}
      height={400}
      open={() => handleModalVisible(true)}
      onClose={() => handleModalVisible(false)}
    >
      <div className="login-modal">
        <div className="login-logo-img">
          <figure
            style={{
              backgroundImage: `url(${logo})`,
            }}
            alt={"로고"}
          />
        </div>
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
              window.open(googleAuthUrl);
            }}
          />
          <Link
            className="login-button login-button-kakao"
            onClick={() => {
              window.open(kakaoAuthUrl);
            }}
          />
          <Link
            className="login-button login-button-naver"
            onClick={() => {
              window.open(naverAuthUrl);
            }}
          />
        </div>
      </div>
    </ModalContainer>
  );
};

export default Login;
