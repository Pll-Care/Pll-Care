import { useState } from "react";
import profile_isProfile from "../../../assets/profile-default-img.png";
import Button from "../../common/Button";
import { uploadImage } from "../../../lib/apis/projectManagementApi";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";
import ProfileInput from "../../common/ProfileInput";

const ModifyUserProfile = ({
  memberId,
  imageUrl,
  name,
  nickname,
  bio,
  changeModify,
}) => {
  const [image, setImage] = useState({
    imageFile: null,
    useImageUrl: imageUrl,
  });

  const [userInfo, setUserInfo] = useState({ nickname: nickname, bio: bio });
  const { putBioAPI } = useProfileClient();
  const reqImageUrl = { url: "" };

  const handleImageUpload = (event) => {
    const file = event.target.files[0];

    setImage({
      imageFile: file,
      useImageUrl: URL.createObjectURL(file),
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (image.imageFile) {
      const imageReqBody = {
        dir: "profile",
        formData: image.imageFile,
      };

      const responseUploadImage = await uploadImage(imageReqBody);
      reqImageUrl.url = responseUploadImage;
    }

    const reqBody = {
      nickname: userInfo.nickname,
      bio: userInfo.bio,
      imageUrl: !reqImageUrl.url ? imageUrl ?? "" : reqImageUrl.url,
    };

    const responsePutBioAPI = await putBioAPI(reqBody);

    if (responsePutBioAPI?.status === 200) changeModify();
  };

  return (
    <div className="profile_introduce_container">
      <div>
        <div className="profile_introduce_image">
          <img
            src={image.useImageUrl ? image.useImageUrl : profile_isProfile}
            alt="유저 프로필"
          />
        </div>

        <div className="profile_introduce_image_change">
          <label htmlFor="input-file" style={{ cursor: "pointer" }}>
            프로필 변경
          </label>
          <input
            type="file"
            id="input-file"
            onChange={handleImageUpload}
            style={{ display: "none" }}
          />
        </div>
      </div>
      <form className="profile_introduce_info" onSubmit={handleSubmit}>
        <div className="profile_introduce_info_name">
          <div className="profile_introduce_info_name_kr">{name}</div>

          <ProfileInput
            value={userInfo.nickname}
            placeholder={"닉네임을 입력해주세요."}
            onChange={(value) =>
              setUserInfo((prev) => ({ ...prev, nickname: value }))
            }
            width="50"
            position="header"
          />
        </div>
        <div className="profile_introduce_info_myself">
          <ProfileInput
            value={userInfo.bio}
            placeholder={"한 줄 자기소개를 입력해주세요."}
            onChange={(value) =>
              setUserInfo((prev) => ({ ...prev, bio: value }))
            }
            position="header"
          />

          <Button
            text="완료"
            size="small"
            type="profile"
            buttonType="onSubmit"
          />
        </div>
      </form>
    </div>
  );
};

export default ModifyUserProfile;
