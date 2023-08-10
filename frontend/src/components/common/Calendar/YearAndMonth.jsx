import { useEffect, useState } from "react";
import useOutSide from "../../../hooks/useOutSide";

const YearAndMonth = ({ type, options, defaultValue, changeDate }) => {
  const [isDropdown, setIsDropdown] = useState(false);
  const { outsideRef, isOutsidClick } = useOutSide();

  const clickSelector = () => {
    setIsDropdown((prev) => !prev);
  };

  const clickOptions = (event) => {
    if (type === "year") {
      const info = { year: event.target.value };
      changeDate(info);
    }
    if (type === "month") {
      const info = { month: event.target.value };
      changeDate(info);
    }
    setIsDropdown(false);
  };

  useEffect(() => {
    if (isOutsidClick) {
      setIsDropdown(false);
    }
  }, [isOutsidClick]);
  return (
    <section ref={outsideRef}>
      <div
        className={
          isDropdown
            ? type === "year"
              ? "selector-year selector-focus"
              : "selector-month selector-focus"
            : type === "year"
            ? "selector-year"
            : "selector-month"
        }
        onClick={clickSelector}
      >
        <div className="selector-in">
          <div className="selector-in-title">
            <span>
              {type === "year" ? `${defaultValue}년` : `${defaultValue}월`}
            </span>
          </div>
        </div>
      </div>
      {isDropdown ? (
        <ul className="options" onClick={clickOptions}>
          {options.map((option) => (
            <li
              key={option.value}
              value={option.value}
              className={type === "year" ? "option-year" : "option-month"}
            >
              {option.title}
            </li>
          ))}
        </ul>
      ) : null}
    </section>
  );
};

export default YearAndMonth;
