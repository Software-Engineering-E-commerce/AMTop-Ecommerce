// an interface for the Order entity
interface Order {
    id: number;
    price: number;
    customerName: string;
    customerID: number;
    status: string;
    orderItems: OrderItem[];
}

// an interface for the Product entity
interface Product {
    id: number;
    price: number;
    name: string;
}

// an interface for the Order Item entity
interface OrderItem {
    orderId: number;
    productId: number;
    originalCost: number;
    quantity: number;
}