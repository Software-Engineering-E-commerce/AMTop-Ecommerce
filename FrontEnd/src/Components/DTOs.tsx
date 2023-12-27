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

// The DTO of the WishlistElement recieved from the api
interface WishlistElement {
  id: number;
  productName: string;
  price: number;
  description: string;
  imageLink: string;
  token: string;
  discountPercentage: number;
}


// The DTO of the cart request needed to send requests to the api and include this object in the body
interface CartRequest {
  productId: number;
}

// The DTO of the wishlist request needed to send requests to the api and include this object in the body
interface WishlistRequest {
  productId: number;
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

interface ProductDTO {
  id: number;
  productName: string;
  price: number;
  postedDate: Date;
  description: string;
  productCountAvailable: number;
  brand: string;
  discountPercentage: number;
  category: string;
}


interface CategoryDTO {
  imageLink:string;
  categoryName:string;
}

interface HomeProductDTO{
  id: number;
  productName: string;
  price: number;
  imageLink: string;
  discountPercentage: number;
  reviews: Review[];
  inWishlist: boolean;
  description:string;
  productCountAvailable:number;
  categoryName:string;
  brand:string;
}