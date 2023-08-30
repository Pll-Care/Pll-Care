import { useCallback, useRef, useState } from "react";

const useSearch = () => {
  const [searchKeyword, setSearchKeyword] = useState("");
  const [autoSearchKeyword, setAutoSearchKeyword] = useState("");
  const [selectIndex, setSelectIndex] = useState(-1);
  const inputRef = useRef(null);

  let isSelectVisible = false;
  if (searchKeyword.length > 0) isSelectVisible = true;
  if (searchKeyword.length === 0) isSelectVisible = false;

  const KeyEvent = {
    Enter: (searchList) => {
      if (searchKeyword === "" || autoSearchKeyword === "") return;

      const filterItem = searchList.filter(
        (item) => item.name === autoSearchKeyword
      );

      const isNewItem = filterItem.length !== 0;

      if (isNewItem) {
        inputRef.current.value = "";
        setSearchKeyword("");
        setAutoSearchKeyword("");
        setSelectIndex(-1);
        return filterItem[0];
      }
    },
    ArrowDown: (searchList) => {
      if (searchList.length === 0) {
        return;
      }
      if (searchList.length === selectIndex + 1) {
        setSelectIndex(() => 0);
        return;
      }
      if (selectIndex === -1) {
        isSelectVisible = true;
      }
      setSelectIndex((index) => index + 1);
      setAutoSearchKeyword(searchList[selectIndex + 1].name);
    },
    ArrowUp: (searchList) => {
      if (selectIndex === -1) {
        return;
      }
      if (selectIndex === 0) {
        setAutoSearchKeyword("");
        setSelectIndex((index) => index - 1);
        isSelectVisible = false;
        return;
      }
      setSelectIndex((index) => index - 1);
      setAutoSearchKeyword(searchList[selectIndex - 1].name);
    },
    Escape: () => {
      setAutoSearchKeyword("");
      setSelectIndex(-1);
      isSelectVisible = false;
    },

    Backspace: () => {
      if (!!autoSearchKeyword) {
        setAutoSearchKeyword("");
        setSelectIndex(-1);
        isSelectVisible = false;
        setSearchKeyword("");
      }
    },
  };

  const changeSearchKeyword = useCallback((event) => {
    setSearchKeyword(event.target.value);
  }, []);

  const handleMouseEnter = useCallback((event) => {
    setSelectIndex(Number(event.target.id));
  }, []);

  const handleBlur = useCallback(() => {
    setSelectIndex(() => -1);
    setAutoSearchKeyword("");
  }, []);

  const clickStack = useCallback((select, searchList) => {
    const filterItem = searchList.filter((item) => item.name === select);
    const isNewItem = filterItem.length !== 0;

    if (isNewItem) {
      inputRef.current.value = "";
      setSearchKeyword("");
      setAutoSearchKeyword("");
      setSelectIndex(() => -1);
      return filterItem[0];
    }
  }, []);

  return {
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
  };
};

export default useSearch;
