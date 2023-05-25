import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import Button from "../Button";
import ControlMenu from "../ControlMenu";

import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";

import ReactQuill from "react-quill";
import 'react-quill/dist/quill.snow.css';
import Quill from 'quill';
import ImageResize from 'quill-image-resize';

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
    const [writer, setWriter] = useState('');
    const [date, setDate] = useState();

    const title = useSelector(state => state.meetingRecordManagement.title);

    const content = useSelector(state => state.meetingRecordManagement.content);

    const initialState = useSelector(state => state.meetingRecordManagement.initialState);

    const editState = useSelector(state => state.meetingRecordManagement.editState);

    const dispatch = useDispatch();

    const handleInitialState = () => {
        dispatch(meetingRecordManagementActions.onChangeEditState('editing'));
        dispatch(meetingRecordManagementActions.onEditInitialState(false));
    }

    const handleCreateMeetingRecord = () => {
        const writerObj = teamMembers.find((member) => parseInt(member.value) === parseInt(selectedMember));

        const newDate = new Date().getTime();
        
        dispatch(meetingRecordManagementActions.onCreateMeetingRecord({
            writer: writerObj.name,
            date: newDate,
            title,
            content,
            bookMarked: false
        }));

        setWriter(writerObj.name);
        setDate(newDate);

        dispatch(meetingRecordManagementActions.onEditContent(content));
        dispatch(meetingRecordManagementActions.onEditTitle(title));
        dispatch(meetingRecordManagementActions.onChangeEditState('afterEdit'));
    }

    const handleEditMeetingRecord = () => {
        const writerObj = teamMembers.find((member) => parseInt(member.value) === parseInt(selectedMember));

        const newDate = new Date().getTime();

        dispatch(meetingRecordManagementActions.onEditMeetingRecord({
            id: originData.id,
            writer: writerObj.name,
            date: newDate,
            title,
            content,
            bookMarked: originData.bookMarked
        }));

        setWriter(writerObj.name);
        setDate(newDate);

        dispatch(meetingRecordManagementActions.onEditContent(content));
        dispatch(meetingRecordManagementActions.onEditTitle(title));
        dispatch(meetingRecordManagementActions.onChangeEditState('afterEdit'));
    }

    const handleRemoveMeetingRecord = () => {
        dispatch(meetingRecordManagementActions.onRemoveMeetingRecord({
            id: originData.id
        }));
        dispatch(meetingRecordManagementActions.onEditInitialState(true));
    }

    const handleBookMarkMeetingRecord = () => {
        dispatch(meetingRecordManagementActions.addBookMarkedMeetingRecord({
            id: originData.id,
            writer: originData.writer,
            date: originData.date,
            title: originData.title,
            content: originData.content,
            bookMarked: true
        }));
    }

    const handleTitleChange = (e) => {
        dispatch(meetingRecordManagementActions.onEditTitle(e.target.value));
    }

    useEffect(() => {
        if (isEdit) {
            dispatch(meetingRecordManagementActions.onChangeEditState('beforeEdit'));
            dispatch(meetingRecordManagementActions.onEditContent(originData.content));

            const writerObj = teamMembers.find((member) => member.name === originData.writer);
            setSelectedMember(writerObj.id);

            dispatch(meetingRecordManagementActions.onEditTitle(originData.title));
            setWriter(originData.writer);
            setDate(originData.date);
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
                ) : ( editState === 'beforeEdit' ? (
                        <div className='meeting-record-not-edited-record'>
                            <div className='meeting-record-body'>
                                <h1>제목: {title}</h1>
                                <h1>작성자: {writer}</h1>
                                <h1>작성일자: {new Date(date).toLocaleDateString()}</h1>
                                <p dangerouslySetInnerHTML={{ __html: content }}></p>
                            </div>
                            <div className='meeting-record-button-wrapper'>
                                <div className='button-wrapper-left-col'>
                                    <Button
                                        text={'북마크하기'}
                                        onClick={handleBookMarkMeetingRecord}
                                    />
                                </div>
                                <div className='button-wrapper-right-col'>
                                    <Button
                                        text={'수정하러 가기'}
                                        onClick={() => dispatch(meetingRecordManagementActions.onChangeEditState('editing'))}
                                    />
                                    <Button
                                        text={'삭제하기'}
                                        onClick={handleRemoveMeetingRecord}
                                    />
                                </div>
                            </div>
                        </div>
                    ) : (
                            editState === 'editing' ? (
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
                                        onChange={(e) => dispatch(meetingRecordManagementActions.onEditContent(e))}
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
                                            <Button
                                                text={'수정하기'}
                                                onClick={handleEditMeetingRecord}
                                            />
                                        </div>
                                    )}
                            </div>
                            ) : (
                                <div className='meeting-record-edited-record'>
                                    <h1>제목: {title}</h1>
                                    <h1>작성자: {writer}</h1>
                                    <h1>작성일자: {new Date(date).toLocaleDateString()}</h1>
                                    <p dangerouslySetInnerHTML={{ __html: content }}></p>
                                </div>       
                        )
                ))
            }
        </div>
    )
}

export default MeetingRecordEditor;