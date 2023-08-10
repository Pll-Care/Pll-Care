import { useState, useRef } from "react";
import { useNavigate, useParams } from "react-router";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { toast } from "react-toastify";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShareIcon from "@mui/icons-material/Share";

import { useEditorImageUploader } from "../../hooks/useEditorImageHandler";
import Button from "../common/Button";
import AlertCheckModal from "../common/AlertCheckModal";

import { isToken } from "../../utils/localstroageHandler";
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
import { useDispatch } from "react-redux";
import { authActions } from "../../redux/authSlice";
import RecruitmentDetailTitle from "./RecruitmentDetailTitle";
import RecruitmentDetailProject from "./RecruitmentDetailProject";
import RecruitmentDetailPosition from "./RecruitmentDetailPosition";
import RecruitmentDetailStackName from "./RecruitmentDetailStackName";
import RecruitmentDetailDescription from "./RecruitmentDetailDescription";

const RecruitmentDetailContent = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const quillRef = useRef(null);
  useEditorImageUploader(quillRef.current);

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

  const dispatch = useDispatch();

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

  // 삭제 버튼을 눌렀을 때
  const deleteRecruitmentPost = () => {
    deletePostMutate(id);
    navigate("/recruitment");
  };

  // 좋아요 버튼을 눌렀을 때
  const handleClickFavoriteIcon = () => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      mutate(id);
    }
  };

  // 백엔드 지원하기 버튼을 눌렀을 때
  const handleBackendApply = () => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setBackendApply((prevState) => !prevState);
    }
  };

  // 프론트엔드 지원하기 버튼을 눌렀을 때
  const handleFrontendApply = () => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setFrontendApply((prevState) => !prevState);
    }
  };

  // 기획 지원하기 버튼을 눌렀을 때
  const handleManagerApply = () => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setFrontendApply((prevState) => !prevState);
    }
  };

  // 디자인 지원하기 버튼을 눌렀을 때
  const handleDesignApply = () => {
    if (!isToken("access_token")) {
      dispatch(authActions.setIsLoginModalVisible(true));
    } else {
      setDesignApply((prevState) => !prevState);
    }
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

      {/*모집글 제목 컴포넌트*/}
      <RecruitmentDetailTitle
        data={data}
        title={title}
        formValues={formValues}
        handleChange={handleChange}
        inputRefs={inputRefs}
        isEdit={isEdit}
        setFormValues={setFormValues}
        setIsEdit={setIsEdit}
        setDeleteIsModalVisible={setDeleteIsModalVisible}
      />

      <div className="recruitment-detail">
        {/*모집글 프로젝트 내용 컴포넌트*/}
        <RecruitmentDetailProject
          data={data}
          isEdit={isEdit}
          handleChange={handleChange}
          formValues={formValues}
          inputRefs={inputRefs}
        />

        {/*모집글 포지션 컴포넌트*/}
        <RecruitmentDetailPosition
          isEdit={isEdit}
          data={data}
          formValues={formValues}
          handleChange={handleChange}
        />

        {/*모집글 스택, 프로젝트 이름 컴포넌트*/}
        <RecruitmentDetailStackName isEdit={isEdit} data={data} />

        {/*모집글 설명 컴포넌트*/}
        <RecruitmentDetailDescription
          data={data}
          isEdit={isEdit}
          formValues={formValues}
          setFormValues={setFormValues}
          handleChange={handleChange}
          inputRefs={inputRefs}
        />
      </div>
      <div className="recruitment-detail-icon">
        {isEdit ? (
          <Button text="수정 완료" onClick={handleModifyPost} />
        ) : (
          <>
            {data?.liked ? (
              <FavoriteIcon
                className="post-icon"
                onClick={handleClickFavoriteIcon}
              />
            ) : (
              <FavoriteBorderIcon
                className="post-icon"
                onClick={handleClickFavoriteIcon}
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
