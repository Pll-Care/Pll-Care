import logo from "../../assets/logo-with-text.jpg";

const Footer = () => {
  return (
    <footer className="footer">
      <figure
        style={{
          backgroundImage: `url(${logo})`,
        }}
      />
      <div>플케어 : PLL CARE, Project Manager</div>
      <div>Copyright 2023. Team PLL CARE. All rights reserved</div>
    </footer>
  );
};

export default Footer;
