import { useState } from "react";
import useSearchList from "../../../../hooks/useSearchList";
import useSearch from "../../../../hooks/useSearch";
import StackItem from "../../../common/StackItem";
import {
  filterSelectStack,
  isStackinList,
} from "../../../../utils/searchStack/handleStackList";

const SearchStack = ({
  stackList,
  changeStack,
  deleteStack,
  type = "five",
}) => {
  const [stacks, setStacks] = useState([...stackList]);

  const {
    searchKeyword,
    autoSearchKeyword,
    selectIndex,
    isSelectVisible,
    KeyEvent,
    changeSearchKeyword,
    handleMouseEnter,
    handleBlur,
    clickStack,
    inputRef,
  } = useSearch();

  const { searchList } = useSearchList(searchKeyword);

  const handleKeyUp = (event) => {
    const key = event.key;
    if (KeyEvent[key]) {
      if (key === "Enter") {
        const response = KeyEvent[key](searchList);
        if (response && !isStackinList(stacks, response.name)) {
          setStacks((prev) => [...prev, response]);
          changeStack(response);
        }
      } else if (key === "ArrowDown" || "ArrowUp") {
        KeyEvent[key](searchList);
      } else {
        KeyEvent[key]();
      }
    }
  };

  const delectSelectStack = (event) => {
    const select = event.target.name;
    const result = filterSelectStack(stacks, select);
    setStacks([...result]);
    deleteStack(select);
  };

  const clickSelectStack = (event) => {
    const select = event.target.innerText;

    const response = clickStack(select, searchList);
    if (response && !isStackinList(stacks, select)) {
      setStacks((prev) => [...prev, response]);
      changeStack(response);
    }
  };

  return (
    <div className="w-f">
      <div className="search">
        <div className="search_select">
          <ul className="select_list w-f">
            <li className="list_input">
              <input
                className="w-f"
                type="search"
                placeholder="기술 이름을 입력해주세요."
                name="검색"
                value={autoSearchKeyword ? autoSearchKeyword : searchKeyword}
                onChange={changeSearchKeyword}
                onKeyUp={handleKeyUp}
                onBlur={handleBlur}
                ref={inputRef}
              />
            </li>
          </ul>
          {isSelectVisible ? (
            <div className="search_wrap">
              <ul className="search_list">
                {searchList.length > 0 ? (
                  searchList.map((skill, listIndex) => (
                    <li
                      key={listIndex}
                      id={listIndex}
                      onClick={clickSelectStack}
                      className={
                        listIndex === selectIndex
                          ? "search_item highlighted"
                          : "search_item"
                      }
                      onMouseEnter={handleMouseEnter}
                    >
                      <div className="search_item_image">
                        <img src={skill.imageUrl} alt="기술 스택 이미지" />
                      </div>
                      <div className="search_item_name">{skill.name}</div>
                    </li>
                  ))
                ) : (
                  <li className="search_item">
                    해당 기술이 없습니다. 영어로만 검색해주세요.
                  </li>
                )}
              </ul>
            </div>
          ) : null}
        </div>
      </div>
      {stacks.length > 0 ? (
        <StackItems
          stacks={stacks}
          delectSelectStack={delectSelectStack}
          type={type}
        />
      ) : null}
    </div>
  );
};

export default SearchStack;

const StackItems = ({ stacks, delectSelectStack, type }) => {
  return (
    <div className="profile_body_introduce_positionBox_stack_select">
      <ul
        className={`profile_body_introduce_positionBox_stack_bc_items${
          type === "five" ? "_five" : `_${type}`
        }`}
      >
        {stacks.map((skill, idx) => (
          <StackItem
            key={skill.name + idx}
            imageUrl={skill.imageUrl}
            name={skill.name}
            onClick={delectSelectStack}
            type="change"
          />
        ))}
      </ul>
    </div>
  );
};
