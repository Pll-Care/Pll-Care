import { useEffect, useRef, useState } from "react";

const useOutSide = () => {
  const [isOutsidClick, setIsOutsidClick] = useState(false);
  const outsideRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(event) {
      if (outsideRef.current && !outsideRef.current.contains(event.target)) {
        setIsOutsidClick(true);
      }
    }
    document.addEventListener("click", handleClickOutside);

    return () => {
      document.removeEventListener("click", handleClickOutside);
      setIsOutsidClick(false);
    };
  });

  return { outsideRef, isOutsidClick };
};

export default useOutSide;
