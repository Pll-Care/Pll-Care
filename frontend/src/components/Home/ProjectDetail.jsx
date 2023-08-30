import Slider from "react-slick";
import { useMediaQuery } from "@mui/material";

import { Link } from "react-router-dom";

import projectDefaultImageUrl from "../../assets/project-default-img.jpg";
import heartImageUrl from "../../assets/heart.png";

const settings = {
  dots: true,
  infinite: true,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1,
  autoplay: false,
  autoplaySpeed: 4000,
};

const ProjectDetail = ({ type, projectList }) => {
  const isTablet = useMediaQuery("(min-width: 767px) and (max-width: 1024px)");

  return (
    projectList.length && (
      <div className="project-detail-wrapper">
        <div className="project-detail">
          <Slider {...settings}>
            {projectList.map((project) => (
              <Link key={project.postId} to={`/recruitment/${project.postId}`}>
                <div className="project-first-row">
                  <div className="project-img-wrapper">
                    <img
                      src={
                        project.projectImageUrl
                          ? project.projectImageUrl
                          : projectDefaultImageUrl
                      }
                      alt={"프로젝트 이미지"}
                    />
                  </div>
                  <div className="project-heading-wrapper">
                    <h1>
                      {isTablet
                        ? project.projectTitle.length > 24
                          ? project.projectTitle.slice(0, 24) + "..."
                          : project.projectTitle
                        : project.projectTitle.length > 27
                        ? project.projectTitle.slice(0, 27) + "..."
                        : project.projectTitle}
                    </h1>
                    {type === "popular" && (
                      <div className="project-like-count">
                        <img src={heartImageUrl} alt="좋아요" />
                        <div>{project.likeCount}</div>
                      </div>
                    )}
                    {type === "imminent" && (
                      <div>
                        <div>마감</div>
                        {new Date(project.recruitEndDate).toLocaleDateString()}
                      </div>
                    )}
                  </div>
                </div>
                <div className="project-second-row">
                  {isTablet ? project.description.length > 35
                    ? project.description.slice(0, 35) + "..."
                    : project.description: project.description.length > 52
                    ? project.description.slice(0, 52) + "..."
                    : project.description}
                </div>
              </Link>
            ))}
          </Slider>
        </div>
      </div>
    )
  );
};

export default ProjectDetail;
