import axios from "axios";
import OrdersList from "../Components/OrdersList";
import { useLocation } from "react-router";

const Dashboard = () => {

  const location = useLocation();
  var {userToken, from} = location.state || {};

  const getOrders = async () => {
    console.log("In get orders");
    try {
      const response = await fetch(
        `http://localhost:9080/api/dash/getOrders`,
        {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
        }
      );

      console.log("In handling the response")
      console.log("the response is: ", response);
      console.log("body: ", response.body);
      const responseBody = await response.json();
      const orders: Order[] = responseBody;
      return orders;
    } catch (error) {
      console.log("Error:", error);
      const orders: Order[] = [];
      return orders;
    }
  };

  const deleteOrder = async (orderId: number) => {
    console.log("In delete");
    try {
      const response = await fetch(
        `http://localhost:9080/api/dash/deleteOrder`,
        {
          method: "POST",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
          body: JSON.stringify({ orderId })
        }
      );

      console.log("In handling the response")
      console.log("the response is: ", response);
      console.log("body: ", response.body);
      const responseBody = await response.json();
      return responseBody as string;
    } catch (error) {
      console.log("Error:", error);
      return "Connection Error";
    }
  };

  // const getOrderItems = async (orderId: number) => {
  //   console.log("In get order items");
  //   try {
  //     const response = await fetch(
  //       `http://localhost:9080/api/dash/getOrderItems`,
  //       {
  //         method: "GET",
  //         headers: {
  //           'Authorization': `Bearer ${userToken}`,
  //         },
  //         body: JSON.stringify({ orderId })
  //       }
  //     );

  //     console.log("In handling the response")
  //     console.log("the response is: ", response);
  //     console.log("body: ", response.body);
  //     const responseBody = await response.json();
  //     const orderItems: OrderItem[] = responseBody;
  //     return orderItems;
  //   } catch (error) {
  //     console.log("Error:", error);
  //     const orderItems: OrderItem[] = [];
  //     return orderItems;
  //   }
  // };

  const updateOrderStatus = async (orderId: number, newStatus: string) => {
    console.log("In update status");
    try {
      const response = await fetch(
        `http://localhost:9080/api/dash/updateOrderStatus`,
        {
          method: "POST",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
          body: JSON.stringify({ orderId, newStatus })
        }
      );

      console.log("In handling the response")
      console.log("the response is: ", response);
      console.log("body: ", response.body);
      const responseBody = await response.json();
      return responseBody as string;
    } catch (error) {
      console.log("Error:", error);
      return "Connection Error";
    }
  };

  return (
    <div>
      <OrdersList 
        getOrders = {getOrders}
        deleteOrder = {deleteOrder}
        updateOrderStatus = {updateOrderStatus}
      />
    </div>
  );
};

export default Dashboard;
