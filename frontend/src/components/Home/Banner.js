import Slider from "react-slick";

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
        <div>1</div>
        <div>2</div>
        <div>3</div>
      </Slider>
    </div>
  );
};

export default Banner;
