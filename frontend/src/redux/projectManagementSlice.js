import { createSlice } from "@reduxjs/toolkit";

const projectManagementInitialState = {
    completedProjectId: []
}

const projectManagementSlice = createSlice({
    name: "projectManagement",
    initialState: projectManagementInitialState,
    reducers: {
        addCompletedProjectId: (state, action) => {
            state.completedProjectId.push(action.payload);
        }
    }
});

export const projectManagementActions = projectManagementSlice.actions;

export default projectManagementSlice;
