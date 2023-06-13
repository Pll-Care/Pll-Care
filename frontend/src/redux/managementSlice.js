import { createSlice } from "@reduxjs/toolkit";

const managementInitialState = {
    projectList: [],
    id: 1,
};

const managementSlice = createSlice({
    name: 'management',
    initialState: managementInitialState,
    reducers: {
        onCreate(state, action) {
            state.projectList.push({ ...action.payload, id: state.id });
            state.id++;
        },
        onRemove(state, action) {
            state.projectList = state.projectList.filter((item) => parseInt(item.id) !== parseInt(action.payload));
        },
        onComplete(state, action) {
            state.projectList = state.projectList.map((item) => parseInt(item.id) === parseInt(action.payload) ? { ...item, state: 'complete' } : item);
        }
    }
})

export const managementActions = managementSlice.actions;

export default managementSlice;