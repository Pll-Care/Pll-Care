import { useGeneralClient } from "../context/Client/GeneralClientContext";

export const useEditorImageUploader = (ref) => {
  const { uploadImage } = useGeneralClient();
  
  const handleImage = async (file) => {
    const range = ref.getEditor().getSelection(true);
    const reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onloadend = async () => {
      const formData = new FormData();
      formData.append("file", file);

      const imgUrl = await uploadImage({
        dir: "memo",
        formData: formData.get("file"),
      });

      ref.getEditor().insertEmbed(range.index, "image", imgUrl);
      ref.getEditor().setSelection(range.index + 1);
    };
  };

  if (!ref) {
    return;
  }

  const toolbar = ref.getEditor().getModule("toolbar");
  toolbar.addHandler("image", () => {
    const input = document.createElement("input");
    input.setAttribute("type", "file");
    input.setAttribute("accept", "image/*");
    input.click();

    input.onchange = () => {
      const file = input.files[0];
      handleImage(file);
    };
  });

  return handleImage;
};
