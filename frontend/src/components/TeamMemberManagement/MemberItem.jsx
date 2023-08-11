import React from "react";
import profile_default from "../../assets/profile-default-img.png";
import EditTeamMember from "./EditTeamMember";

const MemberItem = ({
  name,
  memberId,
  position,
  imageUrl,
  isEdit,
  refetch,
}) => {
  return (
    <li className="member_item">
      <div className="member_item_imgbox">
        <div>
          <img
            src={imageUrl ? imageUrl : profile_default}
            alt={`${name}의 프로필`}
            className="member_item_imgbox_img"
          />
          {isEdit && (
            <EditTeamMember
              memberId={memberId}
              refetch={refetch}
              name={name}
              position={position}
            />
          )}
        </div>
      </div>
      <div className="member_item_infobox">
        <p className="member_item_infobox_name">{name}</p>
        <p className="member_item_infobox_job">{position}</p>
      </div>
    </li>
  );
};

export default React.memo(MemberItem);

/*
TODO: 팀 관리 부분은 프로젝트를 만든 사람이 아니면 못들어가게 만들어야 할거 같다
TODO: 프로젝트에 대한 권한이 없습니다 나오면 프로젝트 관리 페이지로 이동하도록 만들기
*/
