import Slider from "react-slick";

import mainImg1Url from "../../assets/main-img-1.svg";
import mainImg2Url from "../../assets/main-img-2.svg";
import mainImg3Url from "../../assets/main-img-3.svg";

const settings = {
  dots: true,
  infinite: true,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1,
  autoplay: true,
  autoplaySpeed: 3000,
};

const Banner = () => {
  return (
    <div className="banner-wrapper">
      <Slider className="banner" {...settings}>
        <img src={mainImg1Url}  alt="배너 사진1"/>
        <img src={mainImg2Url}  alt="배너 사진2"/>
        <img src={mainImg3Url}  alt="배너 사진3"/>
      </Slider>
    </div>
  );
};

export default Banner;
