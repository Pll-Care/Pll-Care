import profile_default from "../../../assets/profile-default-img.png";

const FinalEvaluationBox = ({ finalEvals = [] }) => {
  return (
    <section className="finalEvaluate">
      <div>
        <h2 className="projectEvaluate_title">최종 평가</h2>
      </div>
      <div className="finalEvaluate_users">
        {finalEvals.map((evaluate, idx) => (
          <UserEvaluate
            key={"finalEvals" + idx}
            memberName={evaluate.name}
            imageUrl={evaluate.imageUrl}
            content={evaluate.content}
            score={evaluate.score}
          />
        ))}
      </div>
    </section>
  );
};

export default FinalEvaluationBox;

const UserEvaluate = ({ memberName, imageUrl, content, score }) => {
  return (
    <div className="finalEvaluate_user">
      <div className="finalEvaluate_userImageWrap">
        <img
          className={
            !imageUrl
              ? "finalEvaluate_userImage image_bg"
              : "finalEvaluate_userImage"
          }
          src={!imageUrl ? profile_default : imageUrl}
          alt={memberName + "의 프로필 이미지"}
        />
      </div>
      <div className="finalEvaluate_userEval">
        <div className="finalEvaluate_userName">
          <span>{memberName}</span>
        </div>
        <div className="finalEvaluate_userScores">
          <div className="finalEvaluate_userScore">
            <div className="finalEvaluate_userScore_part">
              <div className="finalEvaluate_userScore_partTitle">성실도</div>
              <div className="finalEvaluate_userScore_partScore">
                {score.sincerity}
              </div>
            </div>
            <div className="finalEvaluate_userScore_part">
              <div className="finalEvaluate_userScore_partTitle">
                업무 수행 능력
              </div>
              <div className="finalEvaluate_userScore_partScore">
                {score.jobPerformance}
              </div>
            </div>
          </div>
          <div className="finalEvaluate_userScore">
            <div className="finalEvaluate_userScore_part">
              <div className="finalEvaluate_userScore_partTitle">시간엄수</div>
              <div className="finalEvaluate_userScore_partScore">
                {score.punctuality}
              </div>
            </div>
            <div className="finalEvaluate_userScore_part">
              <div className="finalEvaluate_userScore_partTitle">의사소통</div>
              <div className="finalEvaluate_userScore_partScore">
                {score.communication}
              </div>
            </div>
          </div>
        </div>
        <div className="finalEvaluate_userContent">
          <p>{content}</p>
        </div>
      </div>
    </div>
  );
};

/*
finalEvals: {
  memberId: number
  memberName: string
  imageUrl: string
  content: string
  score: {
        sincerity: number
        jobPerformance: number
        punctuality: number
        communication: number
    }
}
*/
