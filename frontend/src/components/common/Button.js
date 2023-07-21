const Button = ({
  text,
  type,
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
          ? `button ${btnSize} button_${btnType} profile_header_button`
          : `button ${btnSize} button_${btnType}`
      }
      onClick={onClick}
      type={buttonType}
    >
      {text}
    </button>
  );
};

export default Button;
