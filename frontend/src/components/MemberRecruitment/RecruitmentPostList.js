import { useState } from "react";
import { useQuery } from "react-query";

import { Grid, Pagination, useMediaQuery } from "@mui/material";

import RecruitmentPost from "./RecruitmentPost";
import { getAllRecruitmentPost } from "../../lib/apis/memberRecruitmentApi";

const RecruitmentPostList = () => {
  // 현재 페이지
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPageMd = 9;
  const itemsPerPageSm = 8;
  const isMobile = useMediaQuery("(max-width:900px)");

  // 모집글 리스트 조회하는 함수
  const { data } = useQuery(["allRecruitmentPosts", currentPage], () =>
    getAllRecruitmentPost(currentPage + 1)
  );
  console.log(data);
  const itemCount = data?.size;
  const pageCount = isMobile
    ? Math.ceil(itemCount / itemsPerPageSm)
    : Math.ceil(itemCount / itemsPerPageMd); // 전체 페이지 수

  const startIndex = currentPage * (isMobile ? itemsPerPageSm : itemsPerPageMd);
  const endIndex =
    (currentPage + 1) * (isMobile ? itemsPerPageSm : itemsPerPageMd);

  return (
    <div className="recruitment">
      <Grid
        container
        spacing={{ xs: 2, md: 3 }}
        columns={{ xs: 4, sm: 8, md: 12 }}
      >
        {Array.from(Array(itemCount))
          .slice(startIndex, endIndex)
          .map((_, index) => (
            <Grid item xs={4} sm={4} md={4} key={index}>
              <RecruitmentPost data={data.content[index]} />
            </Grid>
          ))}
      </Grid>
      {pageCount > 1 && (
        <Pagination
          count={pageCount}
          page={currentPage + 1}
          onChange={(event, page) => setCurrentPage(page - 1)}
        />
      )}
    </div>
  );
};
export default RecruitmentPostList;
