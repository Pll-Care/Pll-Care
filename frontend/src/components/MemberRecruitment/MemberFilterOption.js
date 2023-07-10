import { useState } from "react";

import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import SearchIcon from "@mui/icons-material/Search";

import Button from "../common/Button";

const MemberFilterOption = () => {
  const [optionClick, setOptionClick] = useState(false);

  const backend = ["spring", "django", "express", "flask", "php"];
  const frontend = [
    "React",
    "Vue",
    "typescript",
    "react-query",
    "redux",
    "recoil",
    "Next",
  ];
  const design = ["adobe", "figma", "sketch"];

  return (
    <div className="filter-options">
      <div className="filter-options-select">
        <ArrowDropDownIcon
          onClick={() => setOptionClick((prevOptionClick) => !prevOptionClick)}
        />
        <SearchIcon />
      </div>
      {optionClick && (
        <div className="filter-options-box">
          <h5>많이 찾는 기술 스택</h5>

          <h5>백엔드</h5>
          {backend.map((stack) => (
            <Button key={stack} text={stack} size="small" />
          ))}
          <h5>프론트 엔드</h5>
          {frontend.map((stack) => (
            <Button key={stack} text={stack} size="small" />
          ))}
          <h5>디자인</h5>
          {design.map((stack) => (
            <Button key={stack} text={stack} size="small" />
          ))}
        </div>
      )}
    </div>
  );
};
export default MemberFilterOption;
