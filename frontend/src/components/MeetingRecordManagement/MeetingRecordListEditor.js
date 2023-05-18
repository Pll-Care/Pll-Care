import SelectedMeetingRecord from './SelectedMeetingRecord';
import MeetingRecordEditor from './MeetingRecordEditor';
import { useSelector } from 'react-redux';

const MeetingRecordListEditor = () => {
    const selectedMeetingRecord = useSelector(state => state.meetingRecordManagement.selectedMeetingRecord);
    return (
        <div className="meeting-record-list-editor">
            {
                selectedMeetingRecord?.id ? (
                    <div className='meeting-record-selected-meeting-record'>
                        <SelectedMeetingRecord />
                    </div>
                ) : (
                    <div className='meeting-record-new-meeting-record'>
                        <MeetingRecordEditor
                            isEdit={false}
                            originData={''}   
                        />
                    </div>
                )
            }
        </div>
    )
}

export default MeetingRecordListEditor;