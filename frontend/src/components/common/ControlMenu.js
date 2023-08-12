const ControlMenu = ({ onChange, value, optionList, size }) => {
  return (
    <select
      className={size === "small" ? "control-menu-small" : "control-menu"}
      onChange={(e) => onChange(e.target.value)}
      value={value}
    >
      {optionList.map((opt) => (
        <>
          <option key={opt.id} value={opt.value}>
            {opt.name}
          </option>
        </>
      ))}
    </select>
  );
};

export default ControlMenu;
