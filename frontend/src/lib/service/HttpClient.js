class HttpClient {
  constructor(axiosInstance) {
    this.axiosInstance = axiosInstance;
  }

  get(url, options = {}) {
    return this.axiosInstance.http_instance.get(url, {
      ...options,
      headers: {
        ...options.headers,
      },
    });
  }

  post(url, reqBody, options = {}) {
    return this.axiosInstance.http_instance.post(url, reqBody, {
      ...options,
      headers: {
        ...options.headers,
      },
    });
  }

  put(url, reqBody, options = {}) {
    return this.axiosInstance.http_instance.put(url, reqBody, {
      ...options,
      headers: {
        ...options.headers,
      },
    });
  }

  patch(url, reqBody, options = {}) {
    return this.axiosInstance.http_instance.patch(url, reqBody, {
      ...options,
      headers: {
        ...options.headers,
      },
    });
  }
}

export default HttpClient;
