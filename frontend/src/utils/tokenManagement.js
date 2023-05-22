// 토큰 디코딩 함수
function decodeToken(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

// 액세스 토큰 만료 여부 확인 함수
export const isAccessTokenExpired = (accessToken) => {
    const tokenData = decodeToken(accessToken);
    const currentTime = Math.floor(Date.now() / 1000);

    return tokenData.exp < currentTime;
}