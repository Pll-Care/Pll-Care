import Banner from "../components/Home/Banner";
import Description from "../components/Home/Description";
import Footer from "../components/Home/Footer";
import Project from "../components/Home/Project";

const Home = () => {
  return (
    <div className="home">
      <Banner />
      <Description />
      <div className="project-container-wrapper">
        <div className="project-container">
          <Project type={"popular"} />
          <Project type={"imminent"} />
          <Project type={"upToDate"} />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Home;
