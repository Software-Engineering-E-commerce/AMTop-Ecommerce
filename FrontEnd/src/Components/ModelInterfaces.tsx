// an interface for the Order entity
interface Order {
    id: number;
    startDate: string;
    deliveryDate: string;
    address: string;
    totalAmount: number;
    totalCost: number;
    status: string;
    customer: Customer;
    orderItems: OrderItem[];
}

// an interface for the Product entity
interface Product {
    id: number;
    productName: string;
    price: number;
    postedDate: Date;
    description: string;
    productCountAvailable: number;
    productSoldCount: number;
    brand: string;
    imageLink: string;
    discountPercentage: number;
    category: Category;
    reviews: Review[];
    inWishlist: boolean;
}

// an interface for the Order Item entity
interface OrderItem {
    product: Product;
    originalCost: number;
    quantity: number;
}

// an interface for the Order Item key
interface OrderItemPK {
    order: Order;
    product: Product;
}

// an interface for the Customer entity
interface Customer {
    id: number;
    firstName: string;
    lastName: string;
}

// an interface for the Category entity
interface Category {
    categoryName: string;
    imageLink: string;
}

// an interface for the Review entity
interface Review {
    customer: Customer;
    rating: number;
    comment: string;
    date: Date;
}