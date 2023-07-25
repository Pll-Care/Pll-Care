import { toast } from "react-toastify";
import { uploadImage } from "../lib/apis/managementApi";

export const handleImageUploader = async (files) => {
  if (!files) {
    toast.error("잘못된 접근입니다. 다시 이미지를 업로드해주세요.");
    return;
  }

  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.readAsDataURL(files[0]);

    reader.onloadend = async () => {
      const formData = new FormData();
      formData.append("file", files[0]);

      const imgUrl = await uploadImage({
        dir: "project",
        formData: formData.get("file"),
      });

      resolve(imgUrl);
    };
  });
};
