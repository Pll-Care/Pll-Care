const ControlMenu = ({ onChange, value, optionList }) => {
    return (
        <select
            className='control-menu'
            onChange={(e) => onChange(e.target.value)}
            value={value}
        >
            {optionList.map((opt) => (
                <option key={opt.id} value={opt.value}>{opt.name}</option>
            ))}
        </select>
    )
}

export default ControlMenu;