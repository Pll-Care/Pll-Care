import { useDispatch } from "react-redux";
import { isToken } from "../utils/localstroageHandler";
import { useRouter } from "./useRouter";
import { authActions } from "../redux/authSlice";

const useLinkMenuClick = () => {
  const { routeTo } = useRouter();
  const dispatch = useDispatch();

  const handleClickLinkMenu = (link) => {
    if (link === "/recruitment") {
      routeTo(link);
    } else if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      routeTo(link);
    }
  };

  return handleClickLinkMenu;
};

export default useLinkMenuClick;
