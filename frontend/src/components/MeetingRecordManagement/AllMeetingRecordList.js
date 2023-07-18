import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from "../common/Pagination";
import ControlMenu from "../common/ControlMenu";
import Button from "../../components/common/Button";

import { useQuery, useQueryClient } from "react-query";

import { getAllMeetingRecordList } from "../../lib/apis/meetingRecordManagementApi";
import { getProjectId } from "../../utils/getProjectId";
import { useLocation } from "react-router-dom";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { isCompleteProject } from "../../utils/isCompleteProject";

const filterOptionList = [
  {
    id: 1,
    name: "최신순",
    value: "DESC",
  },
  {
    id: 2,
    name: "오래된순",
    value: "ASC",
  },
];

const AllMeetingRecordList = () => {
  const [sortType, setSortType] = useState("DESC");
  const [currentPage, setCurrentPage] = useState(1);
  const [recordDatasPerPage, setRecordDatasPerPage] = useState(3);

  const projectId = getProjectId(useLocation());

  const completedProjectId = useSelector(
    (state) => state.projectManagement.completedProjectId
  );
  const isComplete = isCompleteProject(completedProjectId, projectId);

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
  }, [currentPage, data.totalPages, projectId, queryClient, sortType]);

  return (
    <div className="meeting-record-all-meeting-record-list">
      <div className="meeting-record-list-header">
        <div className="header-left-col">
          <h1 className="meeting-record-heading">전체</h1>
          <ControlMenu
            value={sortType}
            onChange={setSortType}
            optionList={filterOptionList}
          />
        </div>
        <div className="header-right-col">
          {isComplete === "ONGOING" && <Button
            text={"새로운 회의록 작성하기"}
            onClick={handleCreateMeetingRecord}
          />}
        </div>
      </div>
      <div className="record-list-item-wrapper">
        <MeetingRecordData sortedMeetingRecordList={meetingRecordList} />
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

export default AllMeetingRecordList;
