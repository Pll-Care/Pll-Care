import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";

const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
    meetingRecordManagement: meetingRecordManagementSlice.reducer,
  },
});

export default store;
