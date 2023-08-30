import { useEffect } from "react";

export const useOutsideClick = (ref, closeModal) => {
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (event.target === ref.current) {
        closeModal();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [ref, closeModal]);
};
