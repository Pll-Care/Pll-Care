import { useRef, useState } from "react";

const SearchStack = ({ stackList, changeStack }) => {
  const [stack, setStack] = useState([...stackList]);
  const [searchList, setSearchList] = useState([...searchData]);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [autoSearchKeyword, setAutoSearchKeyword] = useState("");
  const [focusIndex, setFocusIndex] = useState(-1);

  const inputRef = useRef(null);
  const listRef = useRef(null);
  let isSelectVisible = false;
  if (searchKeyword.length > 0) isSelectVisible = true;
  if (searchKeyword.length < 0) isSelectVisible = false;

  const KeyEvent = {
    Enter: (select) => {
      if (searchKeyword === "" || autoSearchKeyword === "") return;
      if (!stack.includes(select)) {
        changeStack([...stack, select]);
        setStack((prev) => [...prev, select]);
        inputRef.current.value = "";
        setSearchKeyword("");
        setAutoSearchKeyword("");
        setFocusIndex(-1);
      }
    },
    ArrowDown: () => {
      if (searchList.length === 0) {
        return;
      }
      if (listRef.current.childElementCount === focusIndex + 1) {
        setFocusIndex(() => 0);
        return;
      }
      if (focusIndex === -1) {
        isSelectVisible = true;
      }
      setFocusIndex((index) => index + 1);
      setAutoSearchKeyword(searchList[focusIndex + 1]);
    },
    ArrowUp: () => {
      if (focusIndex === -1) {
        return;
      }
      if (focusIndex === 0) {
        setAutoSearchKeyword("");
        setFocusIndex((index) => index - 1);
        isSelectVisible = false;
        return;
      }

      setFocusIndex((index) => index - 1);
      setAutoSearchKeyword(searchList[focusIndex - 1]);
    },
    Escape: () => {
      setAutoSearchKeyword("");
      setFocusIndex(-1);
      isSelectVisible = false;
    },
  };

  const changeKeyword = (e) => {
    //TODO: 여기서 서버 요청
    if (isSelectVisible) {
      setFocusIndex(-1);
    }
    setSearchKeyword(e.target.value);
  };

  const handleKeyUp = (e) => {
    if (KeyEvent[e.key]) {
      e.key === "Enter"
        ? KeyEvent[e.key](autoSearchKeyword)
        : KeyEvent[e.key]();
    }
  };

  const handleMouseEnter = (e) => {
    setFocusIndex(Number(e.target.id));
  };

  const handleBlur = () => {
    setFocusIndex(() => -1);
    setAutoSearchKeyword("");
  };

  const clickStack = (e) => {
    const select = e.target.innerText;

    if (!stack.includes(select)) {
      changeStack([...stack, select]);
      setStack((prev) => [...prev, select]);
      inputRef.current.value = "";
      setSearchKeyword("");
      setAutoSearchKeyword("");
      setFocusIndex(() => -1);
    }
  };

  const delectSelectStack = (e) => {
    const select = e.target.name;
    setStack((prev) => [...prev].filter((st) => st !== select));
    changeStack([...stack].filter((st) => st !== select));
  };

  return (
    <div>
      {stack.length > 0 ? (
        <div className="profile_body_introduce_positionBox_stack_select">
          <ul className="profile_body_introduce_positionBox_stack_bc_items">
            {stack.map((skill) => (
              <li
                key={skill}
                className="profile_body_introduce_positionBox_stack_ch_item"
              >
                <span>{skill}</span>
                <button name={skill} onClick={delectSelectStack}>
                  x
                </button>
              </li>
            ))}
          </ul>
        </div>
      ) : null}
      <div className="search">
        <div className="search_select">
          <ul className="select_list w-f">
            <li className="list_input">
              <input
                className="w-f"
                type="search"
                placeholder="기술 이름을 입력해주세요."
                name="검색"
                onChange={changeKeyword}
                onKeyUp={handleKeyUp}
                onBlur={handleBlur}
                ref={inputRef}
              />
            </li>
          </ul>
          {isSelectVisible ? (
            <div className="search_wrap">
              <ul className="search_list" ref={listRef}>
                {searchList.length > 0 ? (
                  searchList.map((skill, listIndex) => (
                    <li
                      key={listIndex}
                      id={listIndex}
                      onClick={clickStack}
                      className={
                        listIndex === focusIndex
                          ? "search_item highlighted"
                          : "search_item"
                      }
                      onMouseEnter={handleMouseEnter}
                    >
                      {skill}
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
    </div>
  );
};

export default SearchStack;

const searchData = [
  "CSS",
  "Tailwind CSS",
  "Styled-Component",
  "Java",
  "JavaScript",
  "HTML",
  "React",
  "TypeScript",
  "View",
];
