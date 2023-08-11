import Button from "../common/Button";

const RecruitmentDetailPosition = ({
  isEdit,
  data,
  formValues,
  handleChange,
}) => {
  return (
    <div className="recruitment-detail-container">
      <h4>포지션</h4>
      <div className="recruitment-detail-container-select">
        <div className="recruitment-grid-row">
          <h5>백엔드</h5>
          <h5>프론트 엔드</h5>
          <h5>기획</h5>
          <h5>디자인</h5>
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
                {info.position === "백엔드" && (
                  <input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={formValues.backendCnt}
                    name="backendCnt"
                    onChange={handleChange}
                  />
                )}
                {info.position === "프론트엔드" && (
                  <input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={formValues.frontendCnt}
                    name="frontendCnt"
                    onChange={handleChange}
                  />
                )}
                {info.position === "기획" && (
                  <input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={formValues.managerCnt}
                    name="managerCnt"
                    onChange={handleChange}
                  />
                )}
                {info.position === "디자인" && (
                  <input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={formValues.designCnt}
                    name="designCnt"
                    onChange={handleChange}
                  />
                )}
              </div>
            ))}
        </div>

        <div className="recruitment-grid-row">
          <Button className="grid-item-three" size="small" text="지원" />
          <Button className="grid-item-three" size="small" text="지원" />
          <Button className="grid-item-three" size="small" text="지원" />
          <Button className="grid-item-three" size="small" text="지원" />
        </div>
      </div>
    </div>
  );
};
export default RecruitmentDetailPosition;
