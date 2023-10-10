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
    "positive_radius",
    "underlined",
    "profile",
  ].includes(type)
    ? type
    : "default";

  return (
    <button
      className={
        isProfile
          ? `button ${btnSize} button_${btnType} button_${color} button_profile_header`
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
