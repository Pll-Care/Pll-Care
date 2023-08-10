import { Link } from "react-router-dom";

import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

import projectDefaultImg from "../../assets/project-default-img.jpg";
import { getStringDate } from "../../utils/date";
import Button from "../common/Button";

const RecruitmentDetailTitle = ({
  data,
  formValues,
  handleChange,
  inputRefs,
  isEdit,
  setFormValues,
  setIsEdit,
  setDeleteIsModalVisible,
}) => {
  const time = data ? getStringDate(new Date(data.createdDate)) : "";

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
  return (
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
            value={formValues.title}
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
  );
};
export default RecruitmentDetailTitle;
