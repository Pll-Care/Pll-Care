import { useState } from "react";

// totlaPage: number
// currentPage: number
// changePageNumber: (pageNumber: number) => void

const PaginationButton = ({
  totalPageNumber,
  currentPageNumber,
  changePageNumber,
}) => {
  const pageNumber = Math.ceil(currentPageNumber / 5);
  const startNumber = 1 + 5 * (pageNumber - 1);
  const endNumber = 5 + 5 * (pageNumber - 1);
  const pageNumbers = [];

  for (let i = startNumber; i <= endNumber; i++) {
    if (i <= totalPageNumber) pageNumbers.push(i);
  }

  const clickPageNumber = (pageNumber) => {
    changePageNumber(pageNumber);
  };
  const prevChapter = () => {
    changePageNumber(startNumber - 5);
  };
  const nextChapter = () => {
    changePageNumber(startNumber + 5);
  };
  const forFirstPage = () => {
    changePageNumber(1);
  };
  const forLastPage = () => {
    changePageNumber(totalPageNumber);
  };

  return (
    <nav className="pagination">
      <ul className="pageButtons">
        <li
          className={startNumber === 1 ? "disabled first_last" : "first_last"}
          onClick={forFirstPage}
        >
          처음 페이지
        </li>
        <li
          onClick={prevChapter}
          className={startNumber === 1 ? "disabled pageButton" : "pageButton"}
        >
          {"<<"}
        </li>
        {pageNumbers.map((page) => (
          <li
            value={page}
            key={page}
            onClick={(event) => clickPageNumber(event.target.value)}
            className={
              currentPageNumber === page ? "current pageButton" : "pageButton"
            }
          >
            {page}
          </li>
        ))}
        <li
          onClick={nextChapter}
          className={
            startNumber + 5 > totalPageNumber
              ? "disabled pageButton"
              : "pageButton"
          }
        >
          {">>"}
        </li>
        <li
          className={
            currentPageNumber === totalPageNumber
              ? "disabled first_last"
              : "first_last"
          }
          onClick={forLastPage}
        >
          끝 페이지
        </li>
      </ul>
    </nav>
  );
};

export default PaginationButton;
