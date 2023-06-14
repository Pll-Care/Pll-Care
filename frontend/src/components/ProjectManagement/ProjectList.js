import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import Button from '../../components/shared/Button';
import { managementActions } from '../../redux/managementSlice';

const ProjectList = ({ projectList }) => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const isAllMembersEvaluated = () => {
        return projectList
            .flatMap((project) => (project.members))
            .every((member) => (
                Object.values(member.isEvaluateCompleted).every((evaluated) => evaluated)
            ))
    };

    const handleCompleteProject = (e, projectId) => {
        e.preventDefault(); 

        if (isAllMembersEvaluated()) {
            dispatch(managementActions.onComplete(projectId));
            alert('완료 처리되었습니다.');
        } else {
            alert('아직 최종 평가가 완료되지 않았습니다. 평가부터 마무리해주세요.');
            navigate(`/management/${projectId}/evaluation`);
        }
    }

    const handleRemoveProject = (e, projectId) => {
        e.preventDefault();
        dispatch(managementActions.onRemove(projectId));
    }

    return (
        <div className='project-list'>
            {projectList?.map((project) => (
                <Link
                    className='project-item'
                    key={project.id}
                    to={`/management/${project.id}/overview`}
                >
                    <div className='project-item-left-col'>
                        <figure />
                    </div>
                    <div className='project-item-right-col'>
                        <div className='project-item-text-wrapper'>
                            <div className='project-item-heading'>
                                {project.title}
                            </div>
                            <div className='project-item-period'>
                                <div className='project-item-period-heading'>진행 기간: </div>
                                <div>
                                    {new Date(project.startDate).toLocaleDateString()}
                                    ~
                                    {new Date(project.endDate).toLocaleDateString()}
                                </div>
                            </div>
                            <div className='project-item-description'>
                                <div>프로젝트 설명:</div>
                                {project.description?.length > 40 ? [project.description.slice(0, 40), '...'].join('') : project.description}
                            </div>
                        </div>
                        <div className='project-item-button-wrapper'>
                            <Button
                                text={'삭제하기'}
                                onClick={(e) => {handleRemoveProject(e, project.id)}}
                            />
                            {project.state === 'ongoing' && <Button
                                text={'완료하기'}
                                onClick={(e) => {handleCompleteProject(e, project.id)}}
                            />}
                        </div>
                    </div>
                </Link>
            ))}
        </div>
    )
}

export default ProjectList;