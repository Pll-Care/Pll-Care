const StackItem = ({ imageUrl, name, onClick, type = "basic" }) => {
  return (
    <li className="stackItem">
      <div className="stackItem_image">
        <img src={imageUrl} alt={name} />
      </div>
      <div>
        <span>{name}</span>
      </div>
      <div>
        {type === "change" && (
          <button className="stackItem_button" name={name} onClick={onClick}>
            x
          </button>
        )}
      </div>
    </li>
  );
};

export default StackItem;
