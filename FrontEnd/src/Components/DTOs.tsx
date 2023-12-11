//an OTD for the register r
interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

//the OTD for the the authentication resonse
interface AuthenticationResponse {
  token: string;
  //can be [{token: "SUCCESS tok"}, {token: "Already Exist"}, {token: errorMessage}]
}

//the OTD for the logIn request
interface LoginRequest {
  email: string;
  password: string;
}

//the DTO of the CartElemet recieved from the api
interface CartElement {
  id: number;
  productName: string;
  price: number;
  description: string;
  imageLink: string;
  quantity: number;
  token: string;
  discountPercentage: number;
}


//the DTO of the cart request needed to send requests to the api and include this object in the body
interface CartRequest {
  productId: number;
}
