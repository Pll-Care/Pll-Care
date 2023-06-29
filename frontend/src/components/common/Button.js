const Button = ({ text, type, size = "big", onClick, isProfile = false }) => {
  const btnSize = size === "big" ? "button_big" : "button_small";
  const btnType = ["positive", "positive_dark", "underlined"].includes(type)
    ? type
    : "default";

  return (
    <button
      className={
        isProfile
          ? `button ${btnSize} button_${btnType} profile_header_button`
          : `button ${btnSize} button_${btnType}`
      }
      onClick={onClick}
    >
      {text}
    </button>
  );
};

export default Button;
