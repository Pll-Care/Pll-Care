import MainHeader from "../components/common/MainHeader";

const GeneralLayout = ({ children }) => {
  return (
    <>
      <MainHeader />
      <div>{children}</div>
    </>
  );
};

export default GeneralLayout;
