import { useEffect, useState } from "react";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from "../common/Pagination";
import { useLocation } from "react-router-dom";
import { useQuery, useQueryClient } from "react-query";
import { getProjectId } from "../../utils/getProjectId";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const BookMarkedMeetingRecordList = () => {
  const projectId = getProjectId(useLocation());

  const [currentPage, setCurrentPage] = useState(1);

  const queryClient = useQueryClient();

  const { getBookMarkMeetingRecordList } = useManagementClient();

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
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, data.totalPages, projectId, queryClient]);

  return (
    <div className="meeting-record-book-marked-record-list">
      <h1 className="meeting-record-heading">북마크</h1>
      <div className="meeting-record-body">
        <MeetingRecordData sortedMeetingRecordList={data.meetingRecordList} />
        <Pagination
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
          totalPages={data.totalPages}
          color={"white"}
        />
      </div>
    </div>
  );
};

export default BookMarkedMeetingRecordList;
