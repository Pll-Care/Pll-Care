import ContactBox from "./ContactBox";
import PositionBox from "./PositionBox";
import ProjectBox from "./ProjectBox";

const Introduce = () => {
  return (
    <div>
      <div>
        <h1 className="profile_introduce_title">개인정보</h1>
      </div>
      <div className="profile_introduce_informationBox">
        <ContactBox />
        <PositionBox />
        <ProjectBox />
      </div>
    </div>
  );
};

export default Introduce;
