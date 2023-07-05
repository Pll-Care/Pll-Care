import ContactBox from "./Box/ContactBox";

const Introduce = () => {
  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>개인정보</h1>
      </div>
      <div className="profile_introduce_informationBox">
        <ContactBox />
      </div>
    </div>
  );
};

export default Introduce;
