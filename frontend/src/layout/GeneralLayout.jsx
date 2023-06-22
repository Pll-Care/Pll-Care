import MainHeader from "../components/MainHeader";

const GeneralLayout = ({ children }) => {
  return (
    <>
      <MainHeader />
      <div>{children}</div>
    </>
  );
};

export default GeneralLayout;
