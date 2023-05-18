import { configureStore } from "@reduxjs/toolkit";
import authSlice from "./authSlice";
import managementSlice from "./managementSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";

const store = configureStore({
    reducer: {
        auth: authSlice.reducer,
        management: managementSlice.reducer,
        meetingRecordManagement: meetingRecordManagementSlice.reducer
    }
});

export default store;