import { useState, Fragment } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router";

import { Box, Tooltip } from "@mui/material";

import { authActions } from "../../redux/authSlice";
import Button from "../common/Button";
import Card from "../common/Card";
import AlertCheckModal from "../common/Modal/AlertCheckModal";
import { positions } from "../../utils/recruitment";
import { isToken } from "../../utils/localstorageHandler";
import {
  useApplyCancelRecruitmentPostMutation,
  useApplyRecruitmentPostMutation,
} from "../../hooks/Mutations/useRecruitmentMutation";
import SearchStack from "../Profile/Introduce/PositionBox/SearchStack";

const RecruitmentDetailPosition = ({
  isEdit,
  data,
  formValues,
  setFormValues,
  handleChange,
}) => {
  const dispatch = useDispatch();
  const { id } = useParams();
  // 지원하기 모달
  const [applyModal, setApplyModal] = useState(false);
  const [position, setPosition] = useState("");

  // 지원취소하기 모달
  const [applyCancelModal, setApplyCancelModal] = useState(false);
  const [cancelPosition, setCancelPosition] = useState("");

  // 모집글 지원
  const { mutate: applyPostMutate } = useApplyRecruitmentPostMutation();

  // 모집글 지원 취소
  const { mutate: applyCancelPostMutate } =
    useApplyCancelRecruitmentPostMutation();

  // 지원하기 버튼을 눌렀을 때
  const handlePositionApply = (pos) => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setApplyModal(true);
      setPosition(pos);
    }
  };

  // 지원 취소하기 버튼을 눌렀을 때
  const handlePositionApplyCancel = (pos) => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setApplyCancelModal(true);
      setCancelPosition(pos);
    }
  };

  // 포지션 별로 인원수 수정 함수
  const handleChangePosition = (e) => {
    const { name, value } = e.target;

    setFormValues((prevState) => {
      const newRecruitInfoList = formValues.recruitInfo.map((info) =>
        info.position === name ? { ...info, totalCnt: value } : info
      );
      return {
        ...prevState,
        recruitInfo: newRecruitInfoList,
      };
    });
  };

  // techStack 업데이트하는 함수
  const changeStack = (response) => {
    setFormValues((prevValues) => ({
      ...prevValues,
      techStack: [...prevValues.techStack, response],
    }));
  };

  // techStack 삭제 함수
  const deleteStack = (stackName) => {
    setFormValues((prevValues) => ({
      ...prevValues,
      techStack: prevValues.techStack.filter(
        (stack) => stack.name !== stackName
      ),
    }));
  };

  return (
    <Fragment>
      {/*지원하기 모달*/}
      <AlertCheckModal
        open={applyModal}
        onClose={() => setApplyModal(false)}
        text={`해당 모집글 ${position}에 지원하시겠습니까?`}
        clickHandler={() => {
          applyPostMutate({ postId: id, position: position });
        }}
      />
      {/*지원 취소 모달*/}
      <AlertCheckModal
        open={applyCancelModal}
        onClose={() => setApplyCancelModal(false)}
        text={`해당 모집글 ${cancelPosition} 지원을 취소하시겠습니까?`}
        clickHandler={() => {
          applyCancelPostMutate(id);
        }}
      />
      <div className="recruitment-detail-container">
        <h4>포지션</h4>
        <div className="recruitment-detail-container-select">
          <div className="recruitment-grid-row">
            {positions.map((pos) => (
              <h5 key={pos}>{pos}</h5>
            ))}
          </div>

          <div className="recruitment-grid-row">
            {!isEdit &&
              data?.recruitInfoList.map((info, index) => (
                <h5 key={index} className="grid-item">
                  {info?.currentCnt} / {info?.totalCnt}
                </h5>
              ))}
            {isEdit &&
              data?.recruitInfoList.map((info, index) => (
                <div className="recruitment-grid-row-item">
                  <h5 key={index}>{info?.currentCnt} / </h5>

                  <input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={formValues.recruitInfo[index].totalCnt}
                    name={formValues.recruitInfo[index].position}
                    onChange={handleChangePosition}
                  />
                </div>
              ))}
          </div>

          {data?.available && (
            <div className="recruitment-grid-row">
              {positions.map((pos) => (
                <Button
                  key={pos}
                  className="grid-item-three"
                  size="small"
                  text="지원"
                  onClick={() => handlePositionApply(pos)}
                />
              ))}
            </div>
          )}

          {!data?.available && data?.applyPosition && (
            <div className="recruitment-grid-row">
              {positions.map((pos) => (
                <Fragment>
                  {data?.applyPosition === pos ? (
                    <Button
                      className="grid-item-three"
                      size="small"
                      text="지원취소"
                      key={pos}
                      onClick={() => handlePositionApplyCancel(pos)}
                    />
                  ) : (
                    <Box sx={{ backgroundColor: "white", height: "20px" }} />
                  )}
                </Fragment>
              ))}
            </div>
          )}
        </div>

        <div className="recruitment-detail-container-tech">
          <h4>요구 기술 스택</h4>
          {isEdit ? (
            <div className="recruitment-detail-container-tech-search">
              <SearchStack
                className="search-stack"
                stackList={formValues.techStack}
                changeStack={changeStack}
                deleteStack={deleteStack}
              />
            </div>
          ) : (
            <Card className="recruitment-detail-container-tech-search-card">
              <div className="recruitment-detail-container-tech-stacks">
                {data?.techStackList?.map((stack, index) => (
                  <Tooltip key={index} title={stack.name}>
                    <img key={index} src={stack.imageUrl} alt="" />
                  </Tooltip>
                ))}
              </div>
            </Card>
          )}
        </div>
      </div>
    </Fragment>
  );
};
export default RecruitmentDetailPosition;
