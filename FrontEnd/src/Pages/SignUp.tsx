import axios from "axios";
import Form from "../Components/Form";
import { useLocation, useNavigate } from "react-router-dom";
import BobUpWindow from "../Components/BobUpWindow";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

const SignUp = () => {
  const navigate = useNavigate();

  //function to direct the flow to sign up using credentials
  const getSignUpCredentials = (customer: RegisterRequest) => {
    handelSignUpBasicCredentialsRequest(customer);
  };

  //function to direct the flow to the sinup using google oath
  const getSignUpGoogleTok = (googleTok: string) => {
    handelSignUpBasicGoogleRequest(googleTok);
  };

  //-------------------------handeling sign up using basic credentials-----------------------------------------
  const handelSignUpBasicCredentialsRequest = async (
    customer: RegisterRequest
  ) => {
    //if success then the user will be added to the DB and then routed to his home page
    try{
      const response:AuthenticationResponse = await axios({
        method: "post",
        url: "http://localhost:9080/api/auth/registerC", //TODO check the local host port of the server
        data: customer,
      })
      handelSignUpBasicCredentialsResponse(response);
    } catch (error) {
      console.error("Error:", error);
      alert(error);
    }
  };

  const handelSignUpBasicCredentialsResponse = (response: AuthenticationResponse) => {
    let userTok = response.token;

    //here the userTok is "SUCCESS + token"
    if (userTok.includes("SUCCESS")) {
      //TODO still we need to extract the user token from the string
      return (
        <>
          <BobUpWindow>
            <p>
              <FontAwesomeIcon
                icon={faCircleCheck}
                style={{
                  color: "green",
                  fontSize: "18px",
                  marginRight: "10px",
                }}
              />
              We've sent a verification email to you, please check it out to be
              able to log in.
            </p>
          </BobUpWindow>
        </>
      );
    
    //but here it is "Already Exist"  
    } else if (userTok === "Already Exist") {
      return (
        <>
          <BobUpWindow>
            <p style={{ color: "black" }}>
              The email address you provided already exists so you might wanna
              log in
            </p>
            <button
              type="button"
              className="btn btn-primary"
              style={{ marginLeft: 0 }}
              onClick={() =>
                //TODO navigate to the sign in
                navigate("/login")
              }
            >
              Log in
            </button>
          </BobUpWindow>
        </>
      );
    } 
  };

  //-------------------------end of handeling sign up using basic credentials-----------------------------------------

  //-------------------------Handling signUp using Google oath-----------------------------------------------------
  const handelSignUpBasicGoogleRequest = async (googleTok: string) => {
    //TODO handel google requests once the back is done
  };

  const handelSignUpBasicGoogleResponse =  () => {
    //TODO handel the google response once back is done
  }
  //-------------------------end of Handling signUp using Google oath-----------------------------------------------------
  
  return (
    <>
      <Form
        isLogin={false}
        getSignUpCredentials={getSignUpCredentials}
        getSignUpGoogleTok={getSignUpGoogleTok}
      />
      {/* {handelSignUpBasicCredentialsResponse({token:"Already Exist"})} */}
    </>
  );
};

export default SignUp;
