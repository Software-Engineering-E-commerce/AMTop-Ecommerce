import { useState, useEffect, useRef } from 'react';
import Pagination from './Pagination';
import { Modal, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import './OrdersList.css';
import AlertModal from './AlertModal';
import ConfirmationModal from './confirmationModal';
import { useNavigate } from 'react-router-dom';

interface Props {
  getOrders: () => Promise<Order[]>;
  deleteOrder: (orderId: number) => Promise<string>;
  deleteOrderItem: (order: Order, product: Product) => Promise<string>;
  updateOrderStatus: (orderId: number, newStatus: string) => Promise<string>;
  isAdmin: boolean;
  firstName: string;
  lastName: string;
  userToken: string;
}

const formatDate = (dateString: string): string => {
  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

const OrderList = ({
  getOrders,
  deleteOrder,
  deleteOrderItem,
  updateOrderStatus,
  isAdmin,
  firstName,
  lastName,
  userToken
}: Props) => {
  const nullCustomer: Customer = {
    id: 0,
    firstName: '',
    lastName: ''
  }
  const nullOrder: Order = {
    id: 0,
    startDate: '',
    deliveryDate: '',
    address: '',
    totalAmount: 0,
    totalCost: 0,
    status: '',
    customer: nullCustomer,
    orderItems: []
  };
  const [orders, setOrders] = useState<Order[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [ordersPerPage] = useState(9);
  const [showModal, setShowModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<Order>(nullOrder);
  const [fadeAnimation, setFadeAnimation] = useState('');
  const [fadeAnimationSingle, setFadeAnimationSingle] = useState('');
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [confirmModalContent, setConfirmModalContent] = useState({ message: '', onConfirm: () => {} });
  const [showAlertModal, setShowAlertModal] = useState(false);
  const [alertModalContent, setAlertModalContent] = useState({ message: '', onConfirm: () => {} });
  const navigate = useNavigate();

  const handleOrderClick = (order: Order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleStatusChange = (order: Order, newStatus: string) => {
    updateOrderStatus(order.id, newStatus).then(response => {
      if(response.includes("Status updated")) {
        selectedOrder.status = newStatus;
        setAlertModalContent({
          message: "Status updated successfully",
          onConfirm: () => {
            handleClose();
            setTimeout(() => {
              handleOrderClick(selectedOrder);
            }, 0);
          }
        });
      } else {
        setAlertModalContent({
          message: response,
          onConfirm: () => {
            handleClose();
            setTimeout(() => {
              handleOrderClick(selectedOrder);
            }, 0);
          }
        });
      }
    });
    setShowAlertModal(true);  
  };

  const handleRemoveProduct = (order: Order, product: Product) => {
    setConfirmModalContent({
      message: "Are you sure you want to delete this product? This action is irreversible.",
      onConfirm: () => {
        deleteOrderItem(order, product).then(response => {
          if(response.includes("Item deleted")) {
            setFadeAnimationSingle('fade-out');
            setTimeout(() => {
              selectedOrder.orderItems = selectedOrder.orderItems.filter(orderItem => orderItem.product.id !== product.id);
              handleClose();
              setTimeout(() => {
                handleOrderClick(selectedOrder);
              }, 0);
            }, 300);
            setTimeout(() => {
              setFadeAnimationSingle('fade-in');
            }, 300);
          } else {
            setAlertModalContent({
              message: response,
              onConfirm: () => {
                handleClose();
                setTimeout(() => {
                  handleOrderClick(selectedOrder);
                }, 0);
              }
            });
            setShowAlertModal(true);
          }
        });
      }
    });
    setShowConfirmModal(true);
  };

  const handleDeleteOrder = (orderId: number) => {
    setConfirmModalContent({
      message: "Are you sure you want to delete this order? This action is irreversible.",
      onConfirm: () => {
        deleteOrder(orderId).then(response => {
          if(response.includes("Order deleted")) {
            setFadeAnimation('fade-out');
            setTimeout(() => {
              setOrders(orders => orders.filter(order => order.id !== orderId));
              setFadeAnimation('fade-in');
            }, 300);
          } else {
            setAlertModalContent({
              message: response,
              onConfirm: () => {
                handleClose();
                setTimeout(() => {
                  handleOrderClick(selectedOrder);
                }, 0);
              }
            });
            setShowAlertModal(true);
          }
        });
      }
    });
    setShowConfirmModal(true);
  };

  const handleItemClick = (product: Product) => {
    const id = product.id;
    navigate("/product-details", {
      state: {
        firstName: firstName,
        lastName: lastName,
        isAdmin: isAdmin,
        productID: id,
        token: userToken,
      },
    });
  };

  // To not fetch orders twice on mounting the component
  const hasFetchedOrders = useRef(false);
  useEffect(() => {
    if (hasFetchedOrders.current) {
        return;
    }
    hasFetchedOrders.current = true;
    const fetchOrders = async () => {
      getOrders().then(orders => setOrders(orders));
    };
    fetchOrders();
  }, []);

  // Get current orders
  const indexOfLastOrder = currentPage * ordersPerPage;
  const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
  const currentOrders = orders.slice(indexOfFirstOrder, indexOfLastOrder);

  // Change page
  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);
  const handlePaginationClick = (pageNumber: number) => {
    setFadeAnimation('fade-out');
    setTimeout(() => {
      paginate(pageNumber);
      setFadeAnimation('fade-in');
    }, 300);
  };

  return (
    <div>
      {isAdmin && 
        <h1 className='mt-3 mb-0'>Orders</h1>
      }
      {!isAdmin && 
        <h1 className='mt-3 mb-0'>My Orders</h1>
      }
      {orders.length === 0 && !isAdmin &&
        <h1 className="semi-faded-text mb-0 d-flex justify-content-center align-items-center">
          Explore our products and place your first order today!
        </h1>
      }
      {orders.length === 0 && isAdmin &&
        <h1 className="semi-faded-text mb-0 d-flex justify-content-center align-items-center">
          No orders are currently present in the system
        </h1>
      }
      <div className={`orders-list ${fadeAnimation}`}>
        {currentOrders.map(order => (
          <div key={order.id} className='order-card' onClick={() => handleOrderClick(order)}>
            {isAdmin && 
              <>
                <p><strong>ID:</strong> {order.id}</p>
                <p><strong>Customer:</strong> {order.customer.firstName} {order.customer.lastName}</p>
              </>
            }
            <p><strong>Price:</strong> ${order.totalCost}</p>
            <p><strong>Status:</strong> {order.status}</p>
            <p><strong>Ordered at:</strong> {formatDate(order.startDate)}</p>
            <p><strong>Expected delivery at:</strong> {formatDate(order.deliveryDate)}</p>
            {isAdmin && 
              <button className="order-delete-button" onClick={(e) => { e.stopPropagation(); handleDeleteOrder(order.id); }}>
                <FontAwesomeIcon icon={faTrash} />
              </button>
            }
          </div>
        ))}
      </div>
      <footer>
        <Pagination
          itemsPerPage={ordersPerPage}
          totalItems={orders.length}
          paginate={handlePaginationClick}
          currentPage={currentPage}
        />
      </footer>
      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Order Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {isAdmin &&
            <>
              <p><strong>Customer's Name:</strong> {selectedOrder.customer.firstName} {selectedOrder.customer.lastName}</p>
              <p><strong>Customer's ID:</strong> {selectedOrder.customer.id}</p>
              <p>Modify the status or remove products from this order.</p>
            </>
          }
          <div className={`products-container ${fadeAnimationSingle}`}>
            <table className="order-items-table">
              <thead>
                <tr>
                  {isAdmin &&
                    <th>ID</th>
                  }
                  <th>Name</th>
                  <th>Cost</th>
                  <th>Quantity</th>
                </tr>
              </thead>
              <tbody>
                {selectedOrder?.orderItems.map(orderItem => (
                  <tr key={orderItem.product.id} onClick={() => handleItemClick(orderItem.product)}>
                    {isAdmin && 
                      <td>{orderItem.product.id}</td>
                    }
                    <td>{orderItem.product.productName}</td>
                    <td>{orderItem.originalCost} $</td>
                    <td>{orderItem.quantity}</td>
                    {isAdmin && 
                      <td>
                        <Button className='remove-btn' variant="danger" onClick={(e) => { e.stopPropagation(); handleRemoveProduct(selectedOrder, orderItem.product)}}>
                          Remove Product
                        </Button>
                      </td>
                    }
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className='status-btn-container'>
            <p>Status: <span className={`status ${selectedOrder?.status?.toLowerCase()}`}>{selectedOrder?.status}</span></p>
            {isAdmin && 
              <>
                <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Pending')}>Mark as Pending</Button>
                <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Shipped')}>Mark as Shipped</Button>
                <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Delivered')}>Mark as Delivered</Button>
              </>
            }
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
      <ConfirmationModal
        show={showConfirmModal}
        message={confirmModalContent.message}
        onConfirm={() => {
          confirmModalContent.onConfirm();
          setShowConfirmModal(false);
        }}
        onCancel={() => setShowConfirmModal(false)}
      />
      <AlertModal
        show={showAlertModal}
        message={alertModalContent.message}
        onConfirm={() => {
          alertModalContent.onConfirm();
          setShowAlertModal(false);
        }}
      />
    </div>
  );
};

export default OrderList;
