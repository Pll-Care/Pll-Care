import { useContext, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { AuthDispatchContext, AuthStateContext } from "../App";

const JWTToken = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const { onLogin } = useContext(AuthDispatchContext);
    const authState = useContext(AuthStateContext);

    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    useEffect(() => {
        if (accessToken && refreshToken) {
            onLogin();

            localStorage.clear();
            localStorage.setItem('access_token', accessToken);
            localStorage.setItem('refresh_token', refreshToken);

            window.opener.postMessage("login", "*");

            window.close();
        }
    }, []);

    return (
        <div></div>
    )
}

export default JWTToken;