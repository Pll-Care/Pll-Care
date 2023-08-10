import { Tooltip } from "@mui/material";

const RecruitmentDetailStackName = ({ isEdit, data }) => {
  return (
    <>
      <div className="recruitment-detail-description">
        <h4>요구하는 스택</h4>
        {isEdit ? (
          <div className="recruitment-detail-description-stacks">
            {data?.techStackList?.map((stack, index) => (
              <Tooltip key={index} title={stack.name}>
                <img key={index} src={stack.imageUrl} alt="" />
              </Tooltip>
            ))}
          </div>
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
    </>
  );
};
export default RecruitmentDetailStackName;
