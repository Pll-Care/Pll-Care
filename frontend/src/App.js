import { RouterProvider } from "react-router-dom";
import React from "react";
import { routers } from "./router";
import { ReactQueryDevtools } from "react-query/devtools";
import { QueryClient, QueryClientProvider } from "react-query";

import { Loading } from "../src/components/common/Loading";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import "./scss/fullcare.css";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
    },
  },
});

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <React.Suspense fallback={<Loading />}>
        <RouterProvider router={routers} />
        <ToastContainer
          autoClose={3000}
          position={"top-right"}
          hideProgressBar
        />
      </React.Suspense>
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
