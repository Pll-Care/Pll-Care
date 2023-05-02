const Button = ({text, type, onClick}) => {
    const btnType = ['positive', 'negative'].includes(type) ? type : 'default';

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