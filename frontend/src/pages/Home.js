import Banner from "../components/Home/Banner";
import Description from "../components/Home/Description";
import Footer from "../components/Home/Footer";
import Project from "../components/Home/Project";

const Home = () => {
  return (
    <div className="home">
      <Banner />
      <Description />
      <div className="project-wrapper">
        <Project type={"popular"} />
        <Project type={"imminent"} />
        <Project type={"upToDate"} />
      </div>
      <Footer />
    </div>
  );
};

export default Home;
