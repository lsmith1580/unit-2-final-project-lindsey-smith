import Button from "./Button";
import "./ConfirmModal.css";

const ConfirmModal = ({ message, onConfirm, onCancel }) => {
  //destructuring
  return (
    <div className="confirm-modal">
      <div className="confirm-modal-content">
        <p>{message}</p> {/*message inside modal*/}
        <div className="button-group">
          <Button onClick={onConfirm} variant="danger">
            Yes, Delete
          </Button>
          <Button onClick={onCancel} variant="secondary">
            Cancel
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
