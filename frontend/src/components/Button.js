const Button = ({ text, type, size = "big", onClick }) => {
  const btnSize = size === "big" ? "button_big" : "button_small";
  const btnType = ["positive", "positive_dark"].includes(type)
    ? type
    : "default";

  return (
    <button className={`button ${btnSize} button_${btnType}`} onClick={onClick}>
      {text}
    </button>
  );
};

export default Button;
