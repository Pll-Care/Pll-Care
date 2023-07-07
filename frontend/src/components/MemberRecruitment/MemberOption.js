import { useState } from "react";

import Button from "../common/Button";

import Card from "../common/Card";
import MemberFilterOption from "./MemberFilterOption";
import ModalContainer from "../common/ModalContainer";

const MemberOption = () => {
  const [modalIsVisible, setModalIsVisible] = useState(false);

  const modalHandler = () => {
    setModalIsVisible(true);
  };

  const modalCloseHandler = () => {
    setModalIsVisible(false);
  };

  return (
    <>
      <div className="member-option">
        <div className="member-option-title">
          <h2>모집 중인 프로젝트 목록</h2>
          <h3>Currently Recruiting</h3>
        </div>
      </div>
      <div className="member-filter">
        <Button text="작성하기" type="positive" onClick={modalHandler} />
        <ModalContainer
          open={modalIsVisible}
          onClose={modalCloseHandler}
          type="dark"
          width="80%"
        >
          <form className="modal-title">
            <div className="modal-title-text">
              <img
                src="https://cdn.pixabay.com/photo/2015/06/24/15/45/hands-820272_640.jpg"
                alt=""
              />
              <h2>New Project</h2>
            </div>

            <div className="modal-title-button">
              <Button text="수정" size="small" />
              <Button text="삭제" size="small" />
            </div>
          </form>

          <Card>
            <div className="modal-content">
              <div className="modal-content-project">
                <h3>프로젝트 선택</h3>
              </div>

              <div className="modal-content-project">
                <h3>주제/분야</h3>
              </div>

              <div className="modal-content-project">
                <h3>모집 기간</h3>
              </div>

              <div className="modal-content-options">
                <h3>지역</h3>
              </div>

              <div className="modal-content-options">
                <h3>포지션</h3>
                <div className="modal-content-options-types">
                  <div className="modal-content-options-types-select">
                    <h4>백엔드</h4>
                    <input type="number" />
                    <input />
                  </div>
                  <div className="modal-content-options-types-select">
                    <h4>프론트엔드</h4>
                    <input type="number" />
                    <input />
                  </div>
                  <div className="modal-content-options-types-select">
                    <h4>디자인</h4>
                    <input type="number" />
                    <input />
                  </div>
                  <div className="modal-content-options-types-select">
                    <h4>기획</h4>
                    <input type="number" />
                    <input />
                  </div>
                </div>
              </div>

              <div className="modal-content-description">
                <h3>설명</h3>
                <input />
              </div>

              <div className="modal-content-description">
                <h3>레퍼런스</h3>
                <input />
                <input />
              </div>

              <div className="modal-content-description">
                <h3>컨택</h3>
                <input />
              </div>
            </div>
          </Card>
        </ModalContainer>
        <MemberFilterOption />
      </div>
    </>
  );
};
export default MemberOption;
