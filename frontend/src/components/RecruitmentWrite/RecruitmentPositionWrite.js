const RecruitmentPostionWrite = ({ formValues, handleChange, inputRefs }) => {
  return (
    <div className="options">
      <div className="options-container">
        <h5>백엔드</h5>
        <h5>프론트엔드</h5>
        <h5>기획</h5>
        <h5>디자인</h5>
      </div>

      <div className="options-count">
        <input
          className="position-number"
          type="number"
          min="0"
          placeholder="0"
          value={formValues.backendCnt}
          name="backendCnt"
          onChange={handleChange}
          ref={inputRefs.backendCnt}
        />
        <input
          className="position-number"
          type="number"
          min="0"
          placeholder="0"
          value={formValues.frontendCnt}
          name="frontendCnt"
          onChange={handleChange}
        />
        <input
          className="position-number"
          type="number"
          min="0"
          placeholder="0"
          value={formValues.managerCnt}
          name="managerCnt"
          onChange={handleChange}
        />
        <input
          className="position-number"
          type="number"
          min="0"
          placeholder="0"
          value={formValues.designCnt}
          name="designCnt"
          onChange={handleChange}
        />
      </div>
    </div>
  );
};
export default RecruitmentPostionWrite;
