import { Fragment } from "react";

import { concepts, location } from "../../utils/recruitment";

const RecruitmentProjectWrite = ({
  setFormValues,
  formValues,
  setImageUrl,
  projectData,
  handleChange,
  inputRefs,
}) => {
  // 프로젝트 선택함에 따라 이미지 반영하기
  const handleChangeProject = (e) => {
    const { name, value } = e.target;
    setFormValues((prevState) => ({
      ...prevState,
      [name]: value,
    }));

    const selectedProject = projectData.find(
      (project) => project.projectId === parseInt(value, 10)
    );
    selectedProject && setImageUrl(selectedProject.imageUrl);
  };
  return (
    <Fragment>
      <div className="member-content-project">
        <h3>프로젝트 선택</h3>
        {projectData?.length > 0 && (
          <select
            className="member-select1"
            name="projectId"
            onChange={handleChangeProject}
            value={formValues.projectId}
          >
            {projectData?.map((project, index) => (
              <option key={index} value={project.projectId}>
                {project.title}
              </option>
            ))}
          </select>
        )}
      </div>

      <div className="member-content-project">
        <h3>주제/분야</h3>
        <select
          className="member-select2"
          onChange={handleChange}
          value={formValues.concept || ""}
          name="concept"
        >
          <option disabled hidden>
            선택하세요
          </option>
          {concepts.map((concept, index) => (
            <option key={index} value={concept}>
              {concept}
            </option>
          ))}
        </select>
      </div>

      <div className="member-content-project">
        <h3>모집 기간</h3>
        <input
          className="member-select3"
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

      <div className="member-content-options">
        <h3>지역</h3>
        <select
          className="member-select4"
          onChange={handleChange}
          name="region"
          value={formValues.region || ""}
          ref={inputRefs.region}
        >
          <option disabled hidden>
            선택하세요
          </option>
          {location.map((place, index) => (
            <option key={index} value={place}>
              {place}
            </option>
          ))}
        </select>
      </div>
    </Fragment>
  );
};
export default RecruitmentProjectWrite;
