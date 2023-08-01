import React, { useEffect, useState } from "react";
import profile_isProfile from "../../../assets/profile-default-img.png";
import { useProfile } from "../../../context/ProfileContext";
import { getBio } from "../../../lib/apis/profileApi";
import Button from "../../common/Button";
import ModifyUserProfile from "./ModifyUserProfile";
import { useQuery } from "react-query";

const QUERY_KEY = "Introduce";

const Introduce = () => {
  const [isModify, setIsModify] = useState(false);

  const { isMyProfile, memberId } = useProfile();

  const { data: profile, refetch } = useQuery([QUERY_KEY, memberId], () =>
    getBio(memberId)
  );

  useEffect(() => {
    refetch();
  }, [isModify, refetch]);

  const changeModify = () => {
    setIsModify((prev) => !prev);
  };

  return (
    <div className="profile_introduce">
      {isModify ? (
        <ModifyUserProfile
          memberId={memberId}
          imageUrl={profile?.imageUrl}
          name={profile?.name}
          nickname={profile?.nickName}
          bio={profile?.bio}
          changeModify={changeModify}
        />
      ) : (
        <UserProfile
          imageUrl={profile?.imageUrl}
          name={profile?.name}
          nickName={profile?.nickName}
          bio={profile?.bio}
          isMyProfile={isMyProfile}
          changeModify={changeModify}
        />
      )}
    </div>
  );
};

export default Introduce;

const UserProfile = ({
  imageUrl,
  name,
  nickName,
  bio,
  isMyProfile,
  changeModify,
}) => {
  return (
    <div className="profile_introduce_container">
      <div className="profile_introduce_image">
        <img src={imageUrl ? imageUrl : profile_isProfile} alt="유저 프로필" />
      </div>
      <div className="profile_introduce_info">
        <div className="profile_introduce_info_name">
          <span className="profile_introduce_info_name_kr">{name}</span>
          <span className="profile_introduce_info_name_us">{nickName}</span>
        </div>
        <div className="profile_introduce_info_myself">
          <div className="profile_introduce_info_myself_bio">
            <span>{bio}</span>
          </div>
          {isMyProfile ? (
            <Button
              text="수정"
              size="small"
              type="profile"
              onClick={changeModify}
            />
          ) : null}
        </div>
      </div>
    </div>
  );
};
