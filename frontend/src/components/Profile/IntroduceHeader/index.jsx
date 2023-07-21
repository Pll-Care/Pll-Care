import React, { useEffect, useState } from "react";
import profile_isProfile from "../../../assets/ranking-img.png";
import InfoMySelf from "./InfoMySelf";
import { useProfile } from "../../../context/ProfileContext";
import { getBio, putBio } from "../../../lib/apis/profileApi";
import Button from "../../common/Button";
import ModifyUserProfile from "./ModifyUserProfile";

const Introduce = () => {
  const [profile, setProfile] = useState({
    name: "",
    nickName: "",
    imageUrl: "",
    bio: "",
  });

  const [isModify, setIsModify] = useState(false);

  const { isMyProfile, memberId } = useProfile();

  useEffect(() => {
    const getIntroduce = async () => {
      const response = await getBio(memberId);
      console.log(response);
      if (response) setProfile(response);
    };
    getIntroduce();
  }, [memberId]);

  const modifyBio = async (bio) => {
    const response = await putBio(memberId, bio);
    if (response) setProfile((prev) => ({ ...prev, bio }));
  };

  const changeModify = () => {
    setIsModify((prev) => !prev);
  };

  return (
    <div className="profile_introduce">
      {isModify ? (
        <ModifyUserProfile
          imageUrl={profile.imageUrl}
          name={profile.name}
          nickName={profile.nickName}
          bio={profile.bio}
          isMyProfile={isMyProfile}
          changeModify={changeModify}
        />
      ) : (
        <UserProfile
          imageUrl={profile.imageUrl}
          name={profile.name}
          nickName={profile.nickName}
          bio={profile.bio}
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
        <img
          src={imageUrl === "" ? profile_isProfile : imageUrl}
          alt="유저 프로필"
        />
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
