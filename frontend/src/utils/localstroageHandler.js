export const setToken = (key, value) => {
  localStorage.setItem(key, value);
};

export const getToken = (key) => {
  return localStorage.getItem(key);
};

export const isToken = (key) => {
  const value = localStorage.getItem(key);
  return !!value;
};
