import { Avatar } from "@mui/material";

import { location } from "../../utils/recruitment";

const RecruitmentDetailProject = ({
  data,
  isEdit,
  handleChange,
  formValues,
  inputRefs,
}) => {
  return (
    <div className="recruitment-detail-content">
      <div className="recruitment-detail-content-item">
        <h4>모집작성자</h4>

        <div className="recruitment-detail-content-item-name">
          {data?.authorImageUrl ? (
            <Avatar src={data?.authorImageUrl} className="mui-icon" />
          ) : (
            <Avatar className="mui-icon" />
          )}

          <h5>{data?.author}</h5>
        </div>
      </div>

      <div className="recruitment-detail-content-item">
        <h4>모집 기간</h4>
        {isEdit ? (
          <div className="recruitment-detail-content-item-duration">
            <input
              type="date"
              required
              name="recruitStartDate"
              value={formValues.recruitStartDate}
              onChange={handleChange}
              data-placeholder="시작 일자"
              ref={inputRefs.startDate}
            />
            ~
            <input
              type="date"
              required
              name="recruitEndDate"
              value={formValues.recruitEndDate}
              onChange={handleChange}
              data-placeholder="종료 일자"
              ref={inputRefs.endDate}
            />
          </div>
        ) : (
          <h5>
            {data?.recruitStartDate} ~ {data?.recruitEndDate}
          </h5>
        )}
      </div>

      <div className="recruitment-detail-content-item">
        <h4>모집 위치</h4>
        {isEdit ? (
          <select
            onChange={handleChange}
            name="region"
            value={formValues.region || ""}
            ref={inputRefs.region}
          >
            {location.map((place, index) => (
              <option key={index} value={place}>
                {place}
              </option>
            ))}
          </select>
        ) : (
          <h5>{data?.region}</h5>
        )}
      </div>
    </div>
  );
};
export default RecruitmentDetailProject;
