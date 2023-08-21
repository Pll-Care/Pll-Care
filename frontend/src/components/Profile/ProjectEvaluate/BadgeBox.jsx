// badgeDtos : {evaluationBadge: string, quantity: number}[]
import PassionImage from "../../../assets/badge-passion.png";
import IdeaImage from "../../../assets/badge-idea.png";
import LeaderImage from "../../../assets/badge-leader.png";
import SupporterImage from "../../../assets/badge-supporter.png";

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
