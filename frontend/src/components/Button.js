const Button = ({ text, type, onClick }) => {
    const btnType = ['positive', 'positive_dark'].includes(type) ? type : 'default';

    return (
        <button 
            className={`button button_${btnType}`}
            onClick={onClick}
        >
            {text}
        </button>
    )
}

export default Button;