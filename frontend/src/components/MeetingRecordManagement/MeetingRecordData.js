import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";

const MeetingRecordData = ({ sortedMeetingRecordList }) => {
    const dispatch = useDispatch();

    const handleClickMeetingRecord = (e) => {
        dispatch(meetingRecordManagementActions.onEditSelectedMeetingRecord({
            id: parseInt(e.currentTarget.id),
            writer: e.currentTarget.getAttribute('writer'),
            date: new Date(parseInt(new Date(e.currentTarget.getAttribute('date')).getTime())),
            title: e.currentTarget.getAttribute('title'),
            content: e.currentTarget.getAttribute('content'),
        }));
    }

    return (
        <div className='meeting-record-all-meeting-record-list-data'>
            {Array.isArray(sortedMeetingRecordList) && sortedMeetingRecordList.map((record) => (
                <div
                    className='meeting-record-item'
                    key={record.memoId}
                    onClick={handleClickMeetingRecord}
                    id={record.memoId}
                    writer={record.author}
                    date={new Date(record.createdDate).toLocaleDateString()}
                    title={record.title}
                    content={record.content}
                >
                    <div className='meeting-record-item-date'>
                        {new Date(record.createdDate).toLocaleDateString()}
                    </div>
                    <div className='meeting-record-item-title'>
                        {record.title}
                    </div>
                    <div className='meeting-record-item-writer'>
                        {record.author}
                    </div>
                </div>
            ))}
        </div>
    )
}

export default MeetingRecordData;