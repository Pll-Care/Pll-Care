import { useContext, useState } from 'react';
import { SelectedMeetingRecordStateContext } from '../pages/MeetingRecordManagement';

import SelectedMeetingRecord from '../components/SelectedMeetingRecord';
import MeetingRecordEditor from './MeetingRecordEditor';

const MeetingRecordListEditor = () => {
    const selectedMeetingRecord = useContext(SelectedMeetingRecordStateContext);
    const [initialState, setInitialState] = useState(true);

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
                            initialState={initialState}
                            setInitialState={setInitialState}    
                        />
                    </div>
                )
            }
        </div>
    )
}

export default MeetingRecordListEditor;