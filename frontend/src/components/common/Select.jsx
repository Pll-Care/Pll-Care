/**
 * select Option data가 들어와야 됨
 * option을 클릭하면 클릭한 값을 받을 함수가 필요함
 */

const Select = ({ options, type = "small", onChange }) => {
  return (
    <select onChange={onChange} className="select_small">
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.title}
        </option>
      ))}
    </select>
  );
};

export default Select;
