import axios from "axios";
import { getToken } from "../../utils/localstroageHandler";

class HttpClient {
  accessToken = getToken("access_token") ?? null;
  refreshToken = getToken("refresh_token") ?? null;
  BASE_URL = "https://fullcare.store/api";

  customAxios = axios.create({
    baseURL: this.BASE_URL,
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${this.accessToken}`,
    },
  });
}

export default HttpClient;
