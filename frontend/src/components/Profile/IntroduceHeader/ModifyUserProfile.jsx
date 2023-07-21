import { useState } from "react";
import profile_isProfile from "../../../assets/ranking-img.png";
import Button from "../../common/Button";

const ModifyUserProfile = ({
  imageUrl = "",
  name = "",
  nickName = "",
  bio = "",
  isMyProfile = "",
  changeModify = "",
}) => {
  const [imageFile, setImageFile] = useState({
    imageUrl: imageUrl,
    useImageUrl: imageUrl,
  });

  const handleImageUpload = (event) => {
    const file = event.target.files[0];

    setImageFile({
      imageUrl: file,
      useImageUrl: URL.createObjectURL(file),
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const reqBody = {
      name: formData.get("name"),
      nickName: formData.get("nickName"),
      bio: formData.get("bio"),
      imageUlr: imageFile.imageUrl,
    };

    console.log(reqBody);
  };

  return (
    <div className="profile_introduce_container">
      <div>
        <div className="profile_introduce_image">
          <img
            src={imageUrl === "" ? profile_isProfile : imageFile.useImageUrl}
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
          <input
            className="profile_input"
            type="text"
            defaultValue={name}
            placeholder="이름을 입력해주세요."
            name="name"
          />
          <input
            className="profile_input"
            type="text"
            defaultValue={nickName}
            placeholder="닉네임을 입력해주세요."
            name="nickName"
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
