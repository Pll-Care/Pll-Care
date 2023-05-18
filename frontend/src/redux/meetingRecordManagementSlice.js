import { createSlice } from "@reduxjs/toolkit";

const meetingRecordManagementInitialState = {
    meetingRecordList: [],
    selectedMeetingRecord: {},
    initialState: true,
    editState: 'beforeEdit',
    title: '',
    content: ''
}

const meetingRecordManagementSlice = createSlice({
    name: 'meetingRecordManagement',
    initialState: meetingRecordManagementInitialState,
    reducers: {
        initMeetingRecord(state, action) {
            state.meetingRecordList = action.payload;
        },
        onCreateMeetingRecord(state, action) {
            state.meetingRecordList.push({ ...action.payload, id: state.meetingRecordList.length + 1 });
        },
        onRemoveMeetingRecord(state, action) {
            state.meetingRecordList = state.meetingRecordList.filter((item) => parseInt(item.id) !== parseInt(action.payload.id));
        },
        onEditMeetingRecord(state, action) {    
            state.meetingRecordList = state.meetingRecordList.map((item) => parseInt(item.id) === parseInt(action.payload.id) ? { ...action.payload } : item);
        },
        addBookMarkedMeetingRecord(state, action) {
            state.meetingRecordList = state.meetingRecordList.map((item) => parseInt(item.id) === parseInt(action.payload.id) ? { ...action.payload } : item);
        },
        onEditSelectedMeetingRecord(state, action) {
            state.selectedMeetingRecord = { ...action.payload };
        },
        onEditInitialState(state, action) {
            state.initialState = action.payload;
        },
        onChangeEditState(state, action) {
            state.editState = action.payload;
        },
        onEditTitle(state, action) {
            state.title = action.payload;
        },
        onEditContent(state, action) {
            state.content = action.payload;
        }
    }
})

export const meetingRecordManagementActions = meetingRecordManagementSlice.actions;

export default meetingRecordManagementSlice;