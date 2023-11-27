import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.css";
import SignUp from "./Pages/SignUp.tsx";
import Home from "./Pages/Home.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
  },
  {
    path: "/home",
    element: <Home />,
  },
  {
    path: "/signup",
    element: <SignUp/>,
  },
  {
    path: "*",
    element: <h1>404</h1>,
  },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
