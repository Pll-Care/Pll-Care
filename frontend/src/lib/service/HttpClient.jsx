import axios from "axios";

class HttpClient {
  constructor(baseURL, tokenRepository) {
    this.baseURL = baseURL;
    this.tokenRepository = tokenRepository;

    this.customAxios = axios.create({
      baseURL: this.baseURL,
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + this.tokenRepository.get("access"),
      },
    });
  }
}

export default HttpClient;
