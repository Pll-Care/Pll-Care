import { Link } from "react-router-dom";

import { googleAuthUrl, kakaoAuthUrl, naverAuthUrl } from '../utils/auth';
import { useRef } from "react";

const Login = ({ setIsLoginModalVisible }) => {
    const modalOutside = useRef();

    const handleLoginWindow = (url) => {
        const left = (window.innerWidth-472) / 2;

        window.open(url, '_blank', `width=450, height=500, top=55px, left=${left}`);
    }

    const handleModalClose = (e) => {
        if (e.target === modalOutside.current) {
            setIsLoginModalVisible(false);
        }
    }

    return (
        <div className='login-modal-wrapper' ref={modalOutside} onClick={handleModalClose}>
            <div className='login-modal'>
                <div className='login-logo-img'>
                    
                </div>
                <div className='login-heading'>
                    <h1>Log in</h1>
                </div>
                <div className='login-text'>
                    <p>
                        플케어에서 팀원을 만나고 <br />프로젝트를 경험하세요!
                    </p>
                </div>
                <div className='login-button-wrapper'>
                    <Link 
                        to={googleAuthUrl} 
                        target="_blank" 
                        className='login-button login-button-google'
                        onClick={() => {
                            handleLoginWindow(googleAuthUrl);
                        }}
                    />
                    <Link 
                        to={kakaoAuthUrl} 
                        target="_blank"  
                        className='login-button login-button-kakao' 
                        onClick={() => {
                            handleLoginWindow(kakaoAuthUrl);
                        }}
                    />
                    <Link 
                        to={naverAuthUrl} 
                        target="_blank"
                        className='login-button login-button-naver'
                        onClick={() => {
                            handleLoginWindow(naverAuthUrl);
                        }}
                    />
                </div>
            </div>
        </div>
    )
}

export default Login;