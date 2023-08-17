import { createSlice } from "@reduxjs/toolkit";

const userInfoInitialState = {
  memberId: null,
  imageUrl: null,
};

const userInfoSlice = createSlice({
  name: "userInfo",
  initialState: userInfoInitialState,
  reducers: {
    setUserInfo(state, action) {
      const { memberId, imageUrl } = action.payload;

      state.memberId = memberId;
      state.imageUrl = imageUrl;
    },
  },
});

export const userInfoActions = userInfoSlice.actions;

export default userInfoSlice;
