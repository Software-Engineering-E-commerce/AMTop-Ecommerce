// an interface for the Order entity
interface Order {
    id: number;
    price: number;
    customerName: string;
    customerID: number;
    status: string;
    products: Product[];
}

// an interface for the Product entity
interface Product {
    id: number;
    price: number;
    name: string;
}