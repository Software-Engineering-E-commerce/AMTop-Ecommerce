import React from 'react';
import './Pagination.css';

interface PaginationProps {
  itemsPerPage: number;
  totalItems: number;
  paginate: (pageNumber: number) => void;
  currentPage: number;
}

const Pagination: React.FC<PaginationProps> = ({ itemsPerPage, totalItems, paginate, currentPage }) => {
  const pageNumbers = [];
  const totalPages = Math.max(Math.ceil(totalItems / itemsPerPage), 1);
  const maxPageButtons = 5;
  let startPage, endPage;

  if (totalPages <= maxPageButtons) {
    // Less than 10 total pages so show all
    startPage = 1;
    endPage = totalPages;
  } else {
    // More than 10 total pages so calculate start and end pages
    if (currentPage <= 3) {
      startPage = 1;
      endPage = maxPageButtons;
    } else if (currentPage + 2 >= totalPages) {
      startPage = totalPages - 4;
      endPage = totalPages;
    } else {
      startPage = currentPage - 2;
      endPage = currentPage + 2;
    }
  }

  for (let i = startPage; i <= endPage; i++) {
    pageNumbers.push(i);
  }

  return (
    <nav>
      <ul className='pagination'>
        {(
          <li>
            <button onClick={() => paginate(currentPage - 1)} className='pagination-button' disabled = {currentPage === 1}>
              Previous
            </button>
          </li>
        )}
        {pageNumbers.map(number => (
          <li key={number} className='page-item'>
            <button onClick={() => paginate(number)} className={`pagination-button-num ${number === currentPage ? 'active' : ''}`} disabled = {number === currentPage}>
              {number}
            </button>
          </li>
        ))}
        {(
          <li>
            <button onClick={() => paginate(currentPage + 1)} className='pagination-button' disabled = {currentPage === totalPages}>
              Next
            </button>
          </li>
        )}
      </ul>
    </nav>
  );
};

export default Pagination;
