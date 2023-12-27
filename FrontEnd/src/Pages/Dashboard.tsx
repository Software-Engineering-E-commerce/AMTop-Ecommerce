import axios from "axios";
import OrdersList from "../Components/OrdersList";
import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";

const Dashboard = () => {

  const location = useLocation();
  var {userToken, isAdmin, firstName, lastName} = location.state || {};

  const getOrders = async () => {
    console.log("In get orders");
    try {
      const response = await axios(
        `http://localhost:9080/api/dash/getOrders`,
        {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
        }
      );
      
      const orders: Order[] = response.data;
      console.log(orders)
      return orders;
    } catch (error) {
      console.log("Error:", error);
      const orders: Order[] = [];
      return orders;
    }
  };

  const getSortedOrders = async(sortBy: any, sortOrder: any) => {
    console.log("In get sorted orders");
    let url = `http://localhost:9080/api/sort/Order/${sortBy}/${sortOrder}`;
    try {
      const response = await axios(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      console.log(response.data);
      const orders: Order[] = response.data;
      return orders;
    } catch (error) {
      console.log("Error:", error);
      const orders: Order[] = [];
      return orders;
    }
  }

  const deleteOrder = async (orderId: number) => {
    const deleteOrderRequest: DeleteOrderRequest = {
      orderId: orderId
    }
    console.log("In delete");
    try {
      const response = await axios(
        `http://localhost:9080/api/dash/deleteOrder`,
        {
          method: "DELETE",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
          data: deleteOrderRequest
        }
      );
      return response.data;
    } catch (error) {
      console.log("Error:", error);
      return "Connection Error";
    }
  };

  const deleteOrderItem = async (order: Order, product: Product) => {
    const deleteItemRequest: DeleteItemRequest = {
      orderId: order.id,
      productId: product.id
    }
    console.log("In delete");
    try {
      const response = await axios(
        `http://localhost:9080/api/dash/deleteItem`,
        {
          method: "DELETE",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
          data: deleteItemRequest
        }
      );
      return response.data;
    } catch (error) {
      console.log("Error:", error);
      return "Connection Error";
    }
  };

  const updateOrderStatus = async (orderId: number, newStatus: string) => {
    const updateOrderRequest: UpdateOrderRequest = {
      orderId: orderId,
      newStatus: newStatus
    }
    console.log("In update status");
    try {
      const response = await axios(
        `http://localhost:9080/api/dash/updateOrderStatus`,
        {
          method: "POST",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
          data: updateOrderRequest
        }
      );
      return response.data;
    } catch (error) {
      console.log("Error:", error);
      return "Connection Error";
    }
  };

  return (
    <div>
      <Navbar 
          firstName = {firstName}
          lastName = {lastName}
          isAdmin = {isAdmin}
          token = {userToken}
      />
      <OrdersList 
        getOrders = {getOrders}
        getSortedOrders = {getSortedOrders}
        deleteOrder = {deleteOrder}
        deleteOrderItem= {deleteOrderItem}
        updateOrderStatus = {updateOrderStatus}
      />
    </div>
  );
};

export default Dashboard;
