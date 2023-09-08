import axios from "axios";

class AxiosInstance {
  #tokenUrl = "/auth/util/reissuetoken";
  #ACCESS_TOKEN = null;
  #REFRESH_TOKEN = null;

  constructor(baseURL, tokenRepository) {
    this.baseURL = baseURL;
    this.tokenRepository = tokenRepository;

    this.setToken(
      this.tokenRepository.get("access"),
      this.tokenRepository.get("refresh")
    );

    this.http_instance = axios.create({
      baseURL: this.baseURL,
      headers: {
        "Content-Type": "application/json",
      },
    });

    this.http_instance.interceptors.request.use(
      async (config) => {
        if (config.url === this.#tokenUrl) {
          config.headers.Authorization = null;
          config.headers.Authorization_refresh = `Bearer ${
            this.#REFRESH_TOKEN
          }`;
        } else {
          config.headers.Authorization = !!this.#ACCESS_TOKEN
            ? `Bearer ${this.#ACCESS_TOKEN}`
            : null;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    this.http_instance.interceptors.response.use(
      (response) => {
        return response;
      },
      async (error) => {
        const {
          config: originalRequest,
          response: { data },
        } = error;

        if (data.code === "JWT_002" && !originalRequest.sent) {
          originalRequest.sent = true;
          try {
            const response = await this.http_instance.get(this.#tokenUrl);
            if (response) {
              const { accessToken, refreshToken } = response.data;
              originalRequest.headers.Authorization = `Bearer ${accessToken}`;
              originalRequest.headers.Authorization_refresh = null;

              this.setToken(accessToken, refreshToken);

              this.tokenRepository.save(accessToken, "access");
              this.tokenRepository.save(refreshToken, "refresh");
            }
          } catch (_error) {
            const {
              response: { data },
            } = _error;

            if (data.code === "JWT_009") {
              this.tokenRepository.remove("access");
              this.tokenRepository.remove("refresh");
              window.location.href = "/";
            }
          }
          return this.http_instance(originalRequest);
        }
        return Promise.reject(error);
      }
    );
  }
  setToken(accessToken, refreshToken) {
    this.#ACCESS_TOKEN = accessToken;
    this.#REFRESH_TOKEN = refreshToken;
  }
}

export default AxiosInstance;
