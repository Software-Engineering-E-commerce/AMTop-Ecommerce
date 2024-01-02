import React from "react";
import { useNavigate } from "react-router-dom";
import Form from "../Components/Form";
import axios from "axios";

const AdminVerfication = () => {
  var navigate = useNavigate();
  const { searchParams } = new URL(window.location.href);
  let userTok: string = searchParams.get("token")!;
  let email: string = searchParams.get("email")!;

  //function to direct the flow to sign up using credentials
  const getAdminSignUpCredentials = (admin: RegisterRequest) => {
    handleAdminSignupRequest(admin);
  };

  const handleAdminSignupRequest = async (admin: RegisterRequest) => {
    try {
      let url: string = "http://localhost:9080/api/adminVerification";
      const response = await axios.post(url, admin, {
        headers: {
          Authorization: `Bearer ${userTok}`,
          "Content-Type": "application/json",
        },
      });
      alert(response.data);
      navigate("/home", {
        state: { userToken: userTok, from: "admin signup" },
      });
    } catch (error) {
      //   Handle errors here
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          alert(axiosError.response.data as string);
          //   setResponseData(axiosError.response.data as string);
        } else if (axiosError.request) {
          // The request was made but no response was received
          console.error("No response received:", axiosError.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
  };

  return (
    <>
      <Form
        isLogin={false}
        isAdminSignup={true}
        adminEmail={email}
        getAdminSignUpCredentials={getAdminSignUpCredentials}
      />
    </>
  );
};

export default AdminVerfication;
