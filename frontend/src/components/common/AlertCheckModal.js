import Button from "./Button";
import ModalContainer from "./ModalContainer";

const AlertCheckModal = ({
  onClose,
  open,
  text,
  clickHandler,
}) => {
  return (
    <ModalContainer
      open={open}
      onClose={onClose}
      type="dark"
      width={500}
      height={300}
    >
      <div className="confirm-modal">
        <h1>{text}</h1>
        <div className="confirm-modal-button">
          <Button
            text="네"
            size={"small"}
            onClick={() => {
              clickHandler();
              onClose();
            }}
          />
          <Button text="아니오" onClick={() => onClose()} size={"small"} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default AlertCheckModal;
