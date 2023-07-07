import { Grid, Pagination, useMediaQuery } from "@mui/material";
import RecruitmentPost from "./RecruitmentPost";
import { useState } from "react";

const RecruitmentPostList = () => {
  const itemCount = 16; // 총 RecruitmentPost 아이템 개수
  const itemsPerPageMd = 9; // md 화면 크기에서 한 페이지에 표시할 아이템 개수
  const itemsPerPageSm = 8;

  const isMobile = useMediaQuery("(max-width:900px)");

  const pageCount = isMobile
    ? Math.ceil(itemCount / itemsPerPageSm)
    : Math.ceil(itemCount / itemsPerPageMd); // 전체 페이지 수
  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지

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
              <RecruitmentPost />
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
