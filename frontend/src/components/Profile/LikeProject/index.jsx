import { useEffect, useState } from "react";
import ProjectItem from "../myproject/ProjectItem";
import PaginationButton from "../../common/PaginationButton";
import { useProfile } from "../../../context/ProfileContext";
import { useQuery } from "react-query";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";

const QUERY_KEY = "likeproject";

const LikeProject = () => {
  const [currentPageNumber, setCurrentPageNumber] = useState(1);
  const { memberId } = useProfile();
  const { getLikeProjectAPI } = useProfileClient();

  const { data, refetch } = useQuery(
    [memberId, QUERY_KEY, currentPageNumber],
    () => getLikeProjectAPI(currentPageNumber)
  );

  const totalPages = data?.data?.totalPages || 0;

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
        {data?.data?.content.length > 0 ? (
          <>
            <div className="myProject_project">
              <ul>
                {data.data.content.map((project) => (
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
          </>
        ) : (
          <div className="project-no-item">
            <p>모집글이 없습니다.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default LikeProject;
