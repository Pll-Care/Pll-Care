import { useContext, useEffect } from "react";
import MainHeader from "../components/MainHeader";
import { AuthStateContext } from "../App";

const Home = () => {
    const { authState } = useContext(AuthStateContext);

    useEffect(() => {
        console.log(authState);
    });

    return (
        <div>
            <MainHeader />
            <div>Home</div>
        </div>
    )
}

export default Home;