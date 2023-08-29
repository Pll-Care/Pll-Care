const getSearchKeywordWithCach = (function () {
  const cache = {};
  const EXPIRATION_TIME = 1000 * 60;

  const searchKeyword = (keyword, API) => {
    if (cache[keyword] && cache[keyword].expiration > Date.now()) {
      return cache[keyword].data;
    } else {
      return API(keyword).then((data) => {
        // console.info("calling api");

        if (data) {
          const expiration = Date.now() + EXPIRATION_TIME;
          const cacheData = { data: data.stackList, expiration };
          cache[keyword] = cacheData;
          return data.stackList;
        }
      });
    }
  };
  return searchKeyword;
})();

export default getSearchKeywordWithCach;
