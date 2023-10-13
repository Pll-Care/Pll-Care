import { useDispatch } from "react-redux";
import { useCallback } from "react";

import Button from "../common/Button";

import { authActions } from "../../redux/authSlice";
import { useRouter } from "../../hooks/useRouter";
import { isToken } from "../../utils/localstorageHandler";

const MemberOption = () => {
  const { routeTo } = useRouter();
  const dispatch = useDispatch();

  // 작성하기 버튼을 클릭했을 때
  const handleClickLinkMenu = useCallback(() => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      routeTo("/recruitment/post");
    }
  }, [dispatch, routeTo]);

  return (
    <div className="recruitment-wrapper">
      <div className="recruitment-container">
        <div className="member-option">
          <h2>모집 중인 프로젝트 목록</h2>
          <h3>Currently Recruiting</h3>
        </div>

        <div className="member-button">
          <Button
            text={"작성하기"}
            type={"positive"}
            color={"white"}
            onClick={handleClickLinkMenu}
          />
        </div>
      </div>
    </div>
  );
};
export default MemberOption;
