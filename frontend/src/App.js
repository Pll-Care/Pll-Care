import { RouterProvider } from "react-router-dom";
import React from "react";
import { routers } from "./router";
import { ReactQueryDevtools } from "react-query/devtools";
import { QueryClient, QueryClientProvider } from "react-query";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import "./scss/fullcare.css";

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={routers} />
      <ToastContainer autoClose={3000} position={"top-right"} hideProgressBar />
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
