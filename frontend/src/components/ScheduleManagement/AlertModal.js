import Button from "../common/Button";
import ModalContainer from "../common/ModalContainer";

const AlertModal = ({ onClose, open, text, clickHandler, width }) => {
  return (
    <ModalContainer open={open} onClose={onClose} type="dark" width={width}>
      <div className="confirm-modal">
        <h1>{text}</h1>
        <div className="confirm-modal-button">
          <Button text="네" onClick={clickHandler} />
          <Button text="아니오" onClick={() => onClose()} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default AlertModal;
