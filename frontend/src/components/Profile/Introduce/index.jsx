import React from "react";
import profile_isProfile from "../../../assets/ranking-img.png";
import InfoMySelf from "./InfoMySelf";

const Introduce = () => {
  return (
    <section className="profile_introduce">
      <div className="profile_introduce_container">
        <div className="profile_introduce_image">
          <img src={profile_isProfile} alt="유저 프로필" />
        </div>
        <div className="profile_introduce_info">
          <div className="profile_introduce_info_name">
            <span className="profile_introduce_info_name_kr">김철수</span>
            <span className="profile_introduce_info_name_us">
              Kim, Cheol-Su
            </span>
          </div>
          <InfoMySelf />
        </div>
      </div>
    </section>
  );
};

export default Introduce;
