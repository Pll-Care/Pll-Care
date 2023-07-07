import { useState } from "react";
import Button from "../../../common/Button";

const ContactBox = ({
  email = "https://www.naver.com",
  github = "https://www.naver.com",
  websiteUrl = "https://www.naver.com",
}) => {
  //TODO: API를 통해 contact data 받아오기
  const [isModify, setIsModify] = useState(false);
  const [userInfo, setUserInfo] = useState({
    email: email,
    github: github,
    websiteUrl: websiteUrl,
  });

  const submitModify = () => {
    console.log(userInfo);
    setIsModify(false);
  };

  const chageEmail = (email) => {
    setUserInfo((prev) => ({ ...prev, email: email }));
  };

  const chageGithub = (github) => {
    setUserInfo((prev) => ({ ...prev, github: github }));
  };
  const chageWebsite = (website) => {
    setUserInfo((prev) => ({ ...prev, websiteUrl: website }));
  };

  return (
    <div className="profile_body_introduce_Box">
      <div className="profile_body_introduce_Box_title">
        <h2>연락처</h2>
        <div className="profile_body_introduce_Box_title_btnBox">
          {isModify ? (
            <>
              <Button
                text="취소"
                size="small"
                onClick={() => setIsModify(false)}
              />
              <Button
                type="submit"
                text="완료"
                size="small"
                onClick={submitModify}
              />
            </>
          ) : (
            <Button
              text="수정"
              size="small"
              onClick={() => setIsModify(true)}
            />
          )}
        </div>
      </div>
      {isModify
        ? Modify_UI(chageEmail, chageGithub, chageWebsite)
        : Default_UI({
            email: userInfo.email,
            github: userInfo.github,
            websiteUrl: userInfo.websiteUrl,
          })}
    </div>
  );
};

export default ContactBox;

const Default_UI = ({ email, github, websiteUrl }) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Email</span>
        <a href={email} target="_blank" rel="noreferrer">
          {email}
        </a>
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Github</span>
        <a href={github} target="_blank" rel="noreferrer">
          {github}
        </a>
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Website</span>
        <a href={websiteUrl} target="_blank" rel="noreferrer">
          {websiteUrl}
        </a>
      </div>
    </div>
  );
};

const Modify_UI = (chageEmail, chageGithub, chageWebsite) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Email</span>
        <input
          type="text"
          placeholder="주소를 입력해주세요."
          name="email"
          onChange={(e) => chageEmail(e.target.value)}
        />
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Github</span>

        <input
          type="text"
          placeholder="주소를 입력해주세요."
          name="github"
          onChange={(e) => chageGithub(e.target.value)}
        />
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Website</span>

        <input
          type="text"
          placeholder="주소를 입력해주세요."
          name="website"
          onChange={(e) => chageWebsite(e.target.value)}
        />
      </div>
    </div>
  );
};
