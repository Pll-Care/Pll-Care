import { useContext, useEffect } from "react";
import { InitialStateDispatchContext, SelectedMeetingRecordStateContext } from "../../pages/MeetingRecordManagement";
import MeetingRecordEditor from "./MeetingRecordEditor";

const SelectedMeetingRecord = () => {
    const selectedMeetingRecord = useContext(SelectedMeetingRecordStateContext);
    const { setInitialState } = useContext(InitialStateDispatchContext);

    useEffect(() => {
        setInitialState(false);
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