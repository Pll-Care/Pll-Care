import { createSlice } from "@reduxjs/toolkit";

const meetingRecordManagementInitialState = {
  initialState: true,
  isSelectedMeetingRecord: false,
  isCreatedMeetingRecordVisible: false,
  selectedMeetingRecordId: -1,
  createdMeetingRecordId: -1,
  isEdit: false,
  title: "",
  content: "",
};

const meetingRecordManagementSlice = createSlice({
  name: "meetingRecordManagement",
  initialState: meetingRecordManagementInitialState,
  reducers: {
    setInitialState(state, action) {
      state.initialState = action.payload;
    },
    setSelectedMeetingRecordState(state, action) {
      state.isSelectedMeetingRecord = action.payload;
    },
    setIsCreatedMeetingRecordVisibleState(state, action) {
      state.isCreatedMeetingRecordVisible = action.payload;
    },
    setSelectedMeetingRecordId(state, action) {
      state.selectedMeetingRecordId = action.payload;
    },
    setCreatedMeetingRecordId(state, action) {
      state.createdMeetingRecordId = action.payload;
    },
    setIsEditState(state, action) {
      state.isEdit = action.payload;
    },
    setTitle(state, action) {
      state.title = action.payload;
    },
    setContent(state, action) {
      state.content = action.payload;
    },
  },
});

export const meetingRecordManagementActions =
  meetingRecordManagementSlice.actions;

export default meetingRecordManagementSlice;
