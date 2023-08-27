import { createSlice } from "@reduxjs/toolkit";
import { customAxios } from "../lib/apis/customAxios";

const authInitialState = {
  accessToken: "",
  refreshToken: "",
  isLoggedIn: false,
  isLoginModalVisible: false,
};

const authSlice = createSlice({
  name: "auth",
  initialState: authInitialState,
  reducers: {
    login(state) {
      const accessToken = localStorage.getItem("access_token");
      const refreshToken = localStorage.getItem("refresh_token");

      state.isLoggedIn = true;
      state.isLoginModalVisible = false;
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      customAxios.defaults.headers.common["Authorization"] = accessToken
        ? `Bearer ${accessToken}`
        : null;
    },
    logout(state) {
      localStorage.clear();

      state.accessToken = "";
      state.refreshToken = "";
      state.isLoggedIn = false;
      customAxios.defaults.headers.common["Authorization"] = null;
    },
    setIsLoginModalVisible(state, action) {
      state.isLoginModalVisible = action.payload;
    },
  },
});

export const authReducer = authSlice.reducer;

export const authActions = authSlice.actions;

export default authSlice;
