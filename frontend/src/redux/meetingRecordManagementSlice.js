import { createSlice } from "@reduxjs/toolkit";

const meetingRecordManagementInitialState = {
  initialState: true,
  isSelectedMeetingRecord: false,
  isCreatedMeetingRecordVisible: false,
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
  },
});

export const meetingRecordManagementActions =
  meetingRecordManagementSlice.actions;

export default meetingRecordManagementSlice;
