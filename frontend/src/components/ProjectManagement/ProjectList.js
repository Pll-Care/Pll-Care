import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import Button from '../Button';
import { managementActions } from '../../redux/managementSlice';

const ProjectList = ({ projectList }) => {
    const dispatch = useDispatch();

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
                                onClick={(e) => { e.preventDefault(); dispatch(managementActions.onRemove(project.id)); }}
                            />
                            {project.state === 'ongoing' && <Button
                                text={'완료하기'}
                                onClick={(e) => { e.preventDefault(); dispatch(managementActions.onComplete(project.id)); alert('완료 처리되었습니다.') }}
                            />}
                        </div>
                    </div>
                </Link>
            ))}
        </div>
    )
}

export default ProjectList;