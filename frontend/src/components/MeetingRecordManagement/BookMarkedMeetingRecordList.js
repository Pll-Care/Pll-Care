import { useState } from "react";

import { useSelector } from "react-redux";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from '../shared/Pagination';

const BookMarkedMeetingRecordList = () => {
    const meetingRecordList = useSelector(state => state.meetingRecordManagement.meetingRecordList);

    const bookMarkedMeetingRecordList = useSelector(state => state.meetingRecordManagement.bookMarkedMeetingRecordList);

    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [recordDatasPerPage, setRecordDatasPerPage] = useState(3);

    const indexOfLast = currentPage * recordDatasPerPage;
    const indexOfFirst = indexOfLast - recordDatasPerPage;

    const getCurrentBookMarkedMeetingRecordList = () => {
        return bookMarkedMeetingRecordList.slice(indexOfFirst, indexOfLast);
    }

    return (
        <div className='meeting-record-book-marked-record-list'>
            <h1 className='meeting-record-heading'>북마크</h1>
            <div className='meeting-record-body'>
                <MeetingRecordData
                    sortedMeetingRecordList={getCurrentBookMarkedMeetingRecordList}
                    isLoading={isLoading}
                />
                <Pagination
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    recordDatasPerPage={recordDatasPerPage}
                    totalData={bookMarkedMeetingRecordList?.length}
                />
            </div>
        </div>
    )
}

export default BookMarkedMeetingRecordList;