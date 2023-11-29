import axios, { Axios, AxiosResponse } from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { PulseLoader } from "react-spinners";

const Verification = () => {
  var navigate = useNavigate();

  const [verificationStatus, setVerificationStatus] = useState("loading");

  const { searchParams } = new URL(window.location.href);
  let userTok: string = searchParams.get("token")!;

  console.log("User token = ", userTok);

  useEffect(() => {
    const sendVerificationRequest = async () => {
      try {
        const response = await axios({
          method: "get",
          url: `http://localhost:9080/api/verification?token=${userTok}`,
          headers: {
            Authorization: `Bearer ${userTok}`,
          },
        });
        handleUserVerificationResponse(response);
      } catch (error) {
        console.error("Error:", error);
        alert(error);
      }
    };

    const handleUserVerificationResponse = (response: AxiosResponse) => {
      if (response.status == 200) {
        //means that the user is successfully verified
        setVerificationStatus("success");
        navigate("/home", { state: { userToken: userTok } });
      } else if (response.status == 403) {
        //means that the user already exists and this is a bad request
        //so we notify him and we'll let him in anyway
        alert("You already exist, so you'll be logged in");
        navigate("/home", { state: { userToken: userTok } });
      } else if (response.status == 404) {
        alert("User not found!");
        navigate("/signup");
      }
    };

    sendVerificationRequest();
  }, []); // Empty dependency array to run the effect once on mount

  return verificationStatus === "loading" ? (
    <div
      className="loadingDiv"
      style={{
        position: "fixed",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
      }}
    >
      <PulseLoader
        color={"black"}
        loading={verificationStatus === "loading"}
        size={30}
        aria-label="Loading Spinner"
        data-testid="loader"
      />
    </div>
  ) : (
    <h2>Verified</h2>
  );
};

export default Verification;
