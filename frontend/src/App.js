import { RouterProvider } from "react-router-dom";
import { routers } from "./router";
import { useDispatch } from "react-redux";
import { ReactQueryDevtools } from "react-query/devtools";
import { QueryClient, QueryClientProvider } from "react-query";
import { authActions } from "./redux/authSlice";
import { useEffect } from "react";

import "./scss/fullcare.css";

const queryClient = new QueryClient();

const App = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    const accessToken = localStorage.getItem("access_token");
    const refreshToken = localStorage.getItem("refresh_token");

    if (accessToken && refreshToken) {
      dispatch(authActions.login());
    }
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={routers} />
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
