import { uploadImage } from "../lib/apis/managementApi";

export const useImageUploader = () => {
  const handleImage = async (file, quillRef) => {
    const range = quillRef.current.getEditor().getSelection(true);
    const reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onloadend = async () => {
      const formData = new FormData();
      formData.append("file", file);

      const imgUrl = await uploadImage({
        dir: "memo",
        formData: formData.get("file"),
      });

      quillRef.current.getEditor().insertEmbed(range.index, "image", imgUrl);
      quillRef.current.getEditor().setSelection(range.index + 1);
    };
  };

  return handleImage;
};
