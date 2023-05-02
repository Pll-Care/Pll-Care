import { Link } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';

import Login from './Login';
import Button from './Button';
import { AuthDispatchContext, AuthStateContext } from '../App';

const MainHeader = () => {
    const [isLoginModalVisible, setIsLoginModalVisible] = useState(false);
    const [isLogin, setIsLogin] = useState(true);

    const { onLogout } = useContext(AuthDispatchContext);

    const handleLogout = () => {
        onLogout();
        setIsLoginModalVisible(false);
    }

    const handleLogin = () => {
        setIsLoginModalVisible(true);
    }

    return (
        <>
            <header className='main-header'>
                <div className='main-header-left-col'>
                    <figure className='main-header-logo-img' />
                </div>
                <div className='main-header-link'>
                    <Link to={'/management'}>프로젝트 관리</Link>
                    <Link to={'/recruitment'}>인원 모집</Link>
                </div>
                {isLogin ? (
                    <div className='main-header-right-col main-header-logout-col'>
                        <Button 
                            text={'log out'}
                            onClick={handleLogout}
                        />
                        <Link className='main-header-user-profile-img' to={'/profile'} />
                    </div>
                ) : (
                    <div className='main-header-right-col main-header-login-col'>
                        <Button 
                            text={'log in'}
                            onClick={handleLogin}
                        />
                    </div>
                )}
            </header>
            {isLoginModalVisible && (
                <Login 
                    isLoginModalVisible={isLoginModalVisible} 
                    setIsLoginModalVisible={setIsLoginModalVisible}
                />
            )}
        </>
    )
}

export default MainHeader;