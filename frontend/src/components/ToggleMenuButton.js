import { useRef } from "react";
import { Link } from "react-router-dom";

const ToggleMenuButton = ({ isToggleMenuOpen, setIsToggleMenuOpen }) => {
    const modalOutside = useRef();

    const handleToggleMenuButtonClick = () => {
        setIsToggleMenuOpen((prevState) => !prevState);
    }

    const handleToggleButtonClose = (e) => {
        if (e.target === modalOutside.current) {
            setIsToggleMenuOpen(false);
        }
    }

    return (
        <div
            className={isToggleMenuOpen ? 'toggle-menu-button-wrapper' : 'toggle-menu-button-wrapper-close'}
            ref={modalOutside}
            onClick={handleToggleButtonClose}
        >
            <div
                className='toggle-menu-button'
            >
            {
                isToggleMenuOpen ? (
                    <div>
                        <figure
                            onClick={handleToggleMenuButtonClick}
                        />
                        <div className='toggle-menu-button-link-wrapper'>
                            <div><Link to={'/management'}>프로젝트 관리</Link></div>
                            <div><Link to={'/recruitment'}>인원 모집</Link></div>
                        </div>
                    </div>
                ) : (
                    <div>
                        <figure
                            onClick={handleToggleMenuButtonClick}
                        />
                    </div>   
                )
            }
            </div>
        </div>
    )
    
}

export default ToggleMenuButton;