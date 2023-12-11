// An OTD for the register r
interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

// The OTD for the the authentication resonse
interface AuthenticationResponse {
  token: string;
  // Can be [{token: "SUCCESS tok"}, {token: "Already Exist"}, {token: errorMessage}]
}

// The OTD for the logIn request
interface LoginRequest {
  email: string;
  password: string;
}

// The DTO of the CartElemet recieved from the api
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


// The DTO of the cart request needed to send requests to the api and include this object in the body
interface CartRequest {
  productId: number;
}
