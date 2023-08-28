import { Fragment } from "react";

import { concepts, location } from "../../utils/recruitment";
import ControlMenu from "../common/ControlMenu";

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
      <div className="member-grid">
        <div className="member-grid-item">
          <h3>프로젝트 선택</h3>
          {projectData?.length > 0 && (
            <ControlMenu
              className="control-menu"
              optionList={projectData?.map((project, index) => ({
                id: index,
                value: project.projectId,
                name: project.title,
              }))}
              onChange={(value) =>
                handleChangeProject({
                  target: { name: "projectId", value },
                })
              }
              value={formValues.projectId}
            />
          )}
        </div>

        <div className="member-grid-item">
          <h3>주제/분야</h3>
          <ControlMenu
            optionList={concepts.map((concept, index) => ({
              id: index,
              value: concept,
              name: concept,
            }))}
            onChange={(value) =>
              handleChange({ target: { name: "concept", value } })
            }
            value={formValues.concept || ""}
          />
        </div>

        <div className="member-grid-item">
          <h3>모집 기간</h3>
          <div className="member-grid-item-time">
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
        </div>

        <div className="member-grid-item">
          <h3>지역</h3>
          <ControlMenu
            optionList={location.map((place, index) => ({
              id: index,
              value: place,
              name: place,
            }))}
            onChange={(value) =>
              handleChange({ target: { name: "region", value } })
            }
            value={formValues.region || ""}
          />
        </div>
      </div>
    </Fragment>
  );
};
export default RecruitmentProjectWrite;
