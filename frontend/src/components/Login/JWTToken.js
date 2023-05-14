import { useContext, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { AuthDispatchContext } from "../../App";

const JWTToken = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const { onLogin } = useContext(AuthDispatchContext);

    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    useEffect(() => {
        if (accessToken && refreshToken) {
            onLogin();

            localStorage.clear();
            localStorage.setItem('access_token', accessToken);
            localStorage.setItem('refresh_token', refreshToken);

            window.opener?.postMessage("login", "*");

            window.close();
        }
    }, []);

    return (
        <div></div>
    )
}

export default JWTToken;