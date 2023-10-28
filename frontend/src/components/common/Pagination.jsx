import { useState } from "react";

const Pagination = ({
  currentPage,
  setCurrentPage,
  totalPages,
  color = "gray",
}) => {
  let pageNumbers = [];
  const maxDataNumbers = 5;

  const [pageIndex, setPageIndex] = useState(0);

  const handleDecreasePageIndex = () => {
    pageIndex > 0 &&
      setPageIndex((prevPageIndex) => (prevPageIndex -= maxDataNumbers));
  };

  const handleIncreasePageIndex = () => {
    pageIndex + maxDataNumbers < Math.ceil(totalPages) &&
      setPageIndex((prevPageIndex) => (prevPageIndex += maxDataNumbers));
  };

  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  const handleCurrentPage = (e) => {
    setCurrentPage(e.target.value);
  };

  return (
    <div
      className={["pagination-wrapper", `pagination-wrapper-${color}`].join(" ")}
    >
      {pageNumbers?.length ? (
        <button onClick={handleDecreasePageIndex}>{"<"}</button>
      ) : null}
      <ul>
        {pageNumbers
          .slice(pageIndex, pageIndex + maxDataNumbers)
          .map((page) => (
            <li
              className={
                currentPage === page
                  ? "pagination-page-number-on"
                  : "pagination-page-number"
              }
              key={page}
              onClick={handleCurrentPage}
              value={page}
            >
              {page}
            </li>
          ))}
      </ul>
      {pageNumbers?.length ? (
        <button onClick={handleIncreasePageIndex}>{">"}</button>
      ) : null}
    </div>
  );
};

export default Pagination;
