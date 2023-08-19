import ContactBox from "./ContactBox";
import PositionBox from "./PositionBox";
import ProjectBox from "./ProjectBox";

const Introduce = () => {
  return (
    <div>
      <div className="profile_title">
        <h1>개인정보</h1>
      </div>
      <div className="profile_content">
        <ContactBox />
        <PositionBox />
        <ProjectBox />
      </div>
    </div>
  );
};

export default Introduce;
