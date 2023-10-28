class HomeService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  async getPopularProjects() {
    const response = await this.httpClient.get("/auth/main/mostliked");

    return response.data;
  }

  async getImminentProjects() {
    const response = await this.httpClient.get("/auth/main/closedeadline");

    return response.data;
  }

  async getUpToDateProjects() {
    const response = await this.httpClient.get("/auth/main/uptodate");

    return response.data;
  }
}

export default HomeService;
