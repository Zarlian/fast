export const SET_USER = 'SET_USER';
export const UPDATE_LOCATION = 'UPDATE_LOCATION';
export const GET_USER = 'GET_USER';

export const setUser = (user) => ({
    type: SET_USER,
    payload: user,
});

export const updateLocation = (location) => ({
    type: UPDATE_LOCATION,
    payload: location,
});

export const getUser = () => ({
    type: GET_USER
});
