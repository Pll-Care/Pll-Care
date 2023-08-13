const Button = ({
  text,
  type,
  color = "green",
  size = "big",
  onClick,
  isProfile = false,
  buttonType = "button",
}) => {
  const btnSize = size === "big" ? "button_big" : "button_small";
  const btnType = [
    "positive",
    "positive_dark",
    "underlined",
    "profile",
  ].includes(type)
    ? type
    : "default";

  return (
    <button
      className={
        isProfile
          ? `button ${btnSize} button_${btnType} button_${color} profile_header_button`
          : `button ${btnSize} button_${btnType} button_${color}`
      }
      onClick={onClick}
      type={buttonType}
    >
      {text}
    </button>
  );
};

export default Button;
