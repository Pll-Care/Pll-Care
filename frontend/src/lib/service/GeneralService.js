class GeneralService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  getProfileAPI() {
    return this.httpClient.customAxios.get("/auth/member/image");
  }
}

export default GeneralService;
