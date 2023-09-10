import Banner from "../components/Home/Banner";
import Description from "../components/Home/Description";
import Footer from "../components/Home/Footer";
import Project from "../components/Home/Project";
import { useGeneralClient } from "../context/Client/GeneralClientContext";
import HomeClientProvider from "../context/Client/HomeClientContext";
import HomeService from "../lib/service/HomeService";

const Home = () => {
  const { generalHttpClient } = useGeneralClient();

  const homeService = new HomeService(generalHttpClient);

  return (
    <HomeClientProvider homeService={homeService}>
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
    </HomeClientProvider>
  );
};

export default Home;
