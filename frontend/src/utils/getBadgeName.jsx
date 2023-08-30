export const getBadgeName = (badgeQuantity) => {
    badgeQuantity.forEach((badgeItem) => {
        badgeItem.evaluationBadge = badgeItem.evaluationBadge.replace("_", " ");
    });

    return badgeQuantity;
}