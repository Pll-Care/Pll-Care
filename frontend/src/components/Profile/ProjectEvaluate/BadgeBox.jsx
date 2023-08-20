// badgeDtos : {evaluationBadge: string, quantity: number}[]

const BadgeBox = ({ badgeDtos = [] }) => {
  console.log(badgeDtos);
  return (
    <section className="badgeBox">
      <div>
        <h2 className="projectEvaluate_title">중간평가 뱃지</h2>
      </div>
      <div className="badges">
        {badgeDtos.map((badge, idx) => (
          <div key={idx} className="badge">
            <div>
              <div className="badgeImage"></div>
            </div>
            <div>
              <div className="badge_divide">·</div>
            </div>
            <div className="badge_quantity">
              <span>{badge.quantity}</span>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default BadgeBox;
