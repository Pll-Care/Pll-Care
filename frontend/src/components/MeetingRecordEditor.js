import ReactQuill from "react-quill";
import 'react-quill/dist/quill.snow.css';
import Quill from 'quill';
import ImageResize from 'quill-image-resize';
import { useContext, useEffect, useState } from "react";
import Button from "./Button";
import { InitialStateContext, InitialStateDispatchContext, MeetingRecordDispatchContext } from "../pages/MeetingRecordManagement";
import ControlMenu from "../utils/ControlMenu";
import { flushSync } from 'react-dom';
Quill.register('modules/ImageResize', ImageResize);

const teamMembers = [
    {
        id: 1,
        name: '조상욱',
        value: '1',
    },
    {
        id: 2,
        name: '홍서현',
        value: '2',
    },
    {
        id: 3,
        name: '이연제',
        value: '3',
    },
    {
        id: 4,
        name: '김도연',
        value: '4',
    }
]

const MeetingRecordEditor = ({ isEdit, originData }) => {
    const [selectedMember, setSelectedMember] = useState('1');
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    const { setInitialState }  = useContext(InitialStateDispatchContext);
    const initialState = useContext(InitialStateContext);

    const handleInitialState = () => {
        setInitialState((prevState) => !prevState);
    }

    const { onCreateMeetingRecord, onEditMeetingRecord, onRemoveMeetingRecord, onBookMarkMeetingRecord } = useContext(MeetingRecordDispatchContext);

    const handleCreateMeetingRecord = () => {
        const writerObj = teamMembers.find((member) => parseInt(member.value) === parseInt(selectedMember));
        flushSync(() => {
            onCreateMeetingRecord(writerObj.name, new Date().getTime(), title, content);
        });
    }

    const handleEditMeetingRecord = () => {
        const writerObj = teamMembers.find((member) => parseInt(member.value) === parseInt(selectedMember));
        flushSync(() => {
            onEditMeetingRecord(originData.id, writerObj.name, new Date().getTime(), title, content, originData.bookMarked);
        })
    }

    const handleRemoveMeetingRecord = () => {
        flushSync(() => {
            onRemoveMeetingRecord(originData.id);
        })
    }

    const handleBookMarkMeetingRecord = () => {
        flushSync(() => {
            onBookMarkMeetingRecord(originData.id, originData.writer, originData.date, originData.title, originData.content, true);
        });
    }

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    }

    useEffect(() => {
        if (isEdit) {
            setContent(originData.content);

            const writerObj = teamMembers.find((member) => member.name === originData.writer);
            setSelectedMember(writerObj.id);

            setTitle(originData.title);
        }
    }, [isEdit, originData]);

    return (
        <div className='meeting-record-new-meeting-record-editor'>
            {
                initialState ? (
                    <div className='meeting-record-initial-state'>
                        <h1 className='meeting-record-heading'>회의록을 작성해보세요!</h1>
                        <Button
                            text={'작성하기'}
                            onClick={handleInitialState}
                        />
                    </div>
                ) : (
                        <div className='meeting-record-content-editor'>
                            <div className='meeting-record-writer'>
                                <h1>작성자: </h1>
                                {isEdit ? (<ControlMenu
                                    onChange={setSelectedMember}
                                    value={selectedMember}
                                    optionList={teamMembers}
                                />) : (
                                    <ControlMenu
                                        onChange={setSelectedMember}
                                        value={selectedMember}
                                        optionList={teamMembers}
                                    />
                                )}
                            </div>
                            <div className='meeting-record-title'>
                                <h1>제목: </h1>
                                <input
                                    value={title}
                                    onChange={handleTitleChange}
                                    type='text'
                                    required
                                    placeholder={'제목을 입력하세요'}
                                />
                            </div>
                            <ReactQuill
                                className='react-quill'
                                value={content}
                                onChange={setContent}
                                modules={{
                                    toolbar: [
                                        [{ 'header': [1, 2, 3, false] }],
                                        [{ 'size': ['small', false, 'large', 'huge'] }],
                                        ['bold', 'italic', 'underline', 'strike'],
                                        [{ 'align': [] }],
                                        [{ 'color': [] }, { 'background': [] }],
                                        ['link', 'image'],
                                    ],
                                    ImageResize: {
                                        parchment: Quill.import('parchment')
                                    }
                                }} 
                            />
                            {!isEdit? (<Button
                                text={'작성 완료하기'}
                                onClick={handleCreateMeetingRecord}
                            />) : (
                                    <div className='meeting-record-button-wrapper'>
                                        <div className='meeting-record-button-left-col'>
                                            <Button
                                                text={'북마크하기'}
                                                onClick={handleBookMarkMeetingRecord}
                                            />
                                        </div>
                                        <div className='meeting-record-button-right-col'>
                                            <Button
                                                text={'수정하기'}
                                                onClick={handleEditMeetingRecord}
                                            />
                                            <Button
                                                text={'삭제하기'}
                                                onClick={handleRemoveMeetingRecord}
                                            />
                                    </div>
                                </div>
                            )}
                    </div>
                )
            }
        </div>
    )
}

export default MeetingRecordEditor;