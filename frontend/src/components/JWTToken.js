import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";

const JWTToken = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    useEffect(() => {
        localStorage.clear();
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', refreshToken);

        window.close();

        window.opener.document.location.href= '/';
    }, []);

    return (
        <div></div>
    )
}

export default JWTToken;