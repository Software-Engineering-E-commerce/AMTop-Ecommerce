import { jwtDecode } from "jwt-decode";
import React, { useEffect } from "react";

interface Props {
  getUserData: (googleTok: string) => void;
}

const GoogleAuth = ({ getUserData }: Props) => {
  useEffect(() => {
    google.accounts.id.initialize({
      client_id:
        "128707485706-e32q9271ojecigc1atkget6m4r24cuuq.apps.googleusercontent.com",
      callback: handleCallBackResponse,
    });

    function handleCallBackResponse(response: any) {
      console.log(response.credential);
      // const userObject = jwtDecode(response.credential)
      getUserData(response.credential);
      // console.log("🚀 ~ file: App.tsx:15 ~ handleCallBackResponse ~ userObject:", userObject)
    }

    google.accounts.id.renderButton(document.getElementById("signInDiv"), {
      theme: "outline",
      size: "large",
    });
  }, []);

  return (
    <div id="signInDiv" className="col-12" style={{ width: "100%" }}></div>
  );
};

export default GoogleAuth;
