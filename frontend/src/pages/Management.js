import { Outlet } from "react-router-dom";
import MainHeader from "../components/MainHeader";
import ManagementHeader from "../components/ManagementHeader";

const Management = () => {
    return (
        <div>
            <MainHeader />
            <ManagementHeader />
            <Outlet />
        </div>
    )
}

export default Management;