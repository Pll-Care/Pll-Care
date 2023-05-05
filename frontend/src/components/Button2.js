const Button2 = ({ text, onClick }) => {
  return (
    <button className={"button2"} onClick={onClick}>
      {text}
    </button>
  );
};

export default Button2;
