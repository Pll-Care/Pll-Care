import Banner from "../components/Home/Banner";
import Description from "../components/Home/Description";
import Footer from "../components/Home/Footer";
import Project from "../components/Home/Project";
import HomeLayout from "../layout/HomeLayout";

const Home = () => {
  return (
    <HomeLayout>
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
    </HomeLayout>
  );
};

export default Home;
