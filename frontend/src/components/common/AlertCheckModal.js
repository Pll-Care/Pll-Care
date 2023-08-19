import { useMediaQuery } from "@mui/material";
import Button from "./Button";
import ModalContainer from "./ModalContainer";

const AlertCheckModal = ({ onClose, open, text, clickHandler }) => {
  const isMobile = useMediaQuery("(max-width: 767px)");

  return (
    <ModalContainer
      open={open}
      onClose={onClose}
      type="dark"
      width={isMobile ? 250 : 500}
      height={isMobile ? 270 : 300}
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
