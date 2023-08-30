import { useDispatch } from "react-redux";
import { useCallback, Fragment, useState } from "react";
import { useQuery } from "react-query";

import Button from "../common/Button";
import AlertCheckModal from "../common/Modal/AlertCheckModal";

import { authActions } from "../../redux/authSlice";
import { useRouter } from "../../hooks/useRouter";
import { isToken } from "../../utils/localstorageHandler";
import { getRecruitmentProject } from "../../lib/apis/memberRecruitmentApi";

const MemberOption = () => {
  const { routeTo } = useRouter();
  const dispatch = useDispatch();

  const [projectModal, setProjectModal] = useState(false);

  // 프로젝트 관리 리스트 react query
  const { data, isLoading } = useQuery(
    ["allProject"],
    async () => await getRecruitmentProject(),
    {
      retry: 0,
    }
  );

  // 프로젝트 관리로 이동할 수 있게 하는 함수
  const WriteProjectHandler = () => {
    routeTo("/management");
  };

  // 작성하기 버튼을 클릭했을 때
  const handleClickLinkMenu = useCallback(() => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else if (!isLoading) {
      if (data && data.length > 0) {
        routeTo("/recruitment/post");
      } else if (data && data.length === 0) {
        setProjectModal(true);
      }
    }
  }, [isLoading, data, dispatch, routeTo]);

  return (
    <Fragment>
      <AlertCheckModal
        open={projectModal}
        onClose={() => setProjectModal(false)}
        text={`진행 중인 프로젝트가 없습니다.\n 프로젝트 관리탭에서 프로젝트를 생성하시겠습니까??`}
        clickHandler={WriteProjectHandler}
      />
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
    </Fragment>
  );
};
export default MemberOption;
