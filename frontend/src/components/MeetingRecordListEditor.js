import { useState } from 'react';

import Button from "./Button";
import NewMeetingRecord from "./NewMeetingRecord";

const MeetingRecordListEditor = () => {
    const [initialState, setInitialState] = useState(true);

    const handleInitialState = () => {
        setInitialState((prevState) => !prevState);
    }

    return (
        <div className="meeting-record-list-editor">
            {initialState ? (
                <div className='meeting-record-initial-state'>
                    <h1 className='meeting-record-heading'>회의록을 작성해보세요!</h1>
                    <Button
                        text={'작성하기'}
                        onClick={handleInitialState}
                    />
                </div>
            ): (
                <div className='meeting-record-new-meeting-record'>
                    <NewMeetingRecord />
                </div>
            )}
        </div>
    )
}

export default MeetingRecordListEditor;