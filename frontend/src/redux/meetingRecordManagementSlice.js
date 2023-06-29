import { createSlice } from "@reduxjs/toolkit";

const meetingRecordManagementInitialState = {
  initialState: true,
  isSelectedMeetingRecord: false,
  isCreatedMeetingRecordVisible: false,
  selectedMeetingRecordId: -1,
  createdMeetingRecordId: -1,
  isEdit: false,
  title: '',
  content: '',
};

const meetingRecordManagementSlice = createSlice({
  name: "meetingRecordManagement",
  initialState: meetingRecordManagementInitialState,
  reducers: {
    onEditInitialState(state, action) {
      state.initialState = action.payload;
    },
    onEditSelectedMeetingRecordState(state, action) {
      state.isSelectedMeetingRecord = action.payload;
    },
    onEditIsCreatedMeetingRecordVisibleState(state, action) {
      state.isCreatedMeetingRecordVisible = action.payload;
    },
    onEditSelectedMeetingRecordId(state, action) {
      state.selectedMeetingRecordId = action.payload;
    },
    onEditCreatedMeetingRecordId(state, action) {
      state.createdMeetingRecordId = action.payload;
    },
    onChangeIsEditState(state, action) {
      state.isEdit = action.payload;
    },
    setTitle(state, action) {
      state.title = action.payload;
    },
    setContent(state, action) {
      state.content = action.payload;
    }
  },
});

export const meetingRecordManagementActions =
  meetingRecordManagementSlice.actions;

export default meetingRecordManagementSlice;
