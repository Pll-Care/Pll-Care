class ProfileService {
  httpClient = null;
  memberId = null;

  constructor(httpClient, memberId) {
    this.httpClient = httpClient;
    this.memberId = memberId;
  }

  getMemberIdAPI() {
    return this.httpClient.customAxios.get("/auth/member/onlyid");
  }

  validateProfileAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/validate`
    );
  }

  getBioAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/bio`
    );
  }

  putBioAPI(reqBody) {
    return this.httpClient.customAxios.put(
      `/auth/profile/${this.memberId}`,
      reqBody
    );
  }

  getContactAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/contact`
    );
  }

  patchProfileAPI(userInfo) {
    return this.httpClient.customAxios.patch(
      `/auth/profile/${this.memberId}`,
      userInfo
    );
  }

  getPositionAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/roletechstack`
    );
  }

  searchTechAPI(tech) {
    return this.httpClient.customAxios.get(`/auth/util/techstack?tech=${tech}`);
  }

  getProjectExperienceAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/experience`
    );
  }

  getEvaluationChartAPI() {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/evaluation/chart`
    );
  }

  getEvaluationProjectListAPI(page) {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/evaluation?page=${page}`
    );
  }

  getEvaluationProjectDetailAPI(projectId) {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/evaluation/${projectId}`
    );
  }

  getPostProjectAPI(state, page) {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/post?state=${state}&page=${page}`
    );
  }

  getApplyProjectAPI(state, page) {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/apply?state=${state}&page=${page}`
    );
  }

  getLikeProjectAPI(page) {
    return this.httpClient.customAxios.get(
      `/auth/profile/${this.memberId}/post/like?page=${page}`
    );
  }
}

export default ProfileService;
