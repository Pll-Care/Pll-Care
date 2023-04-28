import { Link } from "react-router-dom";

import { googleAuthUrl, kakaoAuthUrl, naverAuthUrl } from '../utils/auth';

const Login = () => {
    return (
        <div className='login-modal-wrapper'>
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
                    <Link to={googleAuthUrl} className='login-button login-button-google'></Link>
                    <Link to={kakaoAuthUrl} className='login-button login-button-kakao'></Link>
                    <Link to={naverAuthUrl} className='login-button login-button-naver'></Link>
                </div>
            </div>
        </div>
    )
}

export default Login;