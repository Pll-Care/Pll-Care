import { useQuery } from "react-query";
import { useLocation } from "react-router";
import { useMemo, useState } from "react";

import Card from "../common/Card";
import { getFilterSchedule } from "../../lib/apis/scheduleManagementApi";
import { getPastScheduleData } from "../../utils/schedule";
import ScheduleItem from "./ScheduleItem";
import { getProjectId } from "../../utils/getProjectId";
import { Pagination } from "@mui/material";

const ScheduleList = ({ nameId, option }) => {
  const projectId = getProjectId(useLocation());

  // 현재 페이지
  const [currentPage, setCurrentPage] = useState({
    all: 0,
    MILESTONE: 0,
    MEETING: 0,
    pastAll: 0,
  });
  // 한 페이지당 5개씩 보여주기
  const itemsPerPage = 5;
  // 총 게시글 개수
  let itemCount = 0;
  // 총 페이지 개수
  let pageCount = 0;

  const { isLoading, data: schedules } = useQuery(
    ["filterSchedule", projectId, nameId, option, currentPage[option]],
    () => getFilterSchedule(projectId, nameId, option, currentPage[option] + 1)
  );
  if (schedules && !isLoading) {
    itemCount = schedules.totalElements;
    pageCount = Math.ceil(itemCount / itemsPerPage);
  }

  console.log(option, "에 따른", schedules);
  return (
    <Card className="schedule-lists">
      {isLoading && <h1 className="check-schedule-gray">⏳ 로딩 중...</h1>}
      {!isLoading && schedules?.content && schedules?.content.length === 0 && (
        <h1 className="check-schedule-gray">해당 일정이 없습니다</h1>
      )}
      {!schedules && !isLoading && (
        <h1 className="check-schedule-gray">🥲 통신 오류났습니다.</h1>
      )}
      {schedules &&
        !isLoading &&
        schedules?.content?.length > 0 &&
        schedules?.content?.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} />
        ))}
      <div className="schedule-lists-pagination">
        {pageCount > 0 && (
          <Pagination
            count={pageCount}
            page={currentPage[option] + 1}
            onChange={(event, page) => {
              setCurrentPage((prevCurrentPage) => ({
                ...prevCurrentPage,
                [option]: page - 1,
              }));
            }}
          />
        )}
      </div>
    </Card>
  );
};
export default ScheduleList;
