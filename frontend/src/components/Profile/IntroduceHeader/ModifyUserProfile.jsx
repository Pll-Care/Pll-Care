import { useState } from "react";
import profile_isProfile from "../../../assets/profile-default-img.png";
import Button from "../../common/Button";
import { putBioAPI } from "../../../lib/apis/profileApi";
import { uploadImage } from "../../../lib/apis/projectManagementApi";
import { toast } from "react-toastify";

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
    const formData = new FormData(event.target);

    if (!imageUrl && image.imageFile) {
      const imageReqBody = {
        dir: "profile",
        formData: image.imageFile,
      };

      const responseUploadImage = await uploadImage(imageReqBody);
      reqImageUrl.url = responseUploadImage;
    }

    const reqBody = {
      nickname: formData.get("nickname") || nickname,
      bio: formData.get("bio") || bio,
      imageUrl: !reqImageUrl.url ? imageUrl ?? "" : reqImageUrl.url,
    };

    if (
      reqBody.nickname === nickname &&
      reqBody.bio === bio &&
      reqBody.imageUrl === imageUrl
    ) {
      toast.error("프로필을 변경해야합니다.");
      return;
    }

    const responsePutBioAPI = await putBioAPI(memberId, reqBody);

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
          <input
            className="profile_input"
            type="text"
            defaultValue={nickname}
            placeholder="닉네임을 입력해주세요."
            name="nickname"
          />
        </div>
        <div className="profile_introduce_info_myself">
          <input
            className="profile_input"
            type="text"
            defaultValue={bio}
            placeholder="한 줄 자기소개를 입력해주세요."
            name="bio"
          />
          <Button
            text="취소"
            size="small"
            type="profile"
            onClick={changeModify}
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
