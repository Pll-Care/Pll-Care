import { Link } from 'react-router-dom';
import Button from '../Button';
import { useContext } from 'react';
import { ProjectListDispatchContext } from '../../pages/Management';

const ProjectList = ({ projectList }) => {
    const { onRemoveProject, onCompleteProject } = useContext(ProjectListDispatchContext);

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
                        <div>
                            <div className='project-item-heading'>
                                {project.title}
                            </div>
                            <div className='project-item-period'>
                                진행 기간: {new Date(project.startDate).toLocaleDateString()} ~ {new Date(project.endDate).toLocaleDateString()}
                            </div>
                            <div className='project-item-description'>
                                프로젝트 설명: {project.description?.length > 40 ? [project.description.slice(0, 40), '...'].join('') : project.description}
                            </div>
                        </div>
                        <div className='project-item-button-wrapper'>
                            <Button
                                text={'삭제하기'}
                                onClick={(e) => { e.preventDefault(); onRemoveProject(project.id); }}
                            />
                            <Button
                                text={'완료하기'}
                                onClick={(e) => { e.preventDefault(); onCompleteProject(project.id); alert('완료 처리되었습니다.')}}
                            />
                        </div>
                    </div>
                </Link>
            ))}
        </div>
    )
}

export default ProjectList;