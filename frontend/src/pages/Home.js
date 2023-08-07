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
        <Project type={"실시간 인기 프로젝트"} />
        <Project type={"마감 임박 프로젝트"} />
      </div>
      <Footer />
    </div>
  );
};

export default Home;
