import ApplicationItem from "./ApplicationItem";

const dummy = [
  { userId: 0, name: "김철수", job: "Front-End" },
  { userId: 1, name: "김현학", job: "Front-End" },
  { userId: 2, name: "김하영", job: "Back-End" },
  { userId: 3, name: "김아름", job: "Back-End" },
  { userId: 4, name: "김현수", job: "Front-End" },
  { userId: 5, name: "김나영", job: "Back-End" },
];

const ApplicationStatus = () => {
  return (
    <section className="applicationStatus">
      <div>
        <span className="teamMember_title">지원 현황</span>
      </div>
      <div>
        <ul className="applicationStatus_users">
          {dummy.map((user) => (
            <ApplicationItem
              key={user.userId}
              userId={user.userId}
              name={user.name}
              job={user.job}
            />
          ))}
        </ul>
      </div>
    </section>
  );
};

export default ApplicationStatus;
