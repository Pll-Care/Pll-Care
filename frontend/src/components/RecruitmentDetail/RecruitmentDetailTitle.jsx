import { useMediaQuery } from "@mui/material";
import projectDefaultImg from "../../assets/project-default-img.jpg";
import { getStringDate } from "../../utils/date";
import Button from "../common/Button";
import { query } from "../../utils/mediaQuery";

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
  const isMobile = useMediaQuery(query);

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

      techStack: data?.techStackList,
      recruitmentInfo: data?.recruitInfoList,
    });

    setIsEdit((prevState) => !prevState);
  };
  return (
    <div className="detail-title">
      <div className="detail-title-content">
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

        {!isEdit && <h2>{time} 작성</h2>}
        {!isEdit && !isMobile && <h2>좋아요 {data?.likeCount}</h2>}
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
