import React, { ReactNode } from "react";
import { Button, Modal } from "react-bootstrap";

interface TwoButtonsModalProps {
  show: boolean;
  onClose?: () => void;
  resetResponseData?: () => void;
  body: ReactNode;
}

const TwoButtonsModal = ({
  show,
  onClose,
  resetResponseData,
  body,
}: TwoButtonsModalProps) => {
  const handleAll = () => {
    resetResponseData!();
    onClose!();
  };

  return (
    <>
      <Modal show={show} onHide={onClose!}>
        <Modal.Body>{body}</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={onClose!}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleAll}>
            Proceed
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default TwoButtonsModal;
