import { useEffect, useState } from "react";
import ProjectItem from "../myproject/ProjectItem";
import PaginationButton from "../../common/PaginationButton";
import { useProfile } from "../../../context/ProfileContext";
import { useQuery } from "react-query";
import { getLikeProjectAPI } from "../../../lib/apis/profileApi";

const QUERY_KEY = "likeproject";

const LikeProject = () => {
  const [currentPageNumber, setCurrentPageNumber] = useState(1);
  const { memberId } = useProfile();

  const { data, refetch } = useQuery(
    [QUERY_KEY, memberId, currentPageNumber],
    () => getLikeProjectAPI({ memberId, page: currentPageNumber })
  );

  const totalPages = data?.totalPages || 0;

  const changePageNumber = (pageNumber) => {
    setCurrentPageNumber(pageNumber);
  };

  useEffect(() => {
    refetch();
  }, [currentPageNumber, refetch]);

  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>'좋아요' 한 모집글</h1>
      </div>
      <div className="myProject">
        <div className="myProject_selectContainer"></div>
        <div className="myProject_project">
          <ul>
            {data?.content.map((project) => (
              <ProjectItem
                key={project.postId}
                postId={project.postId}
                title={project.title}
                description={project.description}
              />
            ))}
          </ul>
        </div>
        <PaginationButton
          changePageNumber={changePageNumber}
          totalPageNumber={totalPages}
          currentPageNumber={currentPageNumber}
        />
      </div>
    </div>
  );
};

export default LikeProject;
