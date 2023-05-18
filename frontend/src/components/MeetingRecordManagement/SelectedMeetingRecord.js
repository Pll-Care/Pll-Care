import { useEffect } from "react";
import { useDispatch, useSelector } from 'react-redux';

import MeetingRecordEditor from "./MeetingRecordEditor";

import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";

const SelectedMeetingRecord = () => {
    const selectedMeetingRecord = useSelector(state => state.meetingRecordManagement.selectedMeetingRecord);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(meetingRecordManagementActions.onEditInitialState(false));
    })

    return (
        <div className='meeting-record-selected-meeting-record'>
            <MeetingRecordEditor
                isEdit={true}
                originData={selectedMeetingRecord}
            />
        </div>
    )
}

export default SelectedMeetingRecord;