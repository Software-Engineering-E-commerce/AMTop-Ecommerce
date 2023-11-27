
import { jwtDecode } from 'jwt-decode';
import React, { useEffect } from 'react'

interface Props{
  getUserData: (firstName:string, lastName:string, email:string) => void
}

const GoogleAuth = ({getUserData}:Props) => {

  function handleCallBackResponse(response: any) {
    console.log("Hi thereeeeeeeeeeeeeeeeeeeeeeee")
    console.log(response.credential);
    const userObject = jwtDecode(response.credential)
    getUserData(userObject.given_name, userObject.family_name, userObject.email)
    console.log("🚀 ~ file: App.tsx:15 ~ handleCallBackResponse ~ userObject:", userObject)
  }
  useEffect(() => {
    google.accounts.id.initialize({
      client_id:
        "128707485706-e32q9271ojecigc1atkget6m4r24cuuq.apps.googleusercontent.com",
      callback: handleCallBackResponse,
    });

    google.accounts.id.renderButton(document.getElementById("signInDiv"), {
      theme: "outline",
      size: "large",
    });
  }, []);

  return (
    <div id="signInDiv" className='col-12' style={{width:"100%"}}></div>
  )
}

export default GoogleAuth