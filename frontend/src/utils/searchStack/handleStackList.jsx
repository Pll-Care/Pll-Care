export const isStackinList = (stackList, select) => {
  let result = false;

  stackList.forEach((item) => {
    if (item.name === select) result = true;
  });

  return result;
};

export const filterSelectStack = (stackList, select) => {
  return stackList.filter((item) => item.name !== select);
};
