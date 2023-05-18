import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import Login from './Login/Login';
import Button from './Button';
import ToggleMenuButton from './ToggleMenuButton';

import { authActions } from '../redux/authSlice';

const MainHeader = () => {
    const [isLoginModalVisible, setIsLoginModalVisible] = useState(false);
    const [isToggleMenuOpen, setIsToggleMenuOpen] = useState(false);

    const dispatch = useDispatch();

    const authState = useSelector(state => state.auth.isLoggedIn);

    const handleLogout = () => {
        dispatch(authActions.logout());
        setIsLoginModalVisible(false);
    }

    const handleLogin = () => {
        setIsLoginModalVisible(true);

        window.addEventListener("message", (event) => {
            if (event.data === "login") {
                dispatch(authActions.login());
                setIsLoginModalVisible(false);
            }
        });
    }

    return (
        <>
            <header className='main-header'>
                <div className='main-header-left-col'>
                    <figure className='main-header-logo-img' />
                    <ToggleMenuButton
                        isToggleMenuOpen={isToggleMenuOpen}
                        setIsToggleMenuOpen={setIsToggleMenuOpen}
                    />
                </div>
                <div className='main-header-link'>
                    <Link to={'/management'}>프로젝트 관리</Link>
                    <Link to={'/recruitment'}>인원 모집</Link>
                </div>
                {authState ? (
                    <div className='main-header-right-col main-header-logout-col'>
                        <Button 
                            text={'log out'}
                            type={'positive'}
                            onClick={handleLogout}
                        />
                        <Link
                            className='main-header-user-profile-img'
                            to={'/profile'}
                        />
                    </div>
                ) : (
                    <div className='main-header-right-col main-header-login-col'>
                        <Button 
                            text={'log in'}
                            type={'positive'}
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