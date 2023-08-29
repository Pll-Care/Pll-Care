import { useQuery } from "react-query";
import { useLocation } from "react-router";
import { useState } from "react";

import Card from "../common/Card";
import { getFilterSchedule } from "../../lib/apis/scheduleManagementApi";
import ScheduleItem from "./ScheduleItem";
import { getProjectId } from "../../utils/getProjectId";
import { Loading } from "../common/Loading";
import Pagination from "../common/Pagination";
import { toast } from "react-toastify";

const ScheduleList = ({ nameId, option }) => {
  const projectId = getProjectId(useLocation());

  // 현재 페이지
  const [currentPage, setCurrentPage] = useState({
    all: 1,
    MILESTONE: 1,
    MEETING: 1,
    pastAll: 1,
  });
  // 한 페이지당 5개씩 보여주기
  const itemsPerPage = 5;
  // 총 게시글 개수
  let itemCount = 0;
  // 총 페이지 개수
  let pageCount = 0;

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
  if (schedules && !isLoading) {
    itemCount = schedules.totalElements;
    pageCount = Math.ceil(itemCount / itemsPerPage);
  }

  return (
    <Card className="schedule-lists">
      {isLoading && <Loading />}

      {!isLoading && schedules?.content && schedules?.content.length === 0 && (
        <h1 className="check-schedule-gray">해당 일정이 없습니다</h1>
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
            setCurrentPage={(newPage) => {
              setCurrentPage((prevCurrentPage) => ({
                ...prevCurrentPage,
                [option]: newPage,
              }));
            }}
            recordDatasPerPage={itemsPerPage}
            totalData={itemCount}
          />
        )}
      </div>
    </Card>
  );
};
export default ScheduleList;
