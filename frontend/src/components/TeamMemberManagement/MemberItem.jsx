import React from "react";
import profile_default from "../../assets/profile-default-img.png";
import EditTeamMember from "./EditTeamMember";
import { Link } from "react-router-dom";

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
          <Link to={`/profile/${memberId}/introduce`}>
            <img
              src={imageUrl ? imageUrl : profile_default}
              alt={`${name}의 프로필`}
              className="member_item_imgbox_img"
            />
          </Link>
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
