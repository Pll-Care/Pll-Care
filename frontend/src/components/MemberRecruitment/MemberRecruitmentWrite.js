import { Link, useNavigate } from "react-router-dom";
import { useRef, useState } from "react";
import { toast } from "react-toastify";

import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import AddCircleIcon from "@mui/icons-material/AddCircle";

import projectDefaultImg from "../../assets/project-default-img.jpg";
import Button from "../common/Button";
import Card from "../common/Card";

import { useQuery } from "react-query";
import { getRecruitmentProject } from "../../lib/apis/memberRecruitmentApi";
import { useAddRecruitmentPostMutation } from "../../hooks/useRecruitmentMutation";
import { backendStacks, concepts, location } from "../../utils/recruitment";

const MemberRecruitmentWrite = () => {
  const navigate = useNavigate();
  const initialDate = new Date().toISOString().split("T")[0];

  // 모집글 생성 입력값들
  const [formValues, setFormValues] = useState({
    projectId: "",
    title: "",
    concept: "IOS",
    description: "",
    recruitStartDate: initialDate,
    recruitEndDate: initialDate,
    reference: "",
    contact: "",
    region: "서울",
    backendCnt: 0,
    frontendCnt: 0,
    managerCnt: 0,
    designCnt: 0,
  });

  const { title, description, recruitStartDate, recruitEndDate } = formValues;

  const inputRefs = {
    title: useRef(),
    description: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    reference: useRef(),
    region: useRef(),
    techStack: useRef(),
    contact: useRef(),
    backendCnt: useRef(),
  };

  const { data, isLoading } = useQuery(
    ["allProject"],
    async () => await getRecruitmentProject(),
    {
      onSuccess: (data) => {
        setFormValues((prevState) => ({
          ...prevState,
          projectId: data[0]?.projectId || "",
        }));
      },
    }
  );

  // 기본 프로젝트 이미지 => projectId에 따라 이미지 바뀔 수 있게 처리
  let imageUrl = projectDefaultImg;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // 기술 스택들 태그들
  const [stacks, setStacks] = useState([]);

  // 입력받은 스택
  const [stackInput, setStackInput] = useState("");

  // 입력한 값이 포함한 stack 보여주기
  const filteredStacks = backendStacks.filter((stack) =>
    stack.includes(stackInput.toLowerCase())
  );

  // 입력 스택
  const handleStackInputChange = (e) => {
    setStackInput(e.target.value);
  };

  // 스택 추가하기
  const stackPlusClickHandler = () => {
    if (stacks.includes(stackInput)) {
      return;
    }
    setStacks((prevState) => [...prevState, stackInput]);
    setStackInput("");
  };

  // 스택 빼기
  const stackMinusClickHandler = (project) => {
    setStacks((prevState) => prevState.filter((stack) => stack !== project));
  };

  const { mutate: addPostMutate } = useAddRecruitmentPostMutation(formValues);

  // 생성하기
  const handleAddRecruitmetPost = () => {
    const { backendCnt, frontendCnt, designCnt, managerCnt, ...allData } =
      formValues;

    if (title.length < 2) {
      toast.error("모집글 제목을 입력해주세요");
      inputRefs.title.current.focus();
      return;
    }
    const start = new Date(recruitStartDate);
    const end = new Date(recruitEndDate);
    if (start > end) {
      toast.error("모집 기간 수정해주세요");
      inputRefs.startDate.current.focus();
      return;
    }
    if (description.length < 2) {
      toast.error("모집글 설명을 입력해주세요");
      inputRefs.description.current.focus();
      return;
    }
    if (
      parseInt(backendCnt, 10) +
        parseInt(frontendCnt, 10) +
        parseInt(managerCnt, 10) +
        parseInt(designCnt, 10) ===
      0
    ) {
      toast.error("포지션 인원 1명 이상이어야 합니다");
      inputRefs.backendCnt.current.focus();
      return;
    }

    const recruitCnt = [
      {
        position: "BACKEND",
        currentCnt: 0,
        totalCnt: parseInt(backendCnt, 10),
      },
      {
        position: "FRONTEND",
        currentCnt: 0,
        totalCnt: parseInt(frontendCnt, 10),
      },
      {
        position: "MANAGER",
        currentCnt: 0,
        totalCnt: parseInt(managerCnt, 10),
      },
      {
        position: "DESIGN",
        currentCnt: 0,
        totalCnt: parseInt(designCnt, 10),
      },
    ];
    const body = {
      ...allData,
      recruitInfo: recruitCnt,
      techStack: stacks,
    };
    // addPostMutate(body);
    console.log(body);

    setFormValues({
      projectId: "",
      title: "",
      concept: "IOS",
      description: "",
      recruitStartDate: initialDate,
      recruitEndDate: initialDate,
      reference: "",
      contact: "",
      region: "서울",
      techStack: "",
      backendCnt: 0,
      frontendCnt: 0,
      managerCnt: 0,
      designCnt: 0,
    });

    navigate("/recruitment");
  };

  return (
    <div className="member-write">
      <div className="member-title">
        <Link to="/recruitment">
          <ArrowBackIosNewIcon className="recruitment-direction" />
        </Link>
        <img src={imageUrl} alt="" />
        {data && data?.length !== 0 ? (
          <input
            type="text"
            name="title"
            ref={inputRefs.title}
            value={formValues.title}
            onChange={handleChange}
            placeholder="모집글 제목을 작성해보세요"
          />
        ) : (
          <h2>프로젝트 선택해 모집글을 작성하세요 </h2>
        )}
      </div>

      {data && !isLoading && data?.length === 0 && (
        <h1>
          아직 프로젝트가 없습니다! 프로젝트 관리에서 프로젝트를 생성해보세요
        </h1>
      )}

      {data && !isLoading && data?.length > 0 && (
        <>
          <Card className="member-write-card">
            <div className="member-content">
              <div className="member-content-project">
                <h3>프로젝트 선택</h3>
                <select
                  className="member-select1"
                  name="projectId"
                  onChange={handleChange}
                  value={formValues.projectId || ""}
                >
                  <option disabled hidden>
                    선택하세요
                  </option>
                  {data?.map((project, index) => (
                    <option key={index} value={project.projectId}>
                      {project.title}
                    </option>
                  ))}
                </select>
              </div>

              <div className="member-content-project">
                <h3>주제/분야</h3>
                <select
                  className="member-select2"
                  onChange={handleChange}
                  value={formValues.concept || ""}
                  name="concept"
                >
                  <option disabled hidden>
                    선택하세요
                  </option>
                  {concepts.map((concept, index) => (
                    <option key={index} value={concept}>
                      {concept}
                    </option>
                  ))}
                </select>
              </div>

              <div className="member-content-project">
                <h3>모집 기간</h3>
                <input
                  className="member-select3"
                  type="date"
                  required
                  name="recruitStartDate"
                  value={formValues.recruitStartDate}
                  onChange={handleChange}
                  data-placeholder="시작 일자"
                  ref={inputRefs.startDate}
                />
                ~
                <input
                  type="date"
                  required
                  name="recruitEndDate"
                  value={formValues.recruitEndDate}
                  onChange={handleChange}
                  data-placeholder="종료 일자"
                  ref={inputRefs.endDate}
                />
              </div>

              <div className="member-content-options">
                <h3>지역</h3>
                <select
                  className="member-select4"
                  onChange={handleChange}
                  name="region"
                  value={formValues.region || ""}
                  ref={inputRefs.region}
                >
                  <option disabled hidden>
                    선택하세요
                  </option>
                  {location.map((place, index) => (
                    <option key={index} value={place}>
                      {place}
                    </option>
                  ))}
                </select>
              </div>

              <div className="member-content-position">
                <h3>포지션</h3>
                <div className="options">
                  <div className="options-container">
                    <h5>백엔드</h5>
                    <h5>프론트엔드</h5>
                    <h5>기획</h5>
                    <h5>디자인</h5>
                  </div>

                  <div className="options-count">
                    <input
                      className="position-number"
                      type="number"
                      min="0"
                      placeholder="0"
                      value={formValues.backendCnt}
                      name="backendCnt"
                      onChange={handleChange}
                      ref={inputRefs.backendCnt}
                    />
                    <input
                      className="position-number"
                      type="number"
                      min="0"
                      placeholder="0"
                      value={formValues.frontendCnt}
                      name="frontendCnt"
                      onChange={handleChange}
                    />
                    <input
                      className="position-number"
                      type="number"
                      min="0"
                      placeholder="0"
                      value={formValues.managerCnt}
                      name="managerCnt"
                      onChange={handleChange}
                    />
                    <input
                      className="position-number"
                      type="number"
                      min="0"
                      placeholder="0"
                      value={formValues.designCnt}
                      name="designCnt"
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="member-content-position-stack">
                  <h5>기술 스택</h5>
                  <div className="member-stack">
                    <div className="member-stack-input">
                      <input
                        placeholder="기술 스택을 추가하세요"
                        type="text"
                        value={stackInput}
                        onChange={handleStackInputChange}
                        ref={inputRefs.techStack}
                      />
                      <AddCircleIcon
                        className="mui-icon"
                        onClick={stackPlusClickHandler}
                      />
                    </div>
                    {stackInput && (
                      <div className="member-inputcontainer-box">
                        {filteredStacks.map((stack, index) => (
                          <h5 key={index} onClick={() => setStackInput(stack)}>
                            {stack}
                          </h5>
                        ))}
                      </div>
                    )}
                  </div>
                  <div className="member-stack-button">
                    {stacks.length > 0 &&
                      stacks.map((stack) => (
                        <Button
                          text={stack}
                          size="small"
                          onClick={() => stackMinusClickHandler(stack)}
                        />
                      ))}
                    {stacks.length === 0 && (
                      <h6>프로젝트에서 사용할 스택을 추가해보세요</h6>
                    )}
                  </div>
                </div>
              </div>

              <div className="member-content-description">
                <h3>설명</h3>
                <textarea
                  onChange={handleChange}
                  value={formValues.description}
                  name="description"
                  ref={inputRefs.description}
                />
              </div>

              <div className="member-content-description">
                <h3>레퍼런스</h3>
                <textarea
                  onChange={handleChange}
                  value={formValues.reference}
                  name="reference"
                  ref={inputRefs.reference}
                />
              </div>

              <div className="member-content-description">
                <h3>컨택</h3>
                <textarea
                  onChange={handleChange}
                  value={formValues.contact}
                  name="contact"
                  ref={inputRefs.contact}
                />
              </div>
            </div>
          </Card>
          <Button text="생성 완료" onClick={handleAddRecruitmetPost} />
        </>
      )}
    </div>
  );
};
export default MemberRecruitmentWrite;
