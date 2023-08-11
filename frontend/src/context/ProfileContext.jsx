/* eslint-disable react-hooks/exhaustive-deps */
// ProfileContext
// isMyProfile: boolean
// memberId: number
// getMemberId():Promise<undefined>

import { createContext, useContext, useEffect, useState } from "react";
import { validateProfile } from "../lib/apis/profileApi";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { useRouter } from "../hooks/useRouter";

const ProfileContext = createContext(null);
export const useProfile = () => useContext(ProfileContext);

export function ProfileProvider({ children }) {
  const [isMyProfile, setIsMyProfile] = useState(false);
  const { routeOptionTo } = useRouter();

  const { id: memberId } = useParams();

  useEffect(() => {
    const getIdAndValidateProfile = async () => {
      try {
        const response = await validateProfile(memberId);
        if (response.status === 200)
          setIsMyProfile((_) => response.data.myProfile);
      } catch (error) {
        if (error.response.data.status === 404) {
          toast.error(`${error.response.data.message}`);
          routeOptionTo("/", { replace: true });
        }
      }
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
