import React, { ReactNode } from "react";
import { Modal, Button } from "react-bootstrap";

interface GenericAlertModalProps {
  show: boolean;
  onConfirm: () => void;
  body: ReactNode;
}

const GenericAlertModal = ({show,onConfirm,body}:GenericAlertModalProps) => {
  return (
    <>
      <Modal show={show} onHide={onConfirm}>
        <Modal.Body>{body}</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={onConfirm}>
            Ok
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default GenericAlertModal;
