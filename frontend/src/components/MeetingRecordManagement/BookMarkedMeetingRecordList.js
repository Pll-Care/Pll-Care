import { useEffect, useState } from "react";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from "../common/Pagination";
import { useLocation } from "react-router-dom";
import { useQuery, useQueryClient } from "react-query";
import { getBookMarkMeetingRecordList } from "../lib/apis/meetingRecordManagementApi";

const BookMarkedMeetingRecordList = () => {
  const projectId = parseInt(useLocation().pathname.slice(12, 14));

  const [currentPage, setCurrentPage] = useState(1);
  const [recordDatasPerPage, setRecordDatasPerPage] = useState(3);

  const queryClient = useQueryClient();

  const { data = { meetingRecordList: [] } } = useQuery(
    ["managementBookMarkMeetingRecordList", projectId, currentPage],
    () => getBookMarkMeetingRecordList(projectId, currentPage)
  );

  useEffect(() => {
    const nextPage = currentPage + 1;

    if (nextPage <= data.totalPages) {
      queryClient.prefetchQuery(
        ["managementBookMarkMeetingRecordList", projectId, nextPage],
        () => getBookMarkMeetingRecordList(projectId, nextPage)
      );
    }
  }, [currentPage, data.totalPages, projectId, queryClient]);

  return (
    <div className="meeting-record-book-marked-record-list">
      <h1 className="meeting-record-heading">북마크</h1>
      <div className="meeting-record-body">
        <MeetingRecordData sortedMeetingRecordList={data.meetingRecordList} />
        <Pagination
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
          recordDatasPerPage={recordDatasPerPage}
          totalData={data.totalElements}
        />
      </div>
    </div>
  );
};

export default BookMarkedMeetingRecordList;
