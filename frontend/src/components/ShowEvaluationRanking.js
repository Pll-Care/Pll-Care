import { BarChart, Bar, XAxis, YAxis, Tooltip } from 'recharts';

const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
          <div className="custom-tooltip" style={{
            backgroundColor: 'white',
            opacity: '0.8',
            border: 'none',
            width: '150px',
            height: '70px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
          }}>
          <p className="label">{`기여도 평점: ${payload[0].value}`}</p>
        </div>
      );
    }
  
    return null;
  };

const ShowEvaluationRanking = ({ getAverageEvaluationList }) => {    
    return (
        <div className='evaluation-management-show-evaluation-ranking'>
            <BarChart
                width={700}
                height={430}
                data={getAverageEvaluationList()}
            >
                <Tooltip
                    content={<CustomTooltip />}
                />
                <XAxis dataKey="name" />
                <YAxis dataKey="averageEvaluation" tickCount={4}/>
                <Bar dataKey="averageEvaluation" fill="#01E89E" barSize={50} />
            </BarChart>
        </div>
    )
}

export default ShowEvaluationRanking;