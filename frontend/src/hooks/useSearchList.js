import { useEffect, useState } from "react";
import { searchTechAPI } from "../lib/apis/profileApi";
import getSearchKeywordWithCach from "../utils/getSearchKeyWordWithCatche";

const useSearchList = (keyword) => {
  const [searchList, sestSearchList] = useState([]);

  useEffect(() => {
    const handleSearchData = async () => {
      const data = await getSearchKeywordWithCach(keyword, searchTechAPI);
      sestSearchList(data);
    };

    if (keyword.length < 1) return;

    const delayTimer = setTimeout(() => {
      handleSearchData();
    }, 500);

    return () => {
      clearTimeout(delayTimer);
      sestSearchList([]);
    };
  }, [keyword]);

  return { searchList };
};

export default useSearchList;
