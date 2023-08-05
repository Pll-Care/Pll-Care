import { Link } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useCallback } from "react";
import { authActions } from "../../redux/authSlice";

import Button from "../common/Button";
import MemberFilterOption from "./MemberFilterOption";

import { useRouter } from "../../hooks/useRouter";
import { isToken } from "../../utils/localstroageHandler";

const MemberOption = () => {
  const { routeTo } = useRouter();
  const dispatch = useDispatch();

  const handleClickLinkMenu = useCallback(() => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      routeTo("/recruitment/post");
    }
  }, []);
  
  return (
    <>
      <div className="member-option">
        <div className="member-option-title">
          <h2>모집 중인 프로젝트 목록</h2>
          <h3>Currently Recruiting</h3>
        </div>
      </div>
      <div className="member-button">
        <Link onClick={handleClickLinkMenu}>
          <Button text="작성하기" type="positive" />
        </Link>
        <MemberFilterOption />
      </div>
    </>
  );
};
export default MemberOption;
