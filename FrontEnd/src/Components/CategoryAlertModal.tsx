import React, { ReactNode } from "react";
import { Modal, Button } from "react-bootstrap";

interface CategoryAlertModalProps {
  show: boolean;
  onClose: () => void;
  body: ReactNode;
}

const CategoryAlertModal = ({ show, onClose, body }: CategoryAlertModalProps) => {
  const handleClose = () => {
    onClose();
  };

  return (
    <>
      <Modal show={show} onHide={handleClose}>
        <Modal.Body>{body}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default CategoryAlertModal;