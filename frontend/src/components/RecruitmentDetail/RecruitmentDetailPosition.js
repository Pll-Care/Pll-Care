import { positions } from "../../utils/recruitment";
import Button from "../common/Button";

const RecruitmentDetailPosition = ({
  isEdit,
  data,
  formValues,
  setFormValues,
  handleChange,
}) => {
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
  return (
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
              <Button className="grid-item-three" size="small" text="지원" />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
export default RecruitmentDetailPosition;
