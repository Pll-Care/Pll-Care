import { useState, useRef } from "react";
import { useNavigate, useParams } from "react-router";
import { useQuery } from "react-query";
import { toast } from "react-toastify";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShareIcon from "@mui/icons-material/Share";

import Button from "../common/Button";
import AlertCheckModal from "../common/AlertCheckModal";

import { isToken } from "../../utils/localstroageHandler";
import { getRecruitmentPostDetail } from "../../lib/apis/memberRecruitmentApi";
import {
  useAddLikeRecruitmentMutation,
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
  const dispatch = useDispatch();

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
    }
  );

  // 모집글 수정
  const { mutate: modifyPostMutate } =
    useModifyRecruitmentPostMutation(formValues);

  // 모집글 삭제
  const { mutate: deletePostMutate } = useDeleteRecruitmentPostMutation(id);

  // 모집글 좋아요
  const { mutate } = useAddLikeRecruitmentMutation(id);

  const { title, description, recruitStartDate, recruitEndDate } = formValues;

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
    const totalCntSum = formValues.recruitInfo.reduce(
      (sum, item) => sum + item.totalCnt,
      0
    );
    if (totalCntSum < 1) {
      toast.error("포지션 인원 1명 이상이어야 합니다");
      inputRefs.backendCnt.current.focus();
      return;
    }

    const stacks = techStack.map((item) => item.name);

    const body = {
      ...formData,
      techStack: stacks,
      postId: id,
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
