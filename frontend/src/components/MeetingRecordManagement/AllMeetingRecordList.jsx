import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from "../common/Pagination";
import ControlMenu from "../common/ControlMenu";
import Button from "../common/Button";

import { useQuery, useQueryClient } from "react-query";

import { useManagementClient } from "../../context/Client/ManagementClientContext";
import { getProjectId } from "../../utils/getProjectId";
import { useLocation } from "react-router-dom";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";

const filterOptionList = [
  {
    id: 1,
    name: "최신 순",
    value: "DESC",
  },
  {
    id: 2,
    name: "오래된 순",
    value: "ASC",
  },
];

const AllMeetingRecordList = () => {
  const [sortType, setSortType] = useState("DESC");
  const [currentPage, setCurrentPage] = useState(1);

  const projectId = getProjectId(useLocation());

  const { getCompleteProjectData, getAllMeetingRecordList } = useManagementClient();

  const { data: isCompleted } = useQuery(
    ["completeProjectData", projectId],
    () => getCompleteProjectData(projectId)
  );

  const dispatch = useDispatch();

  const queryClient = useQueryClient();

  const { data = { meetingRecordList: [] } } = useQuery(
    ["managementAllMeetingRecordList", projectId, currentPage, sortType],
    () => getAllMeetingRecordList(projectId, currentPage, sortType)
  );

  const meetingRecordList = data.meetingRecordList;

  const handleCreateMeetingRecord = () => {
    dispatch(meetingRecordManagementActions.setTitle(""));
    dispatch(meetingRecordManagementActions.setContent(""));
    dispatch(meetingRecordManagementActions.setIsEditState(false));
    dispatch(meetingRecordManagementActions.setInitialState(false));
    dispatch(
      meetingRecordManagementActions.setSelectedMeetingRecordState(false)
    );
    dispatch(
      meetingRecordManagementActions.setIsCreatedMeetingRecordVisibleState(
        false
      )
    );
  };

  useEffect(() => {
    const nextPage = currentPage + 1;

    if (nextPage <= data.totalPages) {
      queryClient.prefetchQuery(
        ["managementAllMeetingRecordList", projectId, nextPage, sortType],
        () => getAllMeetingRecordList(projectId, nextPage, sortType)
      );
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, data.totalPages, projectId, queryClient, sortType]);

  return (
    <div className="meeting-record-all-meeting-record-list">
      <div className="meeting-record-list-header">
        <div className="header-left-col">
          <h1 className="meeting-record-heading">전체</h1>
          <ControlMenu
            size={"small"}
            value={sortType}
            onChange={setSortType}
            optionList={filterOptionList}
          />
        </div>
        <div className="header-right-col">
          {!isCompleted && (
            <Button
              size={"small"}
              text={"새로운 회의록 작성"}
              onClick={handleCreateMeetingRecord}
            />
          )}
        </div>
      </div>
      <div className="record-list-item-wrapper">
        <MeetingRecordData sortedMeetingRecordList={meetingRecordList} />
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

export default AllMeetingRecordList;
