import { createSlice } from "@reduxjs/toolkit";

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

      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      state.isLoginModalVisible = false;
      state.isLoggedIn = true;
    },
    logout(state) {
      localStorage.clear();

      state.accessToken = "";
      state.refreshToken = "";
      state.isLoggedIn = false;
    },
    setIsLoginModalVisible(state, action) {
      state.isLoginModalVisible = action.payload;
    }
  },
});

export const authReducer = authSlice.reducer;

export const authActions = authSlice.actions;

export default authSlice;
