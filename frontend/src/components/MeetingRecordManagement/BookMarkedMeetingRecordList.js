import { useState } from "react";

import { useSelector } from "react-redux";

import MeetingRecordData from "./MeetingRecordData";
import Pagination from "../Pagination";

const BookMarkedMeetingRecordList = () => {
    const meetingRecordList = useSelector(state => state.meetingRecordManagement.meetingRecordList);

    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [recordDatasPerPage, setRecordDatasPerPage] = useState(2);

    const indexOfLast = currentPage * recordDatasPerPage;
    const indexOfFirst = indexOfLast - recordDatasPerPage;

    const getBookMarkedMeetingRecordList = () => {
        const bookMarkedMeetingRecordList = meetingRecordList.filter((item) => item.bookMarked === true);
        
        return bookMarkedMeetingRecordList;
    }

    const getCurrentBookMarkedMeetingRecordList = () => {
        const currentBookMarkedList = getBookMarkedMeetingRecordList().slice(indexOfFirst, indexOfLast);

        return currentBookMarkedList;
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
                    totalData={getBookMarkedMeetingRecordList()?.length}
                />
            </div>
        </div>
    )
}

export default BookMarkedMeetingRecordList;