import React, { ReactNode } from "react";
import { Modal, Button } from "react-bootstrap";

interface GenericAlertModalProps {
  show: boolean;
  onClose?: () => void;
  resetResponseData?: () => void;
  body: ReactNode;
}

const GenericAlertModal = ({show,onClose,resetResponseData,body}:GenericAlertModalProps) => {
  const handleClose = () =>{
    resetResponseData!();
    onClose!();
  }

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

export default GenericAlertModal;
