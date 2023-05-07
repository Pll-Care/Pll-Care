import React, { useState } from "react";
import ReactPaginate from "react-paginate";

import ProjectItem from "./ProjectItem";

const ProjectList = ({ projectList }) => {
  // 페이지당 보여줄 데이터의 개수를 5로 설정
  const itemsPerPage = 5;

  // 현재 페이지 번호를 저장할 state 변수
  const [currentPage, setCurrentPage] = useState(0);

  // 현재 페이지에서 보여줄 데이터를 계산하는 함수
  const currentData = projectList.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  // 페이지가 변경될 때 호출되는 함수
  const handlePageClick = ({ selected }) => {
    setCurrentPage(selected);
  };

  // 전체 페이지의 개수를 계산
  const pageCount = Math.ceil(projectList.length / itemsPerPage);

  return (
    <div className="project-list">
      <div className="project-list-current">
        {currentData.map((it) => (
          <ProjectItem key={it.id} {...it} />
        ))}
      </div>
      <div className="project-list-pagination">
        <ReactPaginate
          previousLabel={"<"}
          nextLabel={">"}
          breakLabel={"..."}
          pageCount={pageCount}
          onPageChange={handlePageClick}
          initialPage={0}
          containerClassName={"pagination"}
          previousLinkClassName={"previous_page"}
          nextLinkClassName={"next_page"}
          disabledClassName={"pagination_disabled"}
          activeClassName={"pagination_active"}
          pageRangeDisplayed={5}
          marginPagesDisplayed={0}
        />
      </div>
    </div>
  );
};

ProjectList.defaultProps = {
  projectList: [],
};

export default ProjectList;
