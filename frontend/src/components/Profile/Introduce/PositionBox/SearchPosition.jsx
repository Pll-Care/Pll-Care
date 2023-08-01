import { useState } from "react";

const SearchPosition = ({ positionData, changePosition }) => {
  return (
    <div>
      <ul className="select_list w-f">
        <li className="list_input">
          <input
            className="w-f"
            type="search"
            placeholder="기술 이름을 입력해주세요."
            defaultValue={positionData}
            onChange={(e) => changePosition(e.target.value)}
          />
        </li>
      </ul>
    </div>
  );
};

export default SearchPosition;
