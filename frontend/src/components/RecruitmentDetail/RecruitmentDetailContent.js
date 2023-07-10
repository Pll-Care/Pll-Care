import { useParams } from "react-router";

import { Avatar } from "@mui/material";

import Button from "../common/Button";
import RecruitmentDetailTitle from "./RecruitmentDetailTitle";
import { useQuery } from "react-query";
import { getRecruitmentPostDetail } from "../../lib/apis/memberRecruitmentApi";

const RecruitmentDetailContent = () => {
  const { id } = useParams();
  const { data } = useQuery(["recruitmentDetail"], () =>
    getRecruitmentPostDetail(id)
  );
  console.log(data);
  return (
    <>
      <RecruitmentDetailTitle
        title={data?.title}
        writeTime={data?.createdDate}
        views="---"
      />
      <div className="recruitment_detail">
        <div className="recruitment_detail_content">
          <h4>모집 작성자</h4>
          <div className="recruitment_detail_content_name">
            <Avatar />
            <h5>{data?.memberName}</h5>
          </div>
        </div>

        <div className="recruitment_detail_content">
          <h4>모집 기간</h4>
          <h5>2023.4.5 ~ 2023.8.23---</h5>
        </div>

        <div className="recruitment_detail_content_last">
          <div className="recruitment_detail_content_last_content">
            <h4>모집 위치</h4>
            <h5>{data?.region}</h5>
          </div>
        </div>

        <div className="recruitment_detail_container">
          <h4>포지션</h4>
          <div className="recruitment_detail_container_options">
            <div className="recruitment_detail_container_select">
              <h5>백엔드</h5>
              <h5>프론트 엔드</h5>
              <h5>기획</h5>
              <h5>디자인</h5>
            </div>
            <div className="recruitment_detail_container_options_content">
              <div className="recruitment_detail_container_member">
                <h5>⚪⚪⚪⚪</h5>
                <h5>⚪⚪⚪⚪</h5>
                <h5>⚪⚪⚪⚪</h5>
                <h5>⚪⚪</h5>
              </div>
              <div className="recruitment_detail_container_member">
                {data?.recruitInfoList.map((info, index) => (
                  <h5 key={index}>{info?.cnt}</h5>
                ))}
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
          <h5>프로젝트 이름: {data?.projectName}</h5>
          <h5>{data?.description}</h5>
        </div>

        <div className="recruitment_detail_description">
          <h4>레퍼런스</h4>
          <h5>{data?.reference}</h5>
        </div>

        <div className="recruitment_detail_description">
          <h4>컨택</h4>
          <h5>{data?.contact}</h5>
        </div>
      </div>
    </>
  );
};
export default RecruitmentDetailContent;
