import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";
import evaluationManagementSlice from "./evaluationManagementSlice";
import projectManagementSlice from "./projectManagementSlice";

const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
    meetingRecordManagement: meetingRecordManagementSlice.reducer,
    evaluationManagement: evaluationManagementSlice.reducer,
    projectManagement: projectManagementSlice.reducer
  },
});

export default store;
