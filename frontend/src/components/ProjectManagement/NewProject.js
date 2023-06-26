import { useRef, useState } from "react";

import Button from "../../components/common/Button";

import { getStringDate } from "../../utils/date";
import useManagementMutation from "../../hooks/useManagementMutation";
import { toast } from "react-toastify";

const NewProject = ({ setIsModalVisible }) => {
  const modalOutside = useRef();

  const { createMutate } = useManagementMutation();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [startDate, setStartDate] = useState(getStringDate(new Date()));
  const [endDate, setEndDate] = useState(getStringDate(new Date()));
  const [imgUrl, setImgUrl] = useState("");
  const [formData, setFormData] = useState();

  const descriptionRef = useRef(null);

  const inputRef = useRef(null);

  const handleModalClose = (e) => {
    if (e.target === modalOutside.current) {
      setIsModalVisible(false);
    }
  };

  const handleChangeTitle = (e) => {
    setTitle(e.target.value);
  };

  const handleChangeStartDate = (e) => {
    setStartDate(e.target.value);
  };

  const handleChangeEndDate = (e) => {
    setEndDate(e.target.value);
  };

  const handleChangeContent = (e) => {
    setDescription(e.target.value);
  };

  const handleSubmitNewProject = async () => {
    if (description.length < 5) {
      toast.error("프로젝트 설명은 다섯 글자 이상 작성해주세요.");
      descriptionRef.current.focus();
      return;
    }

    createMutate({
      title: title,
      description: description,
      startDate: getStringDate(new Date(startDate)),
      endDate: getStringDate(new Date(endDate)),
    });

    setIsModalVisible(false);
  };

  const handleUploadImage = (e) => {
    if (!e.target.files) {
      toast.error("잘못된 접근입니다. 다시 이미지를 업로드해주세요.");
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(e.target.files[0]);
    reader.onloadend = () => {
      setImgUrl(reader.result);
    };

    const formData = new FormData();
    formData.append('image', e.target.files[0]);
    setFormData(formData);
  };

  const handleUploadImageClick = () => {
    if (!inputRef.current) {
      toast.error("잘못된 접근입니다. 다시 이미지를 업로드해주세요.");
      return;
    }

    inputRef.current.click();
  };

  return (
    <div
      className="new-project-modal-wrapper"
      ref={modalOutside}
      onClick={handleModalClose}
    >
      <div className="new-project">
        <div className="new-project-first-row">
          <div className="new-project-left-col">
            <div classname="new-project-img-wrapper">
              <figure
                style={{
                  backgroundImage: `url(${imgUrl && imgUrl})`,
                }}
              />
              <input
                className="image-input"
                type="file"
                accept="image/*"
                ref={inputRef}
                onChange={handleUploadImage}
              />
              <Button
                onClick={handleUploadImageClick}
                text={"이미지 추가"}
                size={"small"}
              />
            </div>
            <input
              className="new-project-heading-input"
              type="text"
              required
              value={title}
              onChange={handleChangeTitle}
              placeholder="프로젝트 제목을 입력하세요"
            />
          </div>
          <div className="new-project-right-col">
            <Button text="작성 완료" onClick={handleSubmitNewProject} />
          </div>
        </div>
        <div className="new-project-second-row">
          <div className="new-project-period">
            <div className="new-project-period-left-col">
              <h1>진행 기간:</h1>
            </div>
            <div className="new-project-period-right-col">
              <input
                className="new-project-period-start-date"
                type="date"
                required
                value={startDate}
                onChange={handleChangeStartDate}
                data-placeholder="시작 일자"
              />
              -
              <input
                className="new-project-period-end-date"
                type="date"
                required
                value={endDate}
                onChange={handleChangeEndDate}
                data-placeholder="종료 일자"
              />
            </div>
          </div>
          <textarea
            value={description}
            onChange={handleChangeContent}
            ref={descriptionRef}
            placeholder="프로젝트 설명을 작성하세요"
            required
          />
        </div>
      </div>
    </div>
  );
};

export default NewProject;
