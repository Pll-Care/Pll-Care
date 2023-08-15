// totalPageNumber: number
// currentPageNumber: number
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

  return (
    <nav className="pagination">
      <ul className="pageButtons">
        <li
          onClick={prevChapter}
          className={startNumber === 1 ? "disabled pageButton" : "pageButton"}
        >
          {"<"}
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
          {">"}
        </li>
      </ul>
    </nav>
  );
};

export default PaginationButton;
