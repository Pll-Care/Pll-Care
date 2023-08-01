// ProfileContext
// isMyProfile: boolean
// memberId: number
// getMemberId():Promise<undefined>

import { createContext, useContext, useEffect, useState } from "react";
import { validateProfile } from "../lib/apis/profileApi";
import { useParams } from "react-router-dom";

const ProfileContext = createContext(null);
export const useProfile = () => useContext(ProfileContext);

export function ProfileProvider({ children }) {
  const [isMyProfile, setIsMyProfile] = useState(false);

  const { id: memberId } = useParams();

  useEffect(() => {
    const getIdAndValidateProfile = async () => {
      const response = await validateProfile(memberId);
      if (response) setIsMyProfile(response);
    };
    getIdAndValidateProfile();

    return () => {
      setIsMyProfile(false);
    };
  }, [memberId]);

  return (
    <ProfileContext.Provider value={{ isMyProfile, memberId }}>
      {children}
    </ProfileContext.Provider>
  );
}
