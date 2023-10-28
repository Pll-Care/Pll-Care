// badgeDtos : {evaluationBadge: string, quantity: number}[]
import PassionImage from "../../../assets/enthusiastic-participant-badge-img.svg";
import IdeaImage from "../../../assets/idea-bank-badge-img.svg";
import LeaderImage from "../../../assets/good-leader-badge-img.svg";
import SupporterImage from "../../../assets/best-supporter-badge-img.svg";

const BadgeBox = ({ badges }) => {
  return (
    <section className="badgeBox">
      <div>
        <h2 className="projectEvaluate_title">중간평가 뱃지</h2>
      </div>
      {badges && (
        <div className="badges">
          <div className="badge">
            <div>
              <div className="badgeImage">
                <img src={PassionImage} alt={badges.participant} />
              </div>
            </div>
            <div>
              <div className="badge_divide">·</div>
            </div>
            <div className="badge_quantity">
              <span>{badges.quantity1}</span>
            </div>
          </div>
          <div className="badge">
            <div>
              <div className="badgeImage">
                <img src={IdeaImage} alt={badges.bank} />
              </div>
            </div>
            <div>
              <div className="badge_divide">·</div>
            </div>
            <div className="badge_quantity">
              <span>{badges.quantity2}</span>
            </div>
          </div>
          <div className="badge">
            <div>
              <div className="badgeImage">
                <img src={LeaderImage} alt={badges.leader} />
              </div>
            </div>
            <div>
              <div className="badge_divide">·</div>
            </div>
            <div className="badge_quantity">
              <span>{badges.quantity3}</span>
            </div>
          </div>
          <div className="badge">
            <div>
              <div className="badgeImage">
                <img src={SupporterImage} alt={badges.support} />
              </div>
            </div>
            <div>
              <div className="badge_divide">·</div>
            </div>
            <div className="badge_quantity">
              <span>{badges.quantity4}</span>
            </div>
          </div>
        </div>
      )}
    </section>
  );
};

export default BadgeBox;
