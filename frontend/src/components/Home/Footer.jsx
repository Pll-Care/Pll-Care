import logo from "../../assets/logo-with-text.svg";

const Footer = () => {
  return (
    <footer className="footer">
      <figure
        style={{
          backgroundImage: `url(${logo})`,
        }}
      />
      <div>풀케어 : FULL CARE, Project Manager</div>
      <div>Copyright 2023. Team Full-Care. All rights reserved</div>
    </footer>
  );
};

export default Footer;
