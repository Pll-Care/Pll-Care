import { useState, useRef } from "react";
import { useNavigate, useParams } from "react-router";
import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from "react-query";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShareIcon from "@mui/icons-material/Share";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

import projectDefaultImg from "../../assets/project-default-img.jpg";
import Button from "../common/Button";
import AlertCheckModal from "../common/AlertCheckModal";

import { getStringDate } from "../../utils/date";
import { location } from "../../utils/recruitment";
import {
  applyRecruitmentPost,
  getRecruitmentPostDetail,
} from "../../lib/apis/memberRecruitmentApi";
import {
  useAddLikeRecruitmentMutation,
  useApplyRecruitmentPostMutation,
  useDeleteRecruitmentPostMutation,
  useModifyRecruitmentPostMutation,
} from "../../hooks/useRecruitmentMutation";

const RecruitmentDetailContent = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  // 수정 상태
  const [isEdit, setIsEdit] = useState(false);
  // 삭제 상태
  const [deleteIsModalVisible, setDeleteIsModalVisible] = useState(false);
  // 에러 창 모달
  const [errorModal, setErrorModal] = useState(false);
  const [errorText, setErrorText] = useState("");

  // 지원 상태
  // 백엔드
  const [backendApply, setBackendApply] = useState(false);
  const backendBody = {
    postId: id,
    position: "백엔드",
  };
  // 프론트
  const [frontendApply, setFrontendApply] = useState(false);
  const frontendBody = {
    postId: id,
    position: "프론트엔드",
  };
  // 기획
  const [managerApply, setManagerApply] = useState(false);
  const managerBody = {
    postId: id,
    position: "기획",
  };
  // 디자인
  const [designApply, setDesignApply] = useState(false);
  const designBody = {
    postId: id,
    position: "디자인",
  };

  const [formValues, setFormValues] = useState({
    title: "",
    description: "",
    recruitStartDate: "",
    recruitEndDate: "",
    reference: "",
    contact: "",
    region: "",
    backendCnt: 0,
    frontendCnt: 0,
    designCnt: 0,
    managerCnt: 0,
  });

  // 모집글 디테일 페이지 조회
  const { data } = useQuery(
    ["recruitmentDetail"],
    () => getRecruitmentPostDetail(id),
    {
      onSuccess: (data) => {
        setFormValues({
          title: data?.title,
          description: data?.description,
          recruitStartDate: data?.recruitStartDate,
          recruitEndDate: data?.recruitEndDate,
          reference: data?.reference,
          contact: data?.contact,
          region: data?.region,
          backendCnt: data?.recruitInfoList.filter(
            (stack) => stack.position === "백엔드"
          )[0].totalCnt,
          frontendCnt: data?.recruitInfoList.filter(
            (stack) => stack.position === "프론트엔드"
          )[0].totalCnt,
          designCnt: data?.recruitInfoList.filter(
            (stack) => stack.position === "디자인"
          )[0].totalCnt,
          managerCnt: data?.recruitInfoList.filter(
            (stack) => stack.position === "기획"
          )[0].totalCnt,
        });
      },
    }
  );

  // 모집글 수정
  const { mutate: modifyPostMutate } =
    useModifyRecruitmentPostMutation(formValues);

  // 모집글 삭제
  const { mutate: deletePostMutate } = useDeleteRecruitmentPostMutation(id);

  // 모집글 지원
  const queryClient = useQueryClient();
  const { mutate: applyPostMutate } = useMutation(applyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글 지원하였습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
    },
    onError: (error) => {
      setErrorText(error.response.data.message);
      setErrorModal(true);
    },
  });

  // 모집글 좋아요
  const { mutate } = useAddLikeRecruitmentMutation(id);

  const {
    title,
    description,
    recruitStartDate,
    recruitEndDate,
    contact,
    backendCnt,
    frontendCnt,
    managerCnt,
    designCnt,
  } = formValues;

  const inputRefs = {
    title: useRef(),
    description: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    reference: useRef(),
    region: useRef(),
    techStack: useRef(),
    contact: useRef(),
    backendCnt: useRef(),
  };

  const time = data ? getStringDate(new Date(data.createdDate)) : "";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // 수정 버튼 눌렀을 때
  const handleModifyPost = () => {
    const { backendCnt, frontendCnt, designCnt, managerCnt, ...allData } =
      formValues;

    if (title.length < 2) {
      toast.error("모집글 제목을 입력해주세요");
      inputRefs.title.current.focus();
      return;
    }
    const start = new Date(recruitStartDate);
    const end = new Date(recruitEndDate);
    if (start > end) {
      toast.error("모집 기간 수정해주세요");
      inputRefs.startDate.current.focus();
      return;
    }
    if (description.length < 2) {
      toast.error("모집글 설명을 입력해주세요");
      inputRefs.description.current.focus();
      return;
    }
    if (
      parseInt(backendCnt, 10) +
        parseInt(frontendCnt, 10) +
        parseInt(managerCnt, 10) +
        parseInt(designCnt, 10) ===
      0
    ) {
      toast.error("포지션 인원 1명 이상이어야 합니다");
      inputRefs.backendCnt.current.focus();
      return;
    }

    const recruitCnt = [
      {
        position: "백엔드",
        currentCnt: data?.recruitInfoList.filter(
          (stack) => stack.position === "백엔드"
        )[0].currentCnt,
        totalCnt: parseInt(backendCnt, 10),
      },
      {
        position: "프론트엔드",
        currentCnt: data?.recruitInfoList.filter(
          (stack) => stack.position === "프론트엔드"
        )[0].currentCnt,
        totalCnt: parseInt(frontendCnt, 10),
      },
      {
        position: "기획",
        currentCnt: data?.recruitInfoList.filter(
          (stack) => stack.position === "기획"
        )[0].currentCnt,
        totalCnt: parseInt(managerCnt, 10),
      },
      {
        position: "디자인",
        currentCnt: data?.recruitInfoList.filter(
          (stack) => stack.position === "디자인"
        )[0].currentCnt,
        totalCnt: parseInt(designCnt, 10),
      },
    ];
    const body = {
      ...allData,
      recruitInfo: recruitCnt,
      techStack: ["Firebase"],
      postId: id,
      //projectId: 11,
    };

    console.log("수정후", body);
    modifyPostMutate(body);
    setIsEdit((prevState) => !prevState);
  };

  // 수정 취소 버튼 눌렀을 때
  const handleCancelModifyPost = () => {
    setFormValues({
      title: data?.title,
      description: data?.description,
      recruitStartDate: data?.recruitStartDate,
      recruitEndDate: data?.recruitEndDate,
      reference: data?.reference,
      contact: data?.contact,
      region: data?.region,
      backendCnt: data?.recruitInfoList.filter(
        (stack) => stack.position === "백엔드"
      )[0].totalCnt,
      frontendCnt: data?.recruitInfoList.filter(
        (stack) => stack.position === "프론트엔드"
      )[0].totalCnt,
      designCnt: data?.recruitInfoList.filter(
        (stack) => stack.position === "디자인"
      )[0].totalCnt,
      managerCnt: data?.recruitInfoList.filter(
        (stack) => stack.position === "기획"
      )[0].totalCnt,
    });

    setIsEdit((prevState) => !prevState);
  };

  // 삭제 버튼을 눌렀을 때
  const deleteRecruitmentPost = () => {
    deletePostMutate(id);
    navigate("/recruitment");
  };

  return (
    <>
      <AlertCheckModal
        open={errorModal}
        onClose={() => {
          setErrorText("");
          setErrorModal(false);
        }}
        text={errorText}
        clickHandler={() => {
          setErrorText("");
          setErrorModal(false);
        }}
      />
      <AlertCheckModal
        open={deleteIsModalVisible}
        onClose={() => setDeleteIsModalVisible(false)}
        text="인원 모집글 삭제하시겠습니까?"
        clickHandler={deleteRecruitmentPost}
      />
      {/* 지원 */}
      <AlertCheckModal
        open={backendApply}
        onClose={() => setBackendApply(false)}
        text="해당 모집글 백엔드에 지원하시겠습니까?"
        clickHandler={() => applyPostMutate(backendBody)}
      />
      <AlertCheckModal
        open={frontendApply}
        onClose={() => setFrontendApply(false)}
        text="해당 모집글 프론트에 지원하시겠습니까?"
        clickHandler={() => applyPostMutate(frontendBody)}
      />
      <AlertCheckModal
        open={managerApply}
        onClose={() => setManagerApply(false)}
        text="해당 모집글 기획에 지원하시겠습니까?"
        clickHandler={() => applyPostMutate(managerBody)}
      />
      <AlertCheckModal
        open={designApply}
        onClose={() => setDesignApply(false)}
        text="해당 모집글 디자인에 지원하시겠습니까?"
        clickHandler={() => applyPostMutate(designBody)}
      />

      <div className="detail-title">
        <div className="detail-title-content">
          <Link to="/recruitment" className="mui-arrow">
            <ArrowBackIosNewIcon />
          </Link>

          {data?.projectImageUrl ? (
            <img src={data?.projectImageUrl} alt="" />
          ) : (
            <img src={projectDefaultImg} alt="" />
          )}

          {isEdit ? (
            <input
              type="text"
              placeholder="수정할 인원모집 제목을 입력하세요"
              value={title}
              onChange={handleChange}
              name="title"
              ref={inputRefs.title}
            />
          ) : (
            <h1>{data?.title}</h1>
          )}
          <h2>{time} 작성</h2>
        </div>
        <div className="detail-title-button">
          {isEdit ? (
            <Button
              type="underlined"
              text="수정 취소"
              onClick={handleCancelModifyPost}
            />
          ) : (
            <>
              {data?.editable && (
                <Button
                  type="underlined"
                  text="수정"
                  onClick={() => setIsEdit((prevState) => !prevState)}
                />
              )}
              {data?.deletable && (
                <Button
                  type="underlined"
                  text="삭제"
                  onClick={() => setDeleteIsModalVisible(true)}
                />
              )}
            </>
          )}
        </div>
      </div>

      <div className="recruitment-detail">
        <div className="recruitment-detail-content">
          <h4>모집 작성자</h4>

          <div className="recruitment-detail-content-name">
            {data?.authorImageUrl ? (
              <Avatar src={data?.authorImageUrl} />
            ) : (
              <Avatar />
            )}

            <h5>{data?.author}</h5>
          </div>
        </div>

        <div className="recruitment-detail-content">
          <h4>모집 기간</h4>
          {isEdit ? (
            <>
              <input
                type="date"
                required
                name="recruitStartDate"
                value={recruitStartDate}
                onChange={handleChange}
                data-placeholder="시작 일자"
                ref={inputRefs.startDate}
              />
              ~
              <input
                type="date"
                required
                name="recruitEndDate"
                value={recruitEndDate}
                onChange={handleChange}
                data-placeholder="종료 일자"
                ref={inputRefs.endDate}
              />
            </>
          ) : (
            <h5>
              {data?.recruitStartDate} ~ {data?.recruitEndDate}
            </h5>
          )}
        </div>

        <div className="recruitment-detail-content-last">
          <div className="recruitment-detail-content-last-content">
            <h4>모집 위치</h4>
            {isEdit ? (
              <select
                onChange={handleChange}
                name="region"
                value={formValues.region || ""}
                ref={inputRefs.region}
              >
                {location.map((place, index) => (
                  <option key={index} value={place}>
                    {place}
                  </option>
                ))}
              </select>
            ) : (
              <h5>{data?.region}</h5>
            )}
          </div>
        </div>

        <div className="recruitment-detail-container">
          <h4>포지션</h4>
          <div className="recruitment-detail-container-select">
            <h5>백엔드</h5>
            <h5>프론트 엔드</h5>
            <h5>기획</h5>
            <h5>디자인</h5>
          </div>
          <div className="recruitment-detail-container-member">
            {!isEdit &&
              data?.recruitInfoList.map((info, index) => (
                <h5 key={index}>
                  {info?.currentCnt} / {info?.totalCnt}
                </h5>
              ))}
            {isEdit &&
              data?.recruitInfoList.map((info, index) => (
                <div className="recruitment-detail-container-member-cnt">
                  <h5 key={index}>{info?.currentCnt} / </h5>
                  {info.position === "백엔드" && (
                    <input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={backendCnt}
                      name="backendCnt"
                      onChange={handleChange}
                    />
                  )}
                  {info.position === "프론트엔드" && (
                    <input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={frontendCnt}
                      name="frontendCnt"
                      onChange={handleChange}
                    />
                  )}
                  {info.position === "기획" && (
                    <input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={managerCnt}
                      name="managerCnt"
                      onChange={handleChange}
                    />
                  )}
                  {info.position === "디자인" && (
                    <input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={designCnt}
                      name="designCnt"
                      onChange={handleChange}
                    />
                  )}
                </div>
              ))}
          </div>
          {!isEdit && !data?.editable && !data?.deletable && (
            <div className="recruitment-detail-container-button">
              <Button
                size="small"
                text="지원"
                onClick={() => setBackendApply((prevState) => !prevState)}
              />
              <Button
                size="small"
                text="지원"
                onClick={() => setFrontendApply((prevState) => !prevState)}
              />
              <Button
                size="small"
                text="지원"
                onClick={() => setManagerApply((prevState) => !prevState)}
              />
              <Button
                size="small"
                text="지원"
                onClick={() => setDesignApply((prevState) => !prevState)}
              />
            </div>
          )}
        </div>

        <div className="recruitment-detail-description">
          <h4>요구하는 스택</h4>
          {isEdit ? (
            <>
              {/*<div className="member-stack">
                <div className="member-stack-input">
                  <input
                    placeholder="기술 스택을 추가하세요"
                    type="text"
                    value={stackInput}
                    onChange={handleStackInputChange}
                    ref={inputRefs.techStack}
                    onKeyDown={stackPlusClickHandler}
                  />
                </div>
                {stackInput && (
                  <div className="member-inputcontainer-box">
                    {filteredStacks.map((stack, index) => (
                      <h5 key={index} onClick={() => setStackInput(stack)}>
                        {stack}
                      </h5>
                    ))}
                  </div>
                )}
              </div>
              <div className="member-stack-button">
                {stacks?.length > 0 &&
                  stacks.map((stack) => (
                    <Button
                      text={stack}
                      size="small"
                      onClick={stackMinusClickHandler(stack)}
                    />
                  ))}
                {stacks.length === 0 && (
                  <h6>프로젝트에서 사용할 스택을 추가해보세요</h6>
                )}
              </div>*/}
            </>
          ) : (
            <div className="recruitment-detail-description-stacks">
              {data?.techStackList?.map((stack, index) => (
                <Tooltip key={index} title={stack.name}>
                  <img key={index} src={stack.imageUrl} alt="" />
                </Tooltip>
              ))}
            </div>
          )}
        </div>

        <div className="recruitment-detail-description">
          <h4>프로젝트 이름</h4>
          <h5>{data?.projectName}</h5>
        </div>

        <div className="recruitment-detail-description">
          <h4>설명</h4>
          {isEdit ? (
            <textarea
              onChange={handleChange}
              value={description}
              name="description"
              ref={inputRefs.description}
            />
          ) : (
            <h5>{data?.description}</h5>
          )}
        </div>

        <div className="recruitment-detail-description">
          <h4>레퍼런스</h4>
          {isEdit ? (
            <textarea
              onChange={handleChange}
              value={formValues.reference}
              name="reference"
              ref={inputRefs.reference}
            />
          ) : (
            <h5>{data?.reference}</h5>
          )}
        </div>

        <div className="recruitment-detail-description">
          <h4>컨택</h4>
          {isEdit ? (
            <textarea
              onChange={handleChange}
              value={contact}
              name="contact"
              ref={inputRefs.contact}
            />
          ) : (
            <h5>{data?.contact}</h5>
          )}
        </div>
      </div>
      <div className="recruitment-detail-icon">
        {isEdit ? (
          <Button text="수정 완료" onClick={handleModifyPost} />
        ) : (
          <>
            {data?.liked ? (
              <FavoriteIcon className="post-icon" />
            ) : (
              <FavoriteBorderIcon
                className="post-icon"
                onClick={() => mutate(id)}
              />
            )}
            <ShareIcon className="post-icon" />
          </>
        )}
      </div>
    </>
  );
};
export default RecruitmentDetailContent;
