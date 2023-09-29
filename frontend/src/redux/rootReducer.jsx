import { combineReducers, configureStore } from "@reduxjs/toolkit";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";
import userInfoSlice from "./userInfoSlice";

export const rootReducer = combineReducers({
  auth: authSlice.reducer,
  meetingRecordManagement: meetingRecordManagementSlice.reducer,
  userInfo: userInfoSlice.reducer,
});

const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
});

export default store;
