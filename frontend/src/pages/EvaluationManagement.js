import AllParticipants from "../components/AllParticipants";
import EvaluationRanking from "../components/EvaluationRanking";
import ShowEvaluationRanking from "../components/ShowEvaluationRanking";

const EvaluationList = [
  {
    name: '홍서현',
    evaluation: {
      attitude: 4.5,
      competency: 4.7,
      punctuality: 4.3
    },
    averageEvaluation: 0
  },
  {
    name: '조상욱',
    evaluation: {
      attitude: 4.0,
      competency: 4.2,
      punctuality: 4.1
    },
    averageEvaluation: 0
  },
  {
    name: '이연제',
    evaluation: {
      attitude: 3.5,
      competency: 4.0,
      punctuality: 4.9
    },
    averageEvaluation: 0
  },
  {
    name: '김도연',
    evaluation: {
      attitude: 2.0,
      competency: 3.7,
      punctuality: 4.3
    },
    averageEvaluation: 0
  },
  {
    name: '이소현',
    evaluation: {
      attitude: 4.9,
      competency: 4.7,
      punctuality: 4.5
    },
    averageEvaluation: 0
  },
];

const getAverageEvaluationList = () => {
  const sumOfEvaluationList = EvaluationList.map((item) => {
    return Object.values(item.evaluation).reduce((sum, evaluationElement) => {
      return sum + evaluationElement;
    }, 0);
  });

  const averageEvaluationList = sumOfEvaluationList.map((item) => (
    (item / Object.keys(Object.values(EvaluationList)[0].evaluation).length)
  ));

  let i = 0;

  EvaluationList.map((item) => {
    item.averageEvaluation = Number(averageEvaluationList[i].toFixed(2));
    i++;

    return item;
  })

  return EvaluationList;
}

const getSortedAverageEvaluationList = () => {
  const compare = (a, b) => {
    return b.averageEvaluation - a.averageEvaluation;
  }

  const copyList = JSON.parse(JSON.stringify(getAverageEvaluationList()));

  const sortedList = copyList.sort(compare);

  return sortedList;
}

const EvaluationManagement = () => {
  return (
    <div className='evaluation-management'>
      <div className='evaluation-management-ranking-wrapper'>
        <ShowEvaluationRanking getAverageEvaluationList={getAverageEvaluationList} />
        <EvaluationRanking getSortedAverageEvaluationList={getSortedAverageEvaluationList} />
      </div>
      <div className='evaluation-management-participant-wrapper'>
        <AllParticipants getSortedAverageEvaluationList={getSortedAverageEvaluationList} />
      </div>
    </div>
  )
};

export default EvaluationManagement;
