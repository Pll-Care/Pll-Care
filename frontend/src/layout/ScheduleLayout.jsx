import { useGeneralClient } from "../context/Client/GeneralClientContext";
import ScheduleClientProvider from "../context/Client/ScheduleClientContext";
import ScheduleService from "../lib/service/ScheduleService";

const ScheduleLayout = ({ children }) => {
  const { generalHttpClient } = useGeneralClient();

  const scheduleService = new ScheduleService(generalHttpClient);

  return (
    <ScheduleClientProvider scheduleService={scheduleService}>
      {children}
    </ScheduleClientProvider>
  );
};

export default ScheduleLayout;
