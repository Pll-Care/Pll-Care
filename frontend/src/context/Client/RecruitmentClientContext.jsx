import { createContext, useContext } from "react";

const RecruitmentContext = createContext(null);
export const useRecruitmentClient = () => useContext(RecruitmentContext);

const RecruitmentClientProvider = ({ children, recruitmentService }) => {
  const getAllRecruitmentPost =
    recruitmentService.getAllRecruitmentPost.bind(recruitmentService);

  const getRecruitmentPostDetail =
    recruitmentService.getRecruitmentPostDetail.bind(recruitmentService);

  const addLikeRecruitmentPost =
    recruitmentService.addLikeRecruitmentPost.bind(recruitmentService);

  const getRecruitmentProject =
    recruitmentService.getRecruitmentProject.bind(recruitmentService);

  const addRecruitmentPost =
    recruitmentService.addRecruitmentPost.bind(recruitmentService);

  const modifyRecruitmentPost =
    recruitmentService.modifyRecruitmentPost.bind(recruitmentService);

  const deleteRecruitmentPost =
    recruitmentService.deleteRecruitmentPost.bind(recruitmentService);

  const applyRecruitmentPost =
    recruitmentService.applyRecruitmentPost.bind(recruitmentService);

  const applyCancelRecruitmentPost =
    recruitmentService.applyCancelRecruitmentPost.bind(recruitmentService);

  return (
    <RecruitmentContext.Provider
      value={{
        getAllRecruitmentPost,
        getRecruitmentPostDetail,
        addLikeRecruitmentPost,
        getRecruitmentProject,
        addRecruitmentPost,
        modifyRecruitmentPost,
        deleteRecruitmentPost,
        applyRecruitmentPost,
        applyCancelRecruitmentPost,
      }}
    >
      {children}
    </RecruitmentContext.Provider>
  );
};
export default RecruitmentClientProvider;
