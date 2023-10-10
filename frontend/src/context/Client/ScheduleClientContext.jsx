import { createContext, useContext } from "react";

const ScheduleContext = createContext(null);
export const useScheduleClient = () => useContext(ScheduleContext);

