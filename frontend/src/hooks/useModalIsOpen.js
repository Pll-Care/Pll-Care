import { useCallback, useState } from "react";

const useModalIsOpen = () => {
  const [isOpen, setIsOpen] = useState(false);

  const chageModalOpen = useCallback((isModalOpen) => {
    setIsOpen((_) => isModalOpen);
  }, []);

  return { isOpen, chageModalOpen };
};

export default useModalIsOpen;
