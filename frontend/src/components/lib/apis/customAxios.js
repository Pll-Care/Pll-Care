import axios from "axios";

const accessToken = localStorage.getItem('access_token') || '';

export const customAxios = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    },
})