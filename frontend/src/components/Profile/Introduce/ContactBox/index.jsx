import { useEffect, useState } from "react";
import { useQuery } from "react-query";

import Button from "../../../common/Button";
import { getContact, patchProfile } from "../../../../lib/apis/profileApi";
import { useProfile } from "../../../../context/ProfileContext";
import { toast } from "react-toastify";
import ProfileInput from "../../../common/ProfileInput";

const QUERY_KEY = "profile-contact";

const ContactBox = () => {
  const [isModify, setIsModify] = useState(false);
  const [userInfo, setUserInfo] = useState({
    emailStart: "",
    emailEnd: "",
    github: "",
    websiteUrl: "",
  });

  const { isMyProfile, memberId } = useProfile();

  const { data, refetch } = useQuery(
    [QUERY_KEY, memberId],
    () => getContact(memberId),
    {
      onSuccess: (res) => {
        if (!!res.contact) {
          const [emailStart, emailEnd] = res.contact.email.split("@");
          const info = {
            emailStart,
            emailEnd,
            github: res.contact.github,
            websiteUrl: res.contact.websiteUrl,
          };
          setUserInfo((_) => info);
        }
      },
    }
  );
  useEffect(() => {
    refetch();
  }, [isModify, refetch]);

  const submitModify = async () => {
    console.log(userInfo);

    if (userInfo.emailStart === "") {
      return toast.error("이메일을 반드시 입력해야합니다.");
    }

    if (userInfo.emailEnd === "") {
      return toast.error("이메일 주소를 반드시 입력해야합니다.");
    }

    if (userInfo.github === "") {
      return toast.error("github 주소를 반드시 입력해야합니다.");
    }

    const info = {
      email: userInfo.emailStart + "@" + userInfo.emailEnd,
      github: userInfo.github,
      websiteUrl: userInfo.websiteUrl,
    };

    const reqBody = {
      contact: info,
    };

    await patchProfile(memberId, reqBody);
    setIsModify(false);
  };

  const changeEmailStart = (value) => {
    setUserInfo((prev) => ({ ...prev, emailStart: value }));
  };

  const changeEmailEnd = (value) => {
    setUserInfo((prev) => ({ ...prev, emailEnd: value }));
  };

  const chageGithub = (value) => {
    setUserInfo((prev) => ({ ...prev, github: value }));
  };
  const chageWebsite = (value) => {
    setUserInfo((prev) => ({ ...prev, websiteUrl: value }));
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
        ? Modify_UI({
            emailStart: userInfo.emailStart,
            emailEnd: userInfo.emailEnd,
            github: userInfo.github,
            websiteUrl: userInfo.websiteUrl,
            changeEmailStart: changeEmailStart,
            changeEmailEnd: changeEmailEnd,
            chageGithub: chageGithub,
            chageWebsite: chageWebsite,
          })
        : Default_UI({
            email: data?.contact?.email,
            github: data?.contact?.github,
            websiteUrl: data?.contact?.websiteUrl,
          })}
    </div>
  );
};

export default ContactBox;

const Default_UI = ({ email = "", github = "", websiteUrl = "" }) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Email
        </span>
        <p>{email}</p>
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Github
        </span>
        <a href={github} target="_blank" rel="noreferrer">
          {github}
        </a>
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Website
        </span>
        <a href={websiteUrl} target="_blank" rel="noreferrer">
          {websiteUrl}
        </a>
      </div>
    </div>
  );
};

const Modify_UI = ({
  emailStart,
  emailEnd,
  github,
  websiteUrl,
  changeEmailStart,
  changeEmailEnd,
  chageGithub,
  chageWebsite,
}) => {
  return (
    <div className="profile_body_introduce_contactBox_items">
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Email
        </span>
        <div className="profile_body_introduce_contactBox_items_email">
          <ProfileInput
            value={emailStart}
            onChange={changeEmailStart}
            placeholder="주소를 입력해주세요."
            width="45"
          />
          <span>@</span>
          <ProfileInput
            value={emailEnd}
            onChange={changeEmailEnd}
            placeholder="주소를 입력해주세요."
            width="45"
          />
        </div>
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Github
        </span>

        <ProfileInput
          value={github}
          onChange={chageGithub}
          placeholder="주소를 입력해주세요."
        />
      </div>
      <div className="profile_body_introduce_contactBox_items_item">
        <span className="profile_body_introduce_contactBox_items_title">
          Website
        </span>

        <ProfileInput
          value={websiteUrl}
          onChange={chageWebsite}
          placeholder="주소를 입력해주세요."
        />
      </div>
    </div>
  );
};
