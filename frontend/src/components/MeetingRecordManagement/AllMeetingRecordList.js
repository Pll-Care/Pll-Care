import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';

import MeetingRecordData from './MeetingRecordData';
import Pagination from '../shared/Pagination';
import ControlMenu from '../shared/ControlMenu';
import Button from '../../components/shared/Button';

import { useQuery, useQueryClient } from 'react-query';

import { getAllMeetingRecordList } from '../lib/apis/meetingRecordManagementApi';
import { useLocation } from 'react-router-dom';
import { meetingRecordManagementActions } from '../../redux/meetingRecordManagementSlice';

const filterOptionList = [
    {
        id: 1,
        name: '최신순',
        value: 'DESC'
    },
    {
        id: 2,
        name: '오래된순',
        value: 'ASC'
    },
]

const AllMeetingRecordList = () => {
    const [sortType, setSortType] = useState('DESC');
    const [currentPage, setCurrentPage] = useState(1);
    const [recordDatasPerPage, setRecordDatasPerPage] = useState(3);

    const projectId = parseInt(useLocation().pathname.slice(12, 14));
    
    const dispatch = useDispatch();

    const queryClient = useQueryClient();

    const { data = { meetingRecordList: [] } } = useQuery(['managementAllMeetingRecordList', projectId, currentPage, sortType], () => getAllMeetingRecordList(projectId, currentPage, sortType));

    const meetingRecordList = data.meetingRecordList;

    useEffect(() => {
        const nextPage = currentPage + 1;

        if (nextPage <= data.totalPages) {
            queryClient.prefetchQuery(['managementAllMeetingRecordList', projectId, nextPage, sortType], () => getAllMeetingRecordList(projectId, nextPage, sortType));
        }
    }, [currentPage, data.totalPages, projectId, queryClient, sortType]);

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
                            dispatch(meetingRecordManagementActions.onEditInitialState(false));
                            dispatch(meetingRecordManagementActions.onEditSelectedMeetingRecordState(false));
                        }}
                    />
                </div>
            </div>
            <div className='record-list-item-wrapper'>
                <MeetingRecordData
                    sortedMeetingRecordList={meetingRecordList}
                />
                <Pagination
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    recordDatasPerPage={recordDatasPerPage}
                    totalData={data.totalElements}
                />
            </div>
        </div>
    )
}

export default AllMeetingRecordList;