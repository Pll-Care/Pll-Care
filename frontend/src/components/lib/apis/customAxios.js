import axios from "axios";

const accessToken = localStorage.getItem('access_token');

export const customAxios = axios.create({
    baseURL: 'https://localhost:8080',
    headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    },
})