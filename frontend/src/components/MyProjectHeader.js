import React, { useRef, useState } from "react";
import ReactDOM from "react-dom";

// components
import Button2 from "./Button2";
import NewProjectEditor from "./NewProjectEditor";

const MyProjectHeader = (props) => {
  const [modalVisible, setModalVisible] = useState(false);

  const handleNewProjectButtonClick = () => {
    setModalVisible(true);
  };

  const closeModal = () => {
    setModalVisible(false);
  };

  return (
    <header className="myproject-header">
      <div className="myproject-header-left">
        <div className="myproject-header-left-participation-project">
          참여 프로젝트
        </div>
        <div className="myprojcet-header-left-new-project">
          <Button2
            text={"새 프로젝트 생성"}
            onClick={handleNewProjectButtonClick}
          />
        </div>
      </div>

      <div className="myproject-header-right">
        <div className="myproject-header-right-total-btn">
          <Button2 text={"전체"} onClick={() => alert("전체 버튼 클릭!")} />
        </div>
        <div className="myproject-header-right-ongoing-btn">
          <Button2 text={"진행중"} onClick={() => alert("진행 중 버튼 클릭")} />
        </div>
      </div>
      {modalVisible &&
        ReactDOM.createPortal(
          <div className="my-modal">
            <div className="my-modal-content">
              <span className="close-modal-btn" onClick={closeModal}>
                &times;
              </span>
              <NewProjectEditor
                onCreate={props.onCreate}
                closeModal={closeModal}
              />
            </div>
          </div>,
          document.getElementById("root")
        )}
    </header>
  );
};

export default MyProjectHeader;
