import { useContext } from "react";
import { SelectedMeetingRecordStateContext } from "../pages/MeetingRecordManagement";
import MeetingRecordEditor from "./MeetingRecordEditor";

const SelectedMeetingRecord = () => {
    const selectedMeetingRecord = useContext(SelectedMeetingRecordStateContext);

    return (
        <div className='meeting-record-selected-meeting-record'>
            <MeetingRecordEditor
                isEdit={true}
                originData={selectedMeetingRecord}
                initialState={false}
            />
        </div>
    )
}

export default SelectedMeetingRecord;