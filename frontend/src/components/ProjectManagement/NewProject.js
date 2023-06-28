import ProjectEditor from "./ProjectEditor";

const NewProject = ({ isModalVisible, setIsModalVisible }) => {
  return (
    <ProjectEditor
      isModalVisible={isModalVisible}
      setIsModalVisible={setIsModalVisible}
    />
  );
};

export default NewProject;
