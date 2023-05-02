import { useContext, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { AuthDispatchContext } from "../App";

const JWTToken = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    const { onLogin } = useContext(AuthDispatchContext);

    useEffect(() => {
        if (accessToken && refreshToken) {
            localStorage.clear();
            localStorage.setItem('access_token', accessToken);
            localStorage.setItem('refresh_token', refreshToken);

            onLogin();

            window.close();

            window.opener.document.location.href= '/';
        }
    }, []);

    return (
        <div></div>
    )
}

export default JWTToken;