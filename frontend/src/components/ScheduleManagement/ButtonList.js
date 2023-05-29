import React, { useState, useEffect } from "react";
import Button from "../Button";

function ButtonList(props) {
  const { names, onButtonClick } = props;
  const [activeButton, setActiveButton] = useState("");

  useEffect(() => {
    if (activeButton === "") {
      setActiveButton(names[0]);
      onButtonClick(names[0]);
    }
  }, [activeButton, names, onButtonClick]);

  const handleButtonClick = (name) => {
    setActiveButton(name);
    onButtonClick(name);
  };

  return (
    <div>
      {names.map((name, index) => (
        <Button
          key={index}
          text={name}
          type={activeButton === name ? "positive_dark" : ""}
          size={props.size === "small" ? "small" : "big"}
          onClick={() => handleButtonClick(name)}
        />
      ))}
    </div>
  );
}
export default ButtonList;
