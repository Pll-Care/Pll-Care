import { useNavigate } from "react-router-dom";

export const useRouter = () => {
  const router = useNavigate();

  return {
    currentPath: window.location.pathname,
    routeTo: (path) => router(path),
    replaceTo: (path) => router(path, { replace: true }),
    haveDataTo: (path, data) => router(path, { state: data }),
  };
};
