import { Fragment } from "react";
import ReactDOM from "react-dom";

const Backdrop = (props) => {
  return <div className="modal-backdrop" onClick={props.onClose}></div>;
};
const Overlay = ({ children, modalType, modalSize }) => {
  const size = modalSize === "big" ? "modal_big" : "modal_small";
  return (
    <div className={`modal ${size} modal_${modalType}`}>
      <div className="modal-content">{children}</div>
    </div>
  );
};
const Modal = ({ onClose, children, type = "dark", size = "small" }) => {
  return (
    <Fragment>
      {ReactDOM.createPortal(
        <Backdrop onClose={onClose} />,
        document.getElementById("backdrop-root")
      )}
      {ReactDOM.createPortal(
        <Overlay modalType={type} modalSize={size}>
          {children}
        </Overlay>,
        document.getElementById("overlay-root")
      )}
    </Fragment>
  );
};

export default Modal;
