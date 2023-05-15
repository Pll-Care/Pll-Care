import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
    accessToken: '',
    refreshToken: '',
    isLoggedIn: false,
}

const authSlice = createSlice({
    name: 'auth',
    initialState: authInitialState,
    reducers: {
        login(state) {
            const accessToken = localStorage.getItem("access_token");
            const refreshToken = localStorage.getItem("refresh_token");

            return {
                ...state,
                accessToken,
                refreshToken,
                isLoggedIn: true
            }
        },
        logout(state) {
            localStorage.clear();

            return {
                ...state,
                accessToken: '',
                refreshToken: '',
                isLoggedIn: false
            }
        }
    }
});

export const authReducer = authSlice.reducer;

export const authActions = authSlice.actions;

export default authSlice;