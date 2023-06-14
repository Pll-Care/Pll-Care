import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import MeetingRecordData from './MeetingRecordData';
import Pagination from '../shared/Pagination';
import ControlMenu from '../shared/ControlMenu';
import Button from '../../components/shared/Button';

import { meetingRecordManagementActions } from '../../redux/meetingRecordManagementSlice';

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

const AllMeetingRecordList = () => {
    const [sortType, setSortType] = useState('latest');
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [recordDatasPerPage, setRecordDatasPerPage] = useState(3);

    const meetingRecordList = useSelector(state => state.meetingRecordManagement.meetingRecordList);

    const indexOfLast = currentPage * recordDatasPerPage;
    const indexOfFirst = indexOfLast - recordDatasPerPage;

    const dispatch = useDispatch();

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
            <div className='meeting-record-list-header'>
                <div className='header-left-col'>
                    <h1 className='meeting-record-heading'>전체</h1>
                    <ControlMenu
                        value={sortType}
                        onChange={setSortType}
                        optionList={filterOptionList}
                    />
                </div>
                <div className='header-right-col'>
                    <Button
                        text={'새로운 회의록 작성하기'}
                        onClick={() => {
                            dispatch(meetingRecordManagementActions.onChangeEditState('editing'));
                            dispatch(meetingRecordManagementActions.onEditInitialState(false));
                            dispatch(meetingRecordManagementActions.onEditSelectedMeetingRecord({}));
                            dispatch(meetingRecordManagementActions.onEditTitle(''));
                            dispatch(meetingRecordManagementActions.onEditContent(''));
                        }}
                    />
                </div>
            </div>
            <div className='record-list-item-wrapper'>
                <MeetingRecordData
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