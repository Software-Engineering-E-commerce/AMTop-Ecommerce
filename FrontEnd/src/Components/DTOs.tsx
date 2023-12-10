
//an OTD for the register r
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

//the OTD for the logIn request
interface LoginRequest {
    email:string;
    password:string;
}


//the OTD for the delete order request
interface DeleteOrderRequest {
    orderId: number;
}

//the OTD for the delete item request
interface DeleteItemRequest {
    orderId: number;
    productId: number;
}

//the OTD for the update order request
interface UpdateOrderRequest {
    orderId: number;
    newStatus: string;
}