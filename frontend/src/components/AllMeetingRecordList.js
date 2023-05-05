import { useContext, useState } from 'react';
import { MeetingRecordStateContext } from '../pages/MeetingRecordManagement';

import AllMeetingRecordListData from './AllMeetingRecordData';
import Pagination from './Pagination';

const filterOptionList = [
    {
        id: 1,
        name: '최신순',
        value: 'latest'
    },
    {
        id: 2,
        name: '오래된순',
        value: 'oldest'
    },
]

const ControlMenu = ({ onChange, value, optionList }) => {
    return (
        <select
            className='control-menu'
            onChange={(e) => onChange(e.target.value)}
            value={value}
        >
            {optionList.map((opt) => (
                <option key={opt.id} value={opt.value}>{opt.name}</option>
            ))}
        </select>
    )
}

const AllMeetingRecordList = () => {
    const [sortType, setSortType] = useState('latest');
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [recordDatasPerPage, setRecordDatasPerPage] = useState(4);

    const meetingRecordList = useContext(MeetingRecordStateContext);

    const indexOfLast = currentPage * recordDatasPerPage;
    const indexOfFirst = indexOfLast - recordDatasPerPage;

    const getCurrentSortedMeetingRecordList = () => {
        const compare = (a, b) => {
            if (sortType === 'latest') {
                return parseInt(b.date) - parseInt(a.date);
            } else {
                return parseInt(a.date) - parseInt(b.date);
            }
        }

        const copyList = JSON.parse(JSON.stringify(meetingRecordList));

        const sortedList = copyList.sort(compare);

        let currentSortedRecords = sortedList.slice(indexOfFirst, indexOfLast);

        return currentSortedRecords;
    }

    return (
        <div className="meeting-record-all-meeting-record-list">
            <div className='meeting-record-all-meeting-record-list-header'>
                <h1>전체</h1>
                <ControlMenu
                    value={sortType}
                    onChange={setSortType}
                    optionList={filterOptionList}
                />
            </div>
            <div className='meeting-record-all-meeting-record-list-item-wrapper'>
                <AllMeetingRecordListData
                    sortedMeetingRecordList={getCurrentSortedMeetingRecordList}
                    isloading={isLoading}
                />
                <Pagination
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    recordDatasPerPage={recordDatasPerPage}
                    totalData={meetingRecordList.length}
                />
            </div>
        </div>
    )
}

export default AllMeetingRecordList;