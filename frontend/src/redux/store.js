import { configureStore } from "@reduxjs/toolkit";
import authSlice from "./authSlice";
import managementSlice from "./managementSlice";

const store = configureStore({
    reducer: {
        auth: authSlice.reducer,
        management: managementSlice.reducer
    }
});

export default store;