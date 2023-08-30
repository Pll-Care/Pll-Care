class LocalTokenRepository {
  #ACCESS_TOKEN_KEY = "access_token";
  #REFRESH_TOKEN_KEY = "refresh_token";

  save(token, key) {
    if (key === "access") {
      localStorage.setItem(this.#ACCESS_TOKEN_KEY, token);
    }
    if (key === "refresh") {
      localStorage.setItem(this.#REFRESH_TOKEN_KEY, token);
    }
  }

  get(key) {
    if (key === "access") {
      return localStorage.getItem(this.#ACCESS_TOKEN_KEY);
    }
    if (key === "refresh") {
      return localStorage.getItem(this.#REFRESH_TOKEN_KEY);
    }
  }

  remove(key) {
    if (key === "access") {
      localStorage.removeItem(this.#ACCESS_TOKEN_KEY);
    }
    if (key === "refresh") {
      localStorage.removeItem(this.#REFRESH_TOKEN_KEY);
    }
  }
}

export default LocalTokenRepository;
