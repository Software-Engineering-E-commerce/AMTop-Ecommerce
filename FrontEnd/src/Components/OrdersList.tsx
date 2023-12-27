import { useState, useEffect, useRef } from 'react';
import Pagination from './Pagination';
import { Modal, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import './OrdersList.css';
import AlertModal from './AlertModal';
import ConfirmationModal from './ConfirmationModal';
import '@fortawesome/fontawesome-free/css/all.css';

interface Props {
  getOrders: () => Promise<Order[]>;
  getSortedOrders: (sortBy: any, sortOrder: any) => Promise<Order[]>;
  getFilteredOrders: (filter: FilterOrderDto) => Promise<Order[]>;
  deleteOrder: (orderId: number) => Promise<string>;
  deleteOrderItem: (order: Order, product: Product) => Promise<string>;
  updateOrderStatus: (orderId: number, newStatus: string) => Promise<string>;
}

const OrderList = ({
  getOrders,
  getSortedOrders,
  getFilteredOrders,
  deleteOrder,
  deleteOrderItem,
  updateOrderStatus
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
  const [sortParams, setSortParams] = useState({ sortBy: '', sortOrder: true });
  const [showSortModal, setShowSortModal] = useState(false);
  const [filter, setFilter] = useState<FilterOrderDto>({
    id: 0,
    customerId: 0,
    fromPrice: 0,
    toPrice: 200000,
    status: ''
  });

  const handleOrderClick = (order: Order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleSortButtonClick = async() => {
    const orders = await getSortedOrders(sortParams.sortBy, sortParams.sortOrder);
    setOrders(orders);
  };

  const handleFilterButtonClick = async() => {
    const orders = await getFilteredOrders(filter);
    setOrders(orders);
  };

  const handleInputChange = (e: any) => {
    const { name, value } = e.target;
    setFilter(prevFilter => ({
      ...prevFilter,
      [name]: value
    }));
  };

  function updateMinPrice() {
    const firstElem = document.getElementById("minPriceValue");
    const secondElem = document.getElementById("minPrice") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
  }

  function updateMaxPrice() {
    const firstElem = document.getElementById("maxPriceValue");
    const secondElem = document.getElementById("maxPrice") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
  }

  const toggleSortModal = () => {
    setShowSortModal(prev => !prev);
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
      <h1 className='order-header'>Orders</h1>
      <button className="orders-sort-button" onClick={toggleSortModal}>
        Sort
      </button>
      <button className="btn btn-primary btn-filter" type="button" data-bs-toggle="offcanvas"
                  data-bs-target="#offcanvasScrolling" aria-controls="offcanvasScrolling">
        <i className="fas fa-filter"></i>
      </button>
      <div className="offcanvas offcanvas-start" data-bs-scroll="true" data-bs-backdrop="false"
       tabIndex={-1} id="offcanvasScrolling" aria-labelledby="offcanvasScrollingLabel">
        <div className="offcanvas-header">
          <h5 className="offcanvas-title" id="offcanvasScrollingLabel">Filter Options</h5>
            <button type="button" className="btn-close text-reset" data-bs-dismiss="offcanvas"
                  aria-label="Close"></button>
        </div>
        <div className="offcanvas-body">
          <label>Order ID:</label>
          <input type="number" name="id" className="form-control" value={filter.id} onChange={handleInputChange} />

          <label>Customer ID:</label>
          <input type="number" name="customerId" className="form-control" value={filter.customerId} onChange={handleInputChange} />

          <div className="mb-3">
            <label htmlFor="minPrice">Minimum Price: <span id="minPriceValue">0</span>$</label>
            <input type="range" className="form-range" min="0" max="200000" id="minPrice" onInput={updateMinPrice}
             name="fromPrice" value={filter.fromPrice} onChange={handleInputChange}/>
             <input type="number" name="fromPrice" className="form-control" value={filter.fromPrice} 
             onChange={handleInputChange} placeholder='Or, set the value manually..'/>
          </div>

          <div className="mb-3">
            <label htmlFor="maxPrice">Maximum Price: <span id="maxPriceValue">20000</span>$</label>
            <input type="range" className="form-range" min="0" max="200000" id="maxPrice" onInput={updateMaxPrice}
             name="toPrice" value={filter.toPrice} onChange={handleInputChange}/>
             <input type="number" name="toPrice" className="form-control" value={filter.toPrice} 
             onChange={handleInputChange} placeholder='Or, set the value manually..'/>
          </div>

          <label>Status:</label>
          <select name="status" className="form-control" value={filter.status} onChange={handleInputChange}>
            <option value="Pending">Pending</option>
            <option value="Shipped">Shipped</option>
            <option value="Delivered">Delivered</option>
          </select>

          <div className="d-flex justify-content-end mt-2">
            <Button variant="primary" onClick={() => handleFilterButtonClick()}>
              Filter
            </Button>
          </div>
        </div>
      </div>
      <div className={`orders-list ${fadeAnimation}`}>
        {currentOrders.map(order => (
          <div key={order.id} className='order-card' onClick={() => handleOrderClick(order)}>
            <p><strong>ID:</strong> {order.id}</p>
            <p><strong>Customer:</strong> {order.customer.firstName} {order.customer.lastName}</p>
            <p><strong>Price:</strong> ${order.totalCost}</p>
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
          <p><strong>Customer's Name:</strong> {selectedOrder.customer.firstName} {selectedOrder.customer.lastName}</p>
          <p><strong>Customer's ID:</strong> {selectedOrder.customer.id}</p>
          <p>Modify the status or remove products from this order.</p>
          <div className={`products-container ${fadeAnimationSingle}`}>
            <table className="order-items-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Cost</th>
                  <th>Quantity</th>
                </tr>
              </thead>
              <tbody>
                {selectedOrder?.orderItems.map(orderItem => (
                  <tr key={orderItem.product.id}>
                    <td>{orderItem.product.id}</td>
                    <td>{orderItem.product.productName}</td>
                    <td>{orderItem.originalCost} $</td>
                    <td>{orderItem.quantity}</td>
                    <td>
                      <Button className='remove-btn' variant="danger" onClick={() => handleRemoveProduct(selectedOrder, orderItem.product)}>
                        Remove Product
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
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
      {showSortModal && (
        <div className="sort-modal">
          <div className="sort-modal-content">
            <div className="sort-option">
              <label className="sort-label">
                Sort by:
                <select className="sort-select" onChange={(e) => setSortParams(prev => ({ ...prev, sortBy: e.target.value }))} value={sortParams.sortBy}>
                  <option value="">Select Criteria</option>
                  <option value="id">Order ID</option>
                  <option value="customer">Customer ID</option>
                  <option value="startDate">Order Date</option>
                  <option value="deliveryDate">Delivery Date</option>
                  <option value="totalAmount">Number Of Ordered Items</option>
                  <option value="totalCost">Total Cost</option>
                  <option value="status">Status</option>
                </select>
              </label>
            </div>
            <div className="sort-order">
              <label className="sort-label">
                <input type="radio" name="sortOrder" checked={sortParams.sortOrder} onChange={() => setSortParams(prev => ({ ...prev, sortOrder: true }))} />
                Ascending
              </label>
              <label className="sort-label">
                <input type="radio" name="sortOrder" checked={!sortParams.sortOrder} onChange={() => setSortParams(prev => ({ ...prev, sortOrder: false }))} />
                Descending
              </label>
            </div>
            <div className="sort-modal-buttons">
              <button className="apply-button" onClick={() => { handleSortButtonClick(); toggleSortModal(); }}>Apply</button>
              <button className="cancel-button" onClick={toggleSortModal}>Cancel</button>
            </div>
          </div>
        </div>      
      )}
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
