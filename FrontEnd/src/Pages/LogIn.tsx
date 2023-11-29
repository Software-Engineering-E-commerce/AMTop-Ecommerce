import React from "react";
import Form from "../Components/Form";
import axios, { Axios, AxiosResponse } from "axios";
import { useNavigate } from "react-router-dom";
import BobUpWindow from "../Components/BobUpWindow";

const LogIn = () => {
  const navigate = useNavigate();

  //function to direct us to the login using credential handeling
  const getLogInCredentials = (customer: LoginRequest) => {
    handelLoginBasicCredentialsRequest(customer);
  };

  //--------------------------------------Handel log in using credentials----------------------------------
  const handelLoginBasicCredentialsRequest = async (customer: LoginRequest) => {
    //if success then the user will be added to the DB and then routed to his home page
    try {
      const response = await axios({
        method: "post",
        url: "http://localhost:9080/api/auth/authenticate", 
        data: customer,
      });
      console.log("Response: ", response);
      handelLoginBasicCredentialsResponse(response);
    } catch (error) {
      console.error("Error:", error);
      alert(error);
    }
  };

  const handelLoginBasicCredentialsResponse = (response: AxiosResponse) => {
    if (response.status == 200) {
      //then the user exists in out system then we want to navigate to the home page
      let userToken = response.data.token;
      navigate("/home", { state: { userToken: userToken } });

    } else if (response.status == 403) {
      //then the user doesn't exist and forbidden to log in so we need to notify him
      return (
        <>
          <BobUpWindow>
            <p style={{ color: "red" }}>
              User does NOT exist so you might wanna Sign up first !
            </p>
            <button
              type="button"
              className="btn btn-primary"
              style={{ marginLeft: 0 }}
              onClick={() => navigate("/signup")}
            >
              Sign up
            </button>
          </BobUpWindow>
        </>
      );
    }
  };

  //--------------------------------------End Handel log in using credentials----------------------------------


  return (
    <>
      <Form
        isLogin={true}
        getLogInCredentials={getLogInCredentials}
      />
    </>
  );

};

export default LogIn;