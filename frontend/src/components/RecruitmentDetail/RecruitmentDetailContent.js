import { Avatar } from "@mui/material";

import Button from "../common/Button";

const RecruitmentDetailContent = () => {
  return (
    <div className="recruitment_detail">
      <div className="recruitment_detail_content">
        <h4>모집 작성자</h4>
        <div className="recruitment_detail_content_name">
          <Avatar />
          <h5>김철수</h5>
        </div>
      </div>

      <div className="recruitment_detail_content">
        <h4>모집 기간</h4>
        <h5>2023.4.5 ~ 2023.8.23</h5>
      </div>

      <div className="recruitment_detail_content_last">
        <div className="recruitment_detail_content_last_content">
          <h4>모집 위치</h4>
          <h5>서울</h5>
        </div>
      </div>

      <div className="recruitment_detail_container">
        <h4>포지션</h4>
        <div className="recruitment_detail_container_options">
          <div className="recruitment_detail_container_select">
            <h5>백엔드</h5>
            <h5>프론트 엔드</h5>
            <h5>디자인</h5>
            <h5>기획</h5>
          </div>
          <div className="recruitment_detail_container_options_content">
            <div className="recruitment_detail_container_member">
              <h5>⚪⚪⚪⚪</h5>
              <h5>⚪⚪⚪⚪</h5>
              <h5>⚪⚪⚪⚪</h5>
              <h5>⚪⚪</h5>
            </div>
            <div className="recruitment_detail_container_member">
              <h5>2/2</h5>
              <h5>2/2</h5>
              <h5>2/2</h5>
              <h5>2/2</h5>
            </div>
            <div className="recruitment_detail_container_button">
              <Button size="small" text="지원" />
              <Button size="small" text="지원" />
              <Button size="small" text="지원" />
              <Button size="small" text="지원" />
            </div>
          </div>
        </div>
      </div>

      <div className="recruitment_detail_description">
        <h4>설명</h4>
        <h5>----------</h5>
      </div>

      <div className="recruitment_detail_description">
        <h4>레퍼런스</h4>
        <h5>----------</h5>
      </div>

      <div className="recruitment_detail_description">
        <h4>컨택</h4>
        <h5>----------</h5>
      </div>
    </div>
  );
};
export default RecruitmentDetailContent;
