class ScheduleService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  getOverviewAllSchedule = async (id) => {
    const res = await this.httpClient.get(
      `/auth/schedule/list?project_id=${id}`
    );
    return res.data;
  };

  getCalendarAllSchedule = async (id) => {
    const res = await this.httpClient.get(
      `/auth/schedule/calenderlist?project_id=${id}`
    );
    return res.data;
  };

  createNewSchedule = async (data) => {
    const res = await this.httpClient.post("/auth/schedule", data);
    return res;
  };

  getFilterSchedule = async (projectId, memberId, type, page) => {
    if (type === "all") {
      const res = await this.httpClient.get(
        `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}`
      );
      return res.data;
    }
    if (type === "pastAll") {
      const res = await this.httpClient.get(
        `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}&previous=true`
      );
      return res.data;
    }
    const res = await this.httpClient.get(
      `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}&scheduleCategory=${type}`
    );
    return res.data;
  };

  getTodayAfterSchedule = async (projectId) => {
    const res = await this.httpClient.get(
      `/auth/schedule/daily?project_id=${projectId}`
    );
    return res.data;
  };

  getDetailSchedule = async (projectId, scheduleId) => {
    const res = await this.httpClient.get(
      `/auth/schedule/${scheduleId}?project_id=${projectId}`
    );
    return res.data;
  };

  modifySchedule = async (data) => {
    const { scheduleId, ...formBody } = data;

    const res = await this.httpClient.put(
      `auth/schedule/${data.scheduleId}`,
      formBody
    );
    return res;
  };

  deleteSchedule = async (deleteBody) => {
    const res = await this.httpClient.delete(
      `/auth/schedule/${deleteBody.scheduleId}`,
      {
        data: { projectId: deleteBody.projectId },
      }
    );
    return res;
  };

  updateDoneSchedule = async (data) => {
    const { scheduleId, ...body } = data;

    const res = await this.httpClient.post(
      `/auth/schedule/${data.scheduleId}/state`,
      body
    );
    return res;
  };

  updateDoneShcedule = async (scheduleId, projectId) => {
    const body = {
      projectId: projectId,
      state: "TBD",
    };
    const res = await this.httpClient.post(
      `/auth/schedule/${scheduleId}/state`,
      body
    );
    return res;
  };

  makeNewMidEvaluation = async (data) => {
    const res = await this.httpClient.post("/auth/evaluation/midterm", data);
    return res;
  };

  getTeamMember = async (project_id) => {
    try {
      const response = await this.httpClient.get(
        `/auth/project/${project_id}/memberlist`
      );

      if (response.status === 200) return response.data;
    } catch (error) {
      console.log(error);
    }
  };
}
export default ScheduleService;
