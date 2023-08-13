import { Tooltip } from "@mui/material";
import SearchStack from "../Profile/Introduce/PositionBox/SearchStack";

const RecruitmentDetailStackName = ({
  isEdit,
  data,
  setFormValues,
  formValues,
}) => {
  // techStack 업데이트하는 함수
  const changeStack = (response) => {
    console.log("수정전", formValues.techStack, response);
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
    <>
      <div className="recruitment-detail-description">
        <h4>요구하는 스택</h4>
        {isEdit ? (
          <div className="recruitment-detail-description-stacks">
            <SearchStack
              className="search-stack"
              stackList={formValues.techStack}
              changeStack={changeStack}
              deleteStack={deleteStack}
            />
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
