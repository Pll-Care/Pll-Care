import { useEffect, useRef, useState } from "react";

import projectDefaultImg from "../../assets/project-default-img.jpg";

import ModalContainer from "../common/ModalContainer";
import Button from "../common/Button";

import useManagementMutation from "../../hooks/useManagementMutation";

import { getStringDate } from "../../utils/date";
import { deleteImage } from "../../lib/apis/managementApi";
import { toast } from "react-toastify";
import { handleImageUploader } from "../../utils/handleImageUploader";
import { useMediaQuery } from "@mui/material";

const ProjectEditor = ({
  isModalVisible,
  setIsModalVisible,
  isEdit = false,
  editData = null,
}) => {
  const { createMutate, editMutate } = useManagementMutation();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [startDate, setStartDate] = useState(getStringDate(new Date()));
  const [endDate, setEndDate] = useState(getStringDate(new Date()));
  const [imgUrl, setImgUrl] = useState("");
  const [responseImgUrl, setResponseImgUrl] = useState("");

  const descriptionRef = useRef(null);

  const inputRef = useRef(null);

  const handleModalClose = () => {
    setIsModalVisible(false);
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

  const handleChangeImage = async (e) => {
    const imgUrl = await handleImageUploader(e.target.files);

    setImgUrl(imgUrl);
    setResponseImgUrl(imgUrl);
  };

  const handleSubmitNewProject = async () => {
    if (title.length < 2) {
      toast.error("프로젝트 이름은 두 글자 이상 작성해주세요.");
      return;
    }

    if (description.length < 5) {
      toast.error("프로젝트 설명은 다섯 글자 이상 작성해주세요.");
      descriptionRef.current.focus();
      return;
    }

    if (new Date(startDate).getTime() > new Date(endDate).getTime()) {
      toast.error("진행 기간이 잘못 설정되었습니다. 다시 설정해주세요.");
      return;
    }

    if (isEdit) {
      editMutate({
        projectId: editData.projectId,
        title,
        description,
        state: "ONGOING",
        startDate: getStringDate(new Date(startDate)),
        endDate: getStringDate(new Date(endDate)),
        imageUrl: responseImgUrl,
      });
    } else {
      createMutate({
        title: title,
        description: description,
        startDate: getStringDate(new Date(startDate)),
        endDate: getStringDate(new Date(endDate)),
        imageUrl: responseImgUrl,
      });
    }

    setIsModalVisible(false);
  };

  const handleUploadImageClick = () => {
    if (!inputRef.current) {
      toast.error("잘못된 접근입니다. 다시 이미지를 업로드해주세요.");
      return;
    }

    inputRef.current.click();
  };

  const handleRemoveImageClick = () => {
    deleteImage("");

    setImgUrl(null);
    setResponseImgUrl(null);
  };

  useEffect(() => {
    if (isEdit) {
      setTitle(editData.title);
      setStartDate(editData.startDate);
      setEndDate(editData.endDate);
      setImgUrl(editData.imageUrl);
      setDescription(editData.description);
    }
  }, [editData, isEdit]);

  const isMobile = useMediaQuery("(max-width: 767px)");

  return (
    <ModalContainer
      open={isModalVisible}
      onClose={handleModalClose}
      width={isMobile ? "90%" : 600}
      height={isMobile ? "80%" : 350}
    >
      <div className="project-editor">
        <div className="project-editor-heading">
          <input
            className="project-editor-heading-input"
            type="text"
            required
            value={title}
            onChange={handleChangeTitle}
            placeholder="프로젝트 이름을 입력하세요"
          />
        </div>
        <div className="project-editor-body">
          <div className="project-editor-left-col">
            <div className="project-editor-img-wrapper">
              <figure
                style={{
                  backgroundImage: `url(${
                    imgUrl ? imgUrl : projectDefaultImg
                  })`,
                }}
              />
              <input
                className="image-input"
                type="file"
                accept="image/*"
                ref={inputRef}
                onChange={handleChangeImage}
              />
              <Button
                onClick={handleUploadImageClick}
                text={"이미지 업로드"}
                size={"small"}
              />
              <Button
                onClick={handleRemoveImageClick}
                text={"이미지 제거"}
                size={"small"}
              />
            </div>
          </div>
          <div className="project-editor-right-col">
            <div className="project-editor-period-first-row">
              <h1>진행 기간:</h1>
              <div>
                <input
                  className="project-editor-period-start-date"
                  type="date"
                  required
                  value={startDate}
                  onChange={handleChangeStartDate}
                  data-placeholder="시작 일자"
                />
                -
                <input
                  className="project-editor-period-end-date"
                  type="date"
                  required
                  value={endDate}
                  onChange={handleChangeEndDate}
                  data-placeholder="종료 일자"
                />
              </div>
            </div>
            <div className="project-editor-period-second-row">
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
        <div className="button-wrapper">
          <Button
            text="작성 완료"
            size={"small"}
            type={"positive"}
            color={"white"}
            onClick={handleSubmitNewProject}
          />
        </div>
      </div>
    </ModalContainer>
  );
};

export default ProjectEditor;
