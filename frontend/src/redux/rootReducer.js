import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { persistReducer } from "redux-persist";

import authSlice from "./authSlice";
import meetingRecordManagementSlice from "./meetingRecordManagementSlice";

import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["meetingRecordManagement"],
};

export const rootReducer = combineReducers({
  auth: authSlice.reducer,
  meetingRecordManagement: meetingRecordManagementSlice.reducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
});

export default store;
