import axios, { Axios, AxiosResponse } from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { PulseLoader } from "react-spinners";
import { Component } from "react";
import Loading from "../Components/Loading";

const Verification = () => {
  var navigate = useNavigate();

  const [verificationStatus, setVerificationStatus] = useState("loading");

  const { searchParams } = new URL(window.location.href);
  let userTok: string = searchParams.get("token")!;

  console.log("User token = ", userTok);

 
  // useRef to track whether the component is mounted
  const isMounted = useRef<boolean>(true);

  // useEffect runs on component mount
  useEffect(() => {
    if (isMounted.current) {
      // Your one-time initialization code here
      isMounted.current = false;

      const sendVerificationRequest = async () => {
        console.log("In request");
        try {
          const response = await fetch(
            `http://localhost:9080/api/verification`,
            {
              method: "GET",
              headers: {
                'Authorization': `Bearer ${userTok}`,
              },
            }
          );

          // console.log("In handling the response")
          console.log("the response is: ", response);
          if (response.status == 200) {
            //means that the user is successfully verified
            setVerificationStatus("success");
            navigate("/home", { state: { userToken: userTok, from:"Signed-up"}  });

          } else if (response.status == 400) {
            //means that the user already exists and this is a bad request
            //so we notify him and we'll let him in anyway
            alert("You already exist, so you'll be logged in");
            navigate("/home", { state: { userToken: userTok, from:"Signed-up"} });

          } else if (response.status == 404) {
            alert("User not found!");
            navigate("/signup");
          }
        } catch (error) {
          console.error("Error:", error);
          alert(error);
        }

      };

      sendVerificationRequest();


      // Set isMounted to false to prevent running the code on subsequent renders
    }

    // Cleanup function (optional)
    return () => {
      console.log("Component will unmount. Cleanup code here.");
      // This will run when the component is unmounted
    };
  }, []); // The empty dependency array ensures that the effect runs only once



  return verificationStatus === "loading" ? (
      <Loading isLoading={verificationStatus === "loading"}/>
  ) : (
    <h2>Verified</h2>
  );
};

export default Verification;
