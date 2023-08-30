import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";
import userInfoSlice from "./userInfoSlice";

const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
    meetingRecordManagement: meetingRecordManagementSlice.reducer,
    userInfo: userInfoSlice.reducer,
  },
});

export default store;
