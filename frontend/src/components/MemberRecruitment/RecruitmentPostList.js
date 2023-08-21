import { useState } from "react";
import { useQuery } from "react-query";

import { Grid, useMediaQuery } from "@mui/material";

import RecruitmentPost from "./RecruitmentPost";
import { getAllRecruitmentPost } from "../../lib/apis/memberRecruitmentApi";
import Pagination from "../common/Pagination";

const RecruitmentPostList = () => {
  const isMobile = useMediaQuery("(max-width:900px)");
  // 현재 페이지
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = isMobile ? 6 : 9;

  // 총 게시글 개수
  let itemCount = 0;
  // 총 페이지 개수
  let pageCount = 0;

  // 모집글 리스트 조회하는 함수
  const { data, isLoading } = useQuery(
    ["allRecruitmentPosts", currentPage, isMobile],
    () => getAllRecruitmentPost(currentPage, itemsPerPage)
  );

  if (data && !isLoading) {
    itemCount = data.totalElements;
    pageCount = Math.ceil(itemCount / itemsPerPage);
  }

  return (
    <div className="recruitment">
      <Grid
        container
        spacing={{ xs: 2, md: 3 }}
        columns={{ xs: 4, sm: 8, md: 12 }}
      >
        {!isLoading &&
          data?.content &&
          data?.content.map((content, index) => (
            <Grid item xs={4} sm={4} md={4} key={index}>
              <RecruitmentPost data={content} />
            </Grid>
          ))}
        {!isLoading && data?.content.length === 0 && (
          <Grid item xs={4} sm={8} md={12}>
            <h1>아직 모집 중인 프로젝트가 없습니다.</h1>
          </Grid>
        )}
        {!isLoading && !data && (
          <Grid item xs={4} sm={8} md={12}>
            <h1>통신 오류가 났습니다.</h1>
          </Grid>
        )}
      </Grid>
      {pageCount > 0 && (
        <Pagination
          currentPage={currentPage + 1}
          setCurrentPage={setCurrentPage}
          recordDatasPerPage={itemsPerPage}
          totalData={itemCount}
        />
      )}
    </div>
  );
};
export default RecruitmentPostList;
