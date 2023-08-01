import { useState } from "react";
import { useLocation } from "react-router";
import { useQuery } from "react-query";

import { Pagination } from "@mui/material";

import CalendarItem from "./CalendarItem";
import ScheduleModal from "./ScheduleModal";
import Button from "../common/Button";
import { getTodayDateEnglish } from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";

import { getTodayAfterSchedule } from "../../lib/apis/scheduleManagementApi";

const CalendarList = () => {
  const projectId = getProjectId(useLocation());
  const [modalIsVisible, setModalIsVisible] = useState(false);
  const [page, setPage] = useState(0);

  // 총 게시글 개수
  let itemCount = 0;
  // 총 페이지 개수
  let pageCount = 0;

  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth() + 1;
  const { data, isLoading } = useQuery(["todayAfterSchedule", page], () =>
    getTodayAfterSchedule(page + 1, projectId, currentYear, currentMonth)
  );

  if (data && !isLoading) {
    itemCount = data.totalElements;
    pageCount = data.totalPages;
  }
  console.log(data, itemCount, pageCount);

  const modalOpen = () => {
    setModalIsVisible(true);
  };
  const modalClose = () => {
    setModalIsVisible(false);
  };

  // 오늘 날짜 가져오기
  const calendar = getTodayDateEnglish();

  return (
    <div className="calendar-list">
      <div className="calendar-list-time">
        <h5>오늘</h5>
        <h1>{calendar}</h1>
      </div>
      {!data?.content && !isLoading && (
        <h1 className="check-schedule">통신 오류났습니다.</h1>
      )}
      {isLoading && <h1 className="check-schedule">로딩 중</h1>}
      {data?.content && data?.content.length === 0 && (
        <h1 className="check-schedule">오늘 이후의 일정이 없습니다.</h1>
      )}
      {data?.content?.map((data, index) => (
        <CalendarItem key={index} data={data} />
      ))}
      <div className="calendar-list-pagination">
        {pageCount > 0 && (
          <Pagination
            className="pagination"
            count={pageCount}
            page={page + 1}
            onChange={(event, page) => {
              setPage(page - 1);
            }}
          />
        )}
      </div>

      <div className="button-container">
        <Button text="새 일정 생성" onClick={() => modalOpen()} />
      </div>
      <ScheduleModal open={modalIsVisible} onClose={modalClose} />
    </div>
  );
};
export default CalendarList;
