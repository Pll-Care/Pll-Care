import { createContext, useContext } from "react";

const ProfileClientContext = createContext(null);
export const useProfileClient = () => useContext(ProfileClientContext);

const ProfileClientProvider = ({ children, profileService }) => {
  const getMemberIdAPI = profileService.getMemberIdAPI.bind(profileService);
  const validateProfileAPI =
    profileService.validateProfileAPI.bind(profileService);
  const getBioAPI = profileService.getBioAPI.bind(profileService);
  const putBioAPI = profileService.putBioAPI.bind(profileService);
  const getContactAPI = profileService.getContactAPI.bind(profileService);
  const patchProfileAPI = profileService.patchProfileAPI.bind(profileService);
  const getPositionAPI = profileService.getPositionAPI.bind(profileService);
  const searchTechAPI = profileService.searchTechAPI.bind(profileService);
  const getProjectExperienceAPI =
    profileService.getProjectExperienceAPI.bind(profileService);
  const getEvaluationChartAPI =
    profileService.getEvaluationChartAPI.bind(profileService);
  const getEvaluationProjectListAPI =
    profileService.getEvaluationProjectListAPI.bind(profileService);
  const getEvaluationProjectDetailAPI =
    profileService.getEvaluationProjectDetailAPI.bind(profileService);
  const getPostProjectAPI =
    profileService.getPostProjectAPI.bind(profileService);
  const getApplyProjectAPI =
    profileService.getApplyProjectAPI.bind(profileService);
  const getLikeProjectAPI =
    profileService.getLikeProjectAPI.bind(profileService);

  return (
    <ProfileClientContext.Provider
      value={{
        getMemberIdAPI,
        validateProfileAPI,
        getBioAPI,
        putBioAPI,
        getContactAPI,
        patchProfileAPI,
        getPositionAPI,
        searchTechAPI,
        getProjectExperienceAPI,
        getEvaluationChartAPI,
        getEvaluationProjectDetailAPI,
        getPostProjectAPI,
        getApplyProjectAPI,
        getLikeProjectAPI,
        getEvaluationProjectListAPI,
      }}
    >
      {children}
    </ProfileClientContext.Provider>
  );
};

export default ProfileClientProvider;
