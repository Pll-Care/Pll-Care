import { useState } from "react";
import { useQuery } from "react-query";

import { useMediaQuery } from "@mui/material";

import RecruitmentPost from "./RecruitmentPost";
import Pagination from "../common/Pagination";
import { useRecruitmentClient } from "../../context/Client/RecruitmentClientContext";

const RecruitmentPostList = () => {
  const isMobile = useMediaQuery("(max-width:1024px)");
  // 현재 페이지
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = isMobile ? 6 : 9;

  // 총 페이지 개수
  let pageCount = 0;

  const { getAllRecruitmentPost } = useRecruitmentClient();

  // 모집글 리스트 조회하는 함수
  const { data, isLoading } = useQuery(
    ["allRecruitmentPosts", currentPage, isMobile],
    () => getAllRecruitmentPost(currentPage, itemsPerPage)
  );

  if (data && !isLoading) {
    pageCount = data.totalPages;
  }

  return (
    <div className="recruitment">
      <div className="recruitment-grid">
        {!isLoading &&
          data?.content &&
          data?.content.map((content, index) => (
            <RecruitmentPost data={content} key={index} />
          ))}
      </div>
      {pageCount > 0 && (
        <Pagination
          currentPage={currentPage + 1}
          setCurrentPage={setCurrentPage}
          totalPages={pageCount}
        />
      )}
    </div>
  );
};
export default RecruitmentPostList;
