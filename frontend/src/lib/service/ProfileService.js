class ProfileService {
  httpClient = null;
  memberId = null;

  constructor(httpClient, memberId) {
    this.httpClient = httpClient;
    this.memberId = memberId;
  }

  getMemberIdAPI() {
    return this.httpClient.get("/auth/member/onlyid");
  }

  validateProfileAPI() {
    return this.httpClient.get(`/auth/profile/${this.memberId}/validate`);
  }

  getBioAPI() {
    return this.httpClient.get(`/auth/profile/${this.memberId}/bio`);
  }

  putBioAPI(reqBody) {
    return this.httpClient.put(`/auth/profile/${this.memberId}`, reqBody);
  }

  getContactAPI() {
    return this.httpClient.get(`/auth/profile/${this.memberId}/contact`);
  }

  patchProfileAPI(reqBody) {
    return this.httpClient.patch(`/auth/profile/${this.memberId}`, reqBody);
  }

  getPositionAPI() {
    return this.httpClient.get(`/auth/profile/${this.memberId}/roletechstack`);
  }

  searchTechAPI(tech) {
    return this.httpClient.get(`/auth/util/techstack?tech=${tech}`);
  }

  getProjectExperienceAPI() {
    return this.httpClient.get(`/auth/profile/${this.memberId}/experience`);
  }

  getEvaluationChartAPI() {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/evaluation/chart`
    );
  }

  getEvaluationProjectListAPI(page) {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/evaluation?page=${page}`
    );
  }

  getEvaluationProjectDetailAPI(projectId) {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/evaluation/${projectId}`
    );
  }

  getPostProjectAPI(state, page) {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/post?state=${state}&page=${page}`
    );
  }

  getApplyProjectAPI(state, page) {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/apply?state=${state}&page=${page}`
    );
  }

  getLikeProjectAPI(page) {
    return this.httpClient.get(
      `/auth/profile/${this.memberId}/post/like?page=${page}`
    );
  }
}

export default ProfileService;
