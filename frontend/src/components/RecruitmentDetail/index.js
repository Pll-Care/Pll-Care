import { useState, useRef } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShareIcon from "@mui/icons-material/Share";

import { authActions } from "../../redux/authSlice";
import Button from "../common/Button";
import AlertCheckModal from "../common/AlertCheckModal";
import RecruitmentDetailTitle from "./RecruitmentDetailTitle";
import RecruitmentDetailProject from "./RecruitmentDetailProject";
import RecruitmentDetailPosition from "./RecruitmentDetailPosition";
import RecruitmentDetailStackName from "./RecruitmentDetailStackName";
import RecruitmentDetailDescription from "./RecruitmentDetailDescription";

import { isToken } from "../../utils/localstroageHandler";
import {
  getRecruitmentPostDetail,
  modifyRecruitmentPost,
} from "../../lib/apis/memberRecruitmentApi";
import {
  useAddLikeRecruitmentMutation,
  useDeleteRecruitmentPostMutation,
} from "../../hooks/useRecruitmentMutation";
import { useMediaQuery } from "@mui/material";
import { query } from "../../utils/mediaQuery";

const RecruitmentDetailContent = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const baseUrl = "https://fullcare.store";
  const isMobile = useMediaQuery(query);

  // 수정 상태
  const [isEdit, setIsEdit] = useState(false);
  // 삭제 상태
  const [deleteIsModalVisible, setDeleteIsModalVisible] = useState(false);

  const [formValues, setFormValues] = useState({
    title: "",
    description: "",
    recruitStartDate: "",
    recruitEndDate: "",
    reference: "",
    contact: "",
    region: "",

    techStack: [],
    recruitInfo: [],
  });

  // 모집글 디테일 페이지 조회
  const { data } = useQuery(
    ["recruitmentDetail"],
    () => getRecruitmentPostDetail(id),
    {
      enabled: !isEdit,
      retry: 0,
      onSuccess: (data) => {
        if (!isEdit) {
          setFormValues({
            title: data?.title,
            description: data?.description,
            recruitStartDate: data?.recruitStartDate,
            recruitEndDate: data?.recruitEndDate,
            reference: data?.reference,
            contact: data?.contact,
            region: data?.region,

            techStack: data?.techStackList,
            recruitInfo: data?.recruitInfoList,
          });
        }
      },

      onError: () => {
        navigate("/recruitment");
        toast.error("모집글을 찾을 수 없습니다");
      },
    }
  );

  // 모집글 수정
  const queryClient = useQueryClient();
  const { mutate: modifyPostMutate } = useMutation(modifyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 수정되었습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
      queryClient.invalidateQueries("allRecruitmentPosts");
      setIsEdit((prevState) => !prevState);
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });

  // 모집글 삭제
  const { mutate: deletePostMutate } = useDeleteRecruitmentPostMutation(id);

  // 모집글 좋아요
  const { mutate } = useAddLikeRecruitmentMutation(id);

  const { title, description, reference, contact } = formValues;

  const inputRefs = {
    title: useRef(),
    description: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    reference: useRef(),
    region: useRef(),
    techStack: useRef(),
    contact: useRef(),
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
    const { techStack, ...formData } = formValues;
    if (title.length < 2) {
      toast.error("모집글 제목을 입력해주세요");
      inputRefs.title.current.focus();
      return;
    }
    if (description.length < 2) {
      toast.error("모집글 설명을 입력해주세요");
      inputRefs.description.current.focus();
      return;
    }
    const totalCntSum = formValues.recruitInfo.reduce(
      (sum, item) => sum + item.totalCnt,
      0
    );
    if (totalCntSum < 1) {
      toast.error("포지션 인원 1명 이상이어야 합니다");
      inputRefs.backendCnt.current.focus();
      return;
    }
    if (techStack.length === 0) {
      toast.error("프로젝트 기술 스택을 추가해주세요");
      return;
    }
    if (reference.length < 2) {
      toast.error("모집글 레퍼런스를 입력해주세요");
      inputRefs.reference.current.focus();
      return;
    }
    if (contact.length < 2) {
      toast.error("모집글 컨택을 입력해주세요");
      inputRefs.contact.current.focus();
      return;
    }

    const stacks = techStack.map((item) => item.name);

    const body = {
      ...formData,
      techStack: stacks,
      postId: id,
    };

    modifyPostMutate(body);
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

  // url 복사 버튼을 눌렀을 때
  const handleCopyClipBoard = async (text) => {
    try {
      if (!isToken("access_token")) {
        dispatch(authActions.setIsLoginModalVisible(true));
      } else {
        await navigator.clipboard.writeText(text);
        toast.success("클립보드에 링크가 복사되었어요.");
      }
    } catch (err) {
      toast.error("클립보드에 링크가 복사에 실패하였습니다");
    }
  };

  return (
    <>
      {/*모집글 삭제 확인 모달*/}
      <AlertCheckModal
        open={deleteIsModalVisible}
        onClose={() => setDeleteIsModalVisible(false)}
        text="인원 모집글 삭제하시겠습니까?"
        clickHandler={deleteRecruitmentPost}
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
          setFormValues={setFormValues}
          handleChange={handleChange}
        />

        {/*모집글 스택, 프로젝트 이름 컴포넌트*/}
        <RecruitmentDetailStackName
          isEdit={isEdit}
          data={data}
          formValues={formValues}
          setFormValues={setFormValues}
        />

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
                fontSize={isMobile ? "small" : "medium"}
                onClick={handleClickFavoriteIcon}
              />
            ) : (
              <FavoriteBorderIcon
                className="post-icon"
                fontSize={isMobile ? "small" : "medium"}
                onClick={handleClickFavoriteIcon}
              />
            )}
            <ShareIcon
              className="post-icon"
              fontSize={isMobile ? "small" : "medium"}
              onClick={() =>
                handleCopyClipBoard(`${baseUrl}${location.pathname}`)
              }
            />
          </>
        )}
      </div>
    </>
  );
};
export default RecruitmentDetailContent;
