import React from 'react';
import { Modal, Button } from 'react-bootstrap';

interface AlertModalProps {
  show: boolean;
  message: string;
  onConfirm: () => void;
}

const AlertModal: React.FC<AlertModalProps> = ({ show, message, onConfirm }) => (
  <Modal show={show} onHide={onConfirm}>
    <Modal.Body>{message}</Modal.Body>
    <Modal.Footer>
      <Button variant="primary" onClick={onConfirm}>
        Ok
      </Button>
    </Modal.Footer>
  </Modal>
);

export default AlertModal;