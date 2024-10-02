import {SET_USER, UPDATE_LOCATION, GET_USER} from './userActions';

const initialState = {
    user: {
        name: "FAST",
        address: "Fastlane 12, 8000 Bruges",
        location: {
            lon: "3.226324",
            lat: "51.209367"
        },
        profilePicture: "assets/images/users/fast.png",
        adriaId: 1
    },
};

const userReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_USER:
            return {
                ...state,
                user: action.payload,
            };
        case UPDATE_LOCATION:
            return {
                ...state,
                user: {
                    ...state.user,
                    location: action.payload,
                },
            };
        case GET_USER:
            return {
                ...state.user
            };
        default:
            return state;
    }
};

export default userReducer;
