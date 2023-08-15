/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import { useRouter } from "../hooks/useRouter";

const PageRouterLayout = ({ children, pageUrl, passUrl }) => {
  const { routeOptionTo, currentPath } = useRouter();

  useEffect(() => {
    if (currentPath === passUrl) {
      routeOptionTo(`${pageUrl}`, { replace: true });
    }
  }, [pageUrl]);
  return <>{children}</>;
};

export default PageRouterLayout;
