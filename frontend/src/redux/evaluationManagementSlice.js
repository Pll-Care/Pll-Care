import { createSlice } from "@reduxjs/toolkit";

const evaluationManagementInitialState = {
  evaluationManagement: [],
};

const evaluationManagementSlice = createSlice({
  name: "evaluationManagement",
  initialState: evaluationManagementInitialState,
  reducers: {
    addEvaluation: (state, action) => {
      state.evaluationManagement.push(action.payload);
    },
  },
});

export const { addEvaluation } = evaluationManagementSlice.actions;

export default evaluationManagementSlice;
