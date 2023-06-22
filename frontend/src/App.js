import { RouterProvider } from "react-router-dom";
import { routers } from "./router";
import { ReactQueryDevtools } from "react-query/devtools";
import { QueryClient, QueryClientProvider } from "react-query";

import "./scss/fullcare.css";

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={routers} />
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export default App;
