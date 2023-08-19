import React, { useEffect, useState } from "react";
import profile_isProfile from "../../../assets/profile-default-img.png";
import { useProfile } from "../../../context/ProfileContext";
import Button from "../../common/Button";
import ModifyUserProfile from "./ModifyUserProfile";
import { useQuery } from "react-query";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";

const QUERY_KEY = "Introduce";

const Introduce = () => {
  const [isModify, setIsModify] = useState(false);

  const { isMyProfile, memberId } = useProfile();
  const { getBioAPI } = useProfileClient();

  const { data: profile, refetch } = useQuery([memberId, QUERY_KEY], () =>
    getBioAPI()
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
          imageUrl={profile?.data?.imageUrl}
          name={profile?.data?.name}
          nickname={profile?.data?.nickName}
          bio={profile?.data?.bio}
          changeModify={changeModify}
        />
      ) : (
        <UserProfile
          imageUrl={profile?.data?.imageUrl}
          name={profile?.data?.name}
          nickName={profile?.data?.nickName}
          bio={profile?.data?.bio}
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
          <div>
            <span className="profile_introduce_info_name_real">{name}</span>
          </div>
          <div>
            <span className="profile_introduce_info_name_nick">{nickName}</span>
          </div>
        </div>
        <div className="profile_introduce_info_myself">
          <div className="profile_introduce_info_myself_bio">
            <span>{bio}</span>
          </div>
          {isMyProfile && (
            <Button
              text="수정"
              size="small"
              type="profile"
              onClick={changeModify}
            />
          )}
        </div>
      </div>
    </div>
  );
};
