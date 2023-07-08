import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

import Button from "../common/Button";
import Card from "../common/Card";
import { Link } from "react-router-dom";

const MemberRecruitmentWrite = () => {
  const array = ["파이썬", "자바", "자바스크립트"];
  return (
    <div className="member-write">
      <div className="member-title">
        <div className="member-title-text">
          <Link to="/recruitment">
            <ArrowBackIosNewIcon className="recruitment-direction" />
          </Link>
          <img
            src="https://cdn.pixabay.com/photo/2015/06/24/15/45/hands-820272_640.jpg"
            alt=""
          />
          <h2>New Project</h2>
        </div>

        <div className="member-title-button">
          <Button text="수정" size="small" />
          <Button text="삭제" size="small" />
        </div>
      </div>

      <Card className="member-write-card">
        <div className="member-content">
          <div className="member-content-project">
            <h3>프로젝트 선택</h3>
            <select className="member-select1">
              <option disabled selected hidden>
                선택하기
              </option>
              <option>사이드 프로젝트1</option>
              <option>사이드 프로젝트2</option>
            </select>
          </div>

          <div className="member-content-project">
            <h3>주제/분야</h3>
            <select className="member-select2">
              <option>주제1</option>
              <option>주제2</option>
            </select>
          </div>

          <div className="member-content-project">
            <h3>모집 기간</h3>
            <input
              className="member-select3"
              type="date"
              required
              //value={startDate}
              //onChange={handleChangeStartDate}
              data-placeholder="시작 일자"
            />
            ~
            <input
              type="date"
              required
              //value={endDate}
              //onChange={handleChangeEndDate}
              data-placeholder="종료 일자"
            />
          </div>

          <div className="member-content-options">
            <h3>지역</h3>
            <select className="member-select4">
              <option>주제1</option>
              <option>주제2</option>
            </select>
          </div>

          <div className="member-content-position">
            <h3>포지션</h3>
            <div className="member-content-position-container">
              <div className="member-content-position-container-option">
                <div className="member-stack">
                  <h5>백엔드</h5>
                  <input
                    className="position-number"
                    type="number"
                    min="0"
                    placeholder="0"
                  />
                </div>
                <div className="member-stackinput">
                  {array.map((stack) => (
                    <Button text={stack} size="small" />
                  ))}
                  <input placeholder="백엔드 기술 스택을 입력하세요" />
                </div>
              </div>

              <div className="member-content-position-container-option">
                <div className="member-stack">
                  <h5>프론트엔드</h5>
                  <input
                    className="position-number"
                    type="number"
                    min="0"
                    placeholder="0"
                  />
                </div>
                <div className="member-stackinput">
                  {array.map((stack) => (
                    <Button text={stack} size="small" />
                  ))}
                  <input placeholder="프론트 기술 스택을 입력하세요" />
                </div>
              </div>

              <div className="member-content-position-container-option">
                <div className="member-stack">
                  <h5>디자인</h5>
                  <input
                    className="position-number"
                    type="number"
                    min="0"
                    placeholder="0"
                  />
                </div>
                <div className="member-stackinput">
                  {array.map((stack) => (
                    <Button text={stack} size="small" />
                  ))}
                  <input placeholder="디자인 기술 스택을 입력하세요" />
                </div>
              </div>

              <div className="member-content-position-container-option">
                <div className="member-stack">
                  <h5>기획</h5>
                  <input
                    className="position-number"
                    type="number"
                    min="0"
                    placeholder="0"
                  />
                </div>
                <div className="member-stackinput">
                  {array.map((stack) => (
                    <Button text={stack} size="small" />
                  ))}
                  <input placeholder="기획 기술 스택을 입력하세요" />
                </div>
              </div>
            </div>
          </div>

          <div className="member-content-description">
            <h3>설명</h3>
            <textarea />
          </div>

          <div className="member-content-description">
            <h3>레퍼런스</h3>
            <textarea />
            <input className="member-file" placeholder="파일을 첨부하세요" />
          </div>

          <div className="member-content-description">
            <h3>컨택</h3>
            <textarea />
          </div>
        </div>
      </Card>
      <Button text="수정 완료" />
    </div>
  );
};
export default MemberRecruitmentWrite;
