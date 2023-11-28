
//an interface to hold the customer's basic credentials
interface RegisterRequest {
    firstName:string;
    lastName:string;
    email:string;
    password:string;
}

//the OTD for the the authentication resonse
interface AuthenticationResponse {
    token:string;
    //can be [{token: "SUCCESS tok"}, {token: "Already Exist"}, {token: errorMessage}]
}








