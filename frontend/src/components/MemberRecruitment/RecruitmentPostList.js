import { useState } from "react";
import { useQuery } from "react-query";

import { Grid, Pagination, useMediaQuery } from "@mui/material";

import RecruitmentPost from "./RecruitmentPost";
import { getAllRecruitmentPost } from "../../lib/apis/memberRecruitmentApi";

const RecruitmentPostList = () => {
  const isMobile = useMediaQuery("(max-width:900px)");
  // 현재 페이지
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = isMobile ? 6 : 9;

  // 총 게시글 개수
  let itemCount = 0;
  // 총 페이지 개수
  let pageCount = 0;

  // 모집글 리스트 조회하는 함수
  const { data, isLoading } = useQuery(
    ["allRecruitmentPosts", currentPage],
    () => getAllRecruitmentPost(currentPage + 1, itemsPerPage)
  );
  if (data && !isLoading) {
    itemCount = data.totalElements;
    pageCount = Math.ceil(itemCount / itemsPerPage);
  }

  return (
    <div className="recruitment">
      <>
        <Grid
          container
          spacing={{ xs: 2, md: 3 }}
          columns={{ xs: 4, sm: 8, md: 12 }}
        >
          {!isLoading &&
            data?.content.map((content, index) => (
              <Grid item xs={4} sm={4} md={4} key={index}>
                <RecruitmentPost data={content} />
              </Grid>
            ))}
        </Grid>
        {pageCount > 0 && (
          <Pagination
            count={pageCount}
            page={currentPage + 1}
            onChange={(event, page) => setCurrentPage(page - 1)}
          />
        )}
      </>
    </div>
  );
};
export default RecruitmentPostList;
