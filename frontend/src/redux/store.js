import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";
import evaluationManagementSlice from "./evaluationManagementSlice";

const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
    meetingRecordManagement: meetingRecordManagementSlice.reducer,
    evaluationManagement: evaluationManagementSlice.reducer,
  },
});

export default store;
