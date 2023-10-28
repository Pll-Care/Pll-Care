import { useEffect, useState } from "react";
import getSearchKeywordWithCach from "../utils/searchStack/getSearchKeyWordWithCatche";
import { useProfileClient } from "../context/Client/ProfileClientContext";

const useSearchList = (keyword) => {
  const [searchList, sestSearchList] = useState([]);
  const { searchTechAPI } = useProfileClient();

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
  }, [keyword, searchTechAPI]);

  return { searchList };
};

export default useSearchList;
