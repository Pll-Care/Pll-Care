import { useEffect, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "react-query";

import Button from "../../../common/Button";
import { getContact, patchProfile } from "../../../../lib/apis/profileApi";
import { useProfile } from "../../../../context/ProfileContext";
import { toast } from "react-toastify";

const QUERY = {
  KEY: "profile-contact",
};
// const QUERY_KEY : "profile-contact";
// const QUERY_FN : (memberId) => getContact(memberId);

const ContactBox = () => {
  const [isModify, setIsModify] = useState(false);
  const [userInfo, setUserInfo] = useState({
    email: "",
    github: "",
    websiteUrl: "",
  });

  const { isMyProfile, memberId } = useProfile();

  useEffect(() => {
    const getUserContact = async () => {
      const { contact } = await getContact(memberId);
      if (contact) setUserInfo(contact);
    };

    getUserContact();
  }, [memberId, isModify]);

  const submitModify = async () => {
    if (userInfo.email === "") {
      return toast.error("이메일을 반드시 입력해야합니다.");
    }

    if (userInfo.github === "") {
      return toast.error("github 주소를 반드시 입력해야합니다.");
    }

    if (userInfo.websiteUrl === "") {
      return toast.error("웹사이트 주소를 반드시 입력해야합니다.");
    }

    await patchProfile(memberId, { contact: userInfo });
    setIsModify(false);
  };

  const changeEmail = (email) => {
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
          {isMyProfile ? (
            isModify ? (
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
            )
          ) : null}
        </div>
      </div>
      {isModify
        ? Modify_UI(changeEmail, chageGithub, chageWebsite)
        : Default_UI({
            email: userInfo.email,
            github: userInfo.github,
            websiteUrl: userInfo.websiteUrl,
          })}
    </div>
  );
};

export default ContactBox;

const Default_UI = ({ email = "", github = "", websiteUrl = "" }) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Email</span>
        <p>{email}</p>
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

const Modify_UI = (changeEmail, chageGithub, chageWebsite) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span>Email</span>
        <input
          type="text"
          placeholder="주소를 입력해주세요."
          name="email"
          onChange={(e) => changeEmail(e.target.value)}
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
