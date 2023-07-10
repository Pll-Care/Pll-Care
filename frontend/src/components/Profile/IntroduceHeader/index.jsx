import React, { useEffect, useState } from "react";
import profile_isProfile from "../../../assets/ranking-img.png";
import InfoMySelf from "./InfoMySelf";
import { useProfile } from "../../../context/ProfileContext";
import { getBio, putBio } from "../../../lib/apis/profileApi";

const Introduce = () => {
  const [profile, setProfile] = useState({
    name: "",
    nickName: "",
    imageUrl: "",
    bio: "",
  });
  const { isMyProfile, memberId } = useProfile();

  useEffect(() => {
    const getIntroduce = async () => {
      const response = await getBio(memberId);
      if (response) setProfile(response);
    };
    getIntroduce();
  }, [memberId]);

  const modifyBio = async (bio) => {
    const response = await putBio(memberId, bio);
    if (response) setProfile((prev) => ({ ...prev, bio }));
  };
  return (
    <div className="profile_introduce">
      <div className="profile_introduce_container">
        <div className="profile_introduce_image">
          <img
            src={profile.imageUrl === "" ? profile_isProfile : profile.imageUrl}
            alt="유저 프로필"
          />
        </div>
        <div className="profile_introduce_info">
          <div className="profile_introduce_info_name">
            <span className="profile_introduce_info_name_kr">
              {profile.name}
            </span>
            <span className="profile_introduce_info_name_us">
              {profile.nickName}
            </span>
          </div>
          <InfoMySelf
            bio={profile.bio}
            modifyBio={modifyBio}
            isMyProfile={isMyProfile}
          />
        </div>
      </div>
    </div>
  );
};

export default Introduce;
