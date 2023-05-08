import { useRef, useState } from "react";

import Button from "./Button";

const NewProjectEditor = (props) => {
  const titleInput = useRef();
  const contentInput = useRef();

  const { onCreate, closeModal } = props;

  const [state, setState] = useState({
    title: "",
    content: "",
  });

  const handleChangeState = (e) => {
    setState({ ...state, [e.target.name]: e.target.value });
  };

  // 작성완료 시 이벤트
  const handleSubmit = () => {
    if (state.title.length < 1) {
      titleInput.current.focus();
      return;
    }

    if (state.content.length < 5) {
      contentInput.current.focus();
      return;
    }

    console.log(state);
    onCreate(state.title, state.content);
    alert("작성 완료!");
    setState({
      title: "",
      content: "",
    });
    closeModal();
  };

  return (
    <div className="new-project-editor">
      <h2 className="new-project-editor-head">New Project</h2>
      <div className="new-project-editor-title">
        <input
          ref={titleInput}
          name="title"
          value={state.title}
          onChange={handleChangeState}
        />
      </div>
      <div className="new-project-editor-content">
        <textarea
          ref={contentInput}
          name="content"
          value={state.content}
          onChange={handleChangeState}
        />
      </div>
      <div className="new-project-editor-button">
        <Button text={"작성완료"} onClick={handleSubmit}></Button>
      </div>
    </div>
  );
};

export default NewProjectEditor;
