import ContactBox from "./ContactBox";
import PositionBox from "./PositionBox";

const Introduce = () => {
  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>개인정보</h1>
      </div>
      <div className="profile_introduce_informationBox">
        <ContactBox />
        <PositionBox />
      </div>
    </div>
  );
};

export default Introduce;
