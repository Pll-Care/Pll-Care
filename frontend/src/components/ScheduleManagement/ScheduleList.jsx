import { useQuery } from "react-query";
import { useLocation } from "react-router";
import { useState, useMemo, useCallback } from "react";
import { toast } from "react-toastify";

import Card from "../common/Card";
import ScheduleItem from "./ScheduleItem";
import { Loading } from "../common/Loading";
import Pagination from "../common/Pagination";

import { useScheduleClient } from "../../context/Client/ScheduleClientContext";
import { getProjectId } from "../../utils/getProjectId";

const ScheduleList = ({ nameId, option }) => {
  const projectId = getProjectId(useLocation());

  const { getFilterSchedule } = useScheduleClient();

  // 현재 페이지
  const [currentPage, setCurrentPage] = useState({
    all: 1,
    MILESTONE: 1,
    MEETING: 1,
    pastAll: 1,
  });

  // 총 페이지 개수
  const [pageCount, setPageCount] = useState(0);

  const { isLoading, data: schedules } = useQuery(
    ["filterSchedule", projectId, nameId, option, currentPage[option]],
    () => getFilterSchedule(projectId, nameId, option, currentPage[option]),
    {
      onError: (error) => {
        if (error.response.data.code === "MEMBER_001") {
          toast.error("해당 멤버는 존재하지 않습니다");
        }
      },
    }
  );
  useMemo(() => {
    if (!isLoading && schedules) {
      setPageCount(schedules.totalPages);
    }
  }, [schedules, isLoading]);

  const handlePaginationClick = useCallback(
    (newPage) => {
      setCurrentPage((prevCurrentPage) => ({
        ...prevCurrentPage,
        [option]: newPage,
      }));
    },
    [option]
  );

  return (
    <Card className="schedule-lists">
      {isLoading && <Loading />}

      {!isLoading && schedules?.content && schedules?.content.length === 0 && (
        <h1 className="check-schedule-gray-two">해당 일정이 없습니다</h1>
      )}
      {schedules &&
        !isLoading &&
        schedules?.content?.length > 0 &&
        schedules?.content?.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} option={option} />
        ))}
      <div className="schedule-lists-pagination">
        {pageCount > 0 && (
          <Pagination
            currentPage={currentPage[option]}
            setCurrentPage={(newPage) => handlePaginationClick(newPage)}
            totalPages={pageCount}
          />
        )}
      </div>
    </Card>
  );
};
export default ScheduleList;
