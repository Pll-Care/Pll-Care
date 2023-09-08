class ManagementService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  async getProjectList(pageNum = 1, state) {
    let response = null;

    try {
      if (state === "ALL") {
        response = await this.httpClient.get(
          `/auth/project/list?page=${pageNum}&size=4&state=ONGOING&state=COMPLETE`
        );
      } else {
        response = await this.httpClient.get(
          `/auth/project/list?page=${pageNum}&size=4&state=${state}`
        );
      }

      return {
        projectList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
      };
    } catch (e) {
      console.log(e);
    }
  }
}

export default ManagementService;
