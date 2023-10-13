class RecruitmentService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  getAllRecruitmentPost = async (page, size) => {
    try {
      const res = await this.httpClient.get(
        `/auth/post/list?page=${page}&size=${size}&direction=ASC`
      );
      return res.data;
    } catch (err) {
      return err;
    }
  };

  getRecruitmentPostDetail = async (postId) => {
    const res = await this.httpClient.get(`/auth/post/${postId}`);
    return res.data;
  };

  addLikeRecruitmentPost = async (postId) => {
    try {
      const res = await this.httpClient.post(`/auth/post/${postId}/like`);
      return res.data;
    } catch (err) {
      return err;
    }
  };

  getRecruitmentProject = async () => {
    const res = await this.httpClient.get("/auth/project/simplelist");
    return res.data.data;
  };

  addRecruitmentPost = async (body) => {
    const res = await this.httpClient.post("/auth/post", body);
    return res.data;
  };

  modifyRecruitmentPost = async (body) => {
    const { postId, ...formData } = body;
    const res = await this.httpClient.put(`/auth/post/${postId}`, formData);
    return res.data;
  };

  deleteRecruitmentPost = async (postId) => {
    const res = await this.httpClient.delete(`/auth/post/${postId}`);
    return res.data;
  };

  applyRecruitmentPost = async (body) => {
    const { postId, position } = body;
    const res = await this.httpClient.post(`/auth/post/${postId}/apply`, {
      position: position,
    });
    return res;
  };

  applyCancelRecruitmentPost = async (postId) => {
    const res = await this.httpClient.post(`/auth/post/${postId}/applycancel`);
    return res;
  };
}
export default RecruitmentService;
