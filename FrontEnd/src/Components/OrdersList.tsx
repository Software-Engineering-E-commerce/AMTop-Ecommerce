import { useState, useEffect } from 'react';
import Pagination from './Pagination';
import ConfirmationModal from './confirmationModal';
import { Modal, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import './OrdersList.css';

interface Props {
  getOrders: () => Promise<Order[]>;
  deleteOrder: (orderId: number) => Promise<string>;
  updateOrderStatus: (orderId: number, newStatus: string) => Promise<string>;
}

const OrderList = ({
  getOrders,
  deleteOrder,
  updateOrderStatus
}: Props) => {
  const nullOrder: Order = {
    id: 0,
    customerName: '',
    customerID: 0,
    price: 0,
    status: '',
    orderItems: []
  };
  const [response, setResponse] = useState('');
  const [orders, setOrders] = useState<Order[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [ordersPerPage] = useState(9);
  const [showModal, setShowModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<Order>(nullOrder);
  const [fadeAnimation, setFadeAnimation] = useState('');
  const [fadeAnimationSingle, setFadeAnimationSingle] = useState('');
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [confirmModalContent, setConfirmModalContent] = useState({ message: '', onConfirm: () => {} });

  // const dummyProducts: Product[] = [
  //   {
  //     id: 1,
  //     price: 20.99,
  //     name: 'Product 1'
  //   },
  //   {
  //     id: 2,
  //     price: 45.5,
  //     name: 'Product 2'
  //   },
  //   {
  //     id: 3,
  //     price: 30.75,
  //     name: 'Product 3'
  //   },
  //   {
  //     id: 4,
  //     price: 15.0,
  //     name: 'Product 4'
  //   },
  //   {
  //     id: 5,
  //     price: 158.0,
  //     name: 'Product 5'
  //   },
  //   {
  //     id: 6,
  //     price: 158.0,
  //     name: 'Product 5'
  //   },
  //   {
  //     id: 7,
  //     price: 158.0,
  //     name: 'Product 5'
  //   }
  // ];

  const handleOrderClick = (order: Order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleStatusChange = (order: Order, newStatus: string) => {
    updateOrderStatus(order.id, newStatus).then(response => setResponse(response));
    if(response === "Order Status Updated") {
      selectedOrder.status = newStatus;
      handleClose();
      setTimeout(() => {
        handleOrderClick(selectedOrder);
      }, 0);
    } else {
      alert(response);
    }
  };

  const handleRemoveProduct = (productId: number) => {
    setConfirmModalContent({
      message: "Are you sure you want to delete this product? This action is irreversible.",
      onConfirm: () => {
        setFadeAnimationSingle('fade-out');
        setTimeout(() => {
          selectedOrder.orderItems = selectedOrder.orderItems.filter(orderItem => orderItem.productId !== productId);
          handleClose();
          setTimeout(() => {
            handleOrderClick(selectedOrder);
          }, 0);
        }, 300);
        setTimeout(() => {
          setFadeAnimationSingle('fade-in');
        }, 300);
      }
    });
    setShowConfirmModal(true);
  };

  const handleDeleteOrder = (orderId: number) => {
    setConfirmModalContent({
      message: "Are you sure you want to delete this order? This action is irreversible.",
      onConfirm: () => {
        deleteOrder(orderId).then(response => setResponse(response));
        if(response === "Order Deleted") {
          setFadeAnimation('fade-out');
          setTimeout(() => {
            setOrders(orders => orders.filter(order => order.id !== orderId));
            setFadeAnimation('fade-in');
          }, 300);
        } else {
          alert(response);
        }
      }
    });
    setShowConfirmModal(true);
  };

  useEffect(() => {
    const fetchOrders = async () => {
      getOrders().then(orders => setOrders(orders));
      // const dummyOrders: Order[] = [
      //   {
      //     id: 1,
      //     price: 20.99,
      //     customerName: 'John Doe',
      //     customerID: 1,
      //     status: 'Pending',
      //     products: dummyProducts
      //   },
      //   {
      //     id: 2,
      //     price: 45.5,
      //     customerName: 'Jane Smith',
      //     customerID: 2,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   },
      //   {
      //     id: 3,
      //     price: 30.75,
      //     customerName: 'Bob Johnson',
      //     customerID: 3,
      //     status: 'Delivered',
      //     products: dummyProducts
      //   },
      //   {
      //     id: 4,
      //     price: 15.0,
      //     customerName: 'Alice Brown',
      //     customerID: 4,
      //     status: 'Pending',
      //     products: dummyProducts
      //   },
      //   {
      //     id: 5,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 6,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 7,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 8,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 9,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 10,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 11,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   }
      //   ,
      //   {
      //     id: 12,
      //     price: 55.25,
      //     customerName: 'Charlie White',
      //     customerID: 5,
      //     status: 'Shipped',
      //     products: dummyProducts
      //   } 
      // ];
      //setOrders(dummyOrders);
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
      <h1 className='order-header'>Orders</h1>
      <div className={`orders-list ${fadeAnimation}`}>
        {currentOrders.map(order => (
          <div key={order.id} className='order-card' onClick={() => handleOrderClick(order)}>
            <p><strong>ID:</strong> {order.id}</p>
            <p><strong>Customer:</strong> {order.customerName}</p>
            <p><strong>Price:</strong> ${order.price}</p>
            <p><strong>Status:</strong> {order.status}</p>
            <button className="order-delete-button" onClick={(e) => { e.stopPropagation(); handleDeleteOrder(order.id); }}>
              <FontAwesomeIcon icon={faTrash} />
            </button>
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
          <p><strong>Customer's Name:</strong> {selectedOrder.customerName}</p>
          <p><strong>Customer's ID:</strong> {selectedOrder.customerID}</p>
          <p>Modify the status or remove products from this order.</p>
          <div className={`products-container ${fadeAnimationSingle}`}>
            {selectedOrder?.orderItems.map(orderItem => (
              <div key={orderItem.productId} className='product-container'>
                <p>
                  {orderItem.quantity} {orderItem.originalCost}
                  <Button className='remove-btn' variant="danger" onClick={() => handleRemoveProduct(orderItem.productId)}>Remove Product</Button>
                </p>
              </div>
            ))}
          </div>
          <div className='status-btn-container'>
            <p>Status: <span className={`status ${selectedOrder?.status?.toLowerCase()}`}>{selectedOrder?.status}</span></p>
            <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Pending')}>Mark as Pending</Button>
            <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Shipped')}>Mark as Shipped</Button>
            <Button className='status-btn' onClick={() => handleStatusChange(selectedOrder, 'Delivered')}>Mark as Delivered</Button>
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
    </div>
  );
};

export default OrderList;
