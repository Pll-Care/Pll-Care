import { toast } from "react-toastify";

class GeneralService {
  #httpClient = null;

  constructor(httpClient) {
    this.#httpClient = httpClient;
  }

  getProfileAPI() {
    return this.#httpClient.get("/auth/member/image");
  }

  async uploadImage(imgData) {
    try {
      const response = await this.#httpClient.post(
        `/auth/upload/image?dir=${imgData.dir}`,
        {
          file: imgData.formData,
        },
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      return response.data.imageUrl;
    } catch (e) {
      if (e.response.data.code === "AWS_002") {
        toast.error("파일 업로드를 실패했습니다.");
      }
    }
  }

  async deleteImage(imgUrl) {
    try {
      const response = await this.#httpClient.delete(
        `/auth/upload/image?url=${imgUrl}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "AWS_001") {
        toast.error("파일을 찾을 수 없습니다.");
      }
    }
  }
}

export default GeneralService;
