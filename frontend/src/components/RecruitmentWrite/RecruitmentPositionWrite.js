const RecruitmentPostionWrite = ({ formValues, handleChange, inputRefs }) => {
  return (
    <div className="options">
      <h5>백엔드</h5>
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
      <h5>프론트엔드</h5>
      <input
        className="position-number"
        type="number"
        min="0"
        placeholder="0"
        value={formValues.frontendCnt}
        name="frontendCnt"
        onChange={handleChange}
      />
      <h5>기획</h5>
      <input
        className="position-number"
        type="number"
        min="0"
        placeholder="0"
        value={formValues.managerCnt}
        name="managerCnt"
        onChange={handleChange}
      />
      <h5>디자인</h5>
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
  );
};
export default RecruitmentPostionWrite;
