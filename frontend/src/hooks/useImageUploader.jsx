import { toast } from "react-toastify";
import { useGeneralClient } from "../context/Client/GeneralClientContext";

const useImageUploader = () => {
  const { uploadImage } = useGeneralClient();

  const handleImageUpload = async (files) => {
    if (!files) {
      toast.error("잘못된 접근입니다. 다시 이미지를 업로드해주세요.");
      return null;
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

  return { handleImageUpload };
};

export default useImageUploader;
