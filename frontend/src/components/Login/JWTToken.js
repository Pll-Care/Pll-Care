import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useDispatch } from "react-redux";
import { authActions } from "../../redux/authSlice";

const JWTToken = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const dispatch = useDispatch();

    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    useEffect(() => {
        if (accessToken && refreshToken) {
            dispatch(authActions.login());

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