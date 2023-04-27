import axios from "axios";

const Login = () => {
    const handleNaverLogin = async () => {
        try {
            const response = await axios.get('localhost:8080/oauth2/authorization/naver');
            console.log(response);
        } catch (error) {
            console.log(error);
        }
    }

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
                    <button className='login-button login-button-google'></button>
                    <button className='login-button login-button-kakao'></button>
                    <button className='login-button login-button-naver' onClick={handleNaverLogin}></button>
                </div>
            </div>
        </div>
    )
}

export default Login;