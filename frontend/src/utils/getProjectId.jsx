export const getProjectId = (location) => {
    return parseInt(location.pathname.slice(12, 14));
}
