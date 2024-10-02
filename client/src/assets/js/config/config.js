//export const API_URL = "https://project-2.ti.howest.be/2023-2024/group-04/api";


// Description: All the API endpoints are defined here
export const GET_USER = "/user";
export const GET_USERS = "/users";
export const GET_GROUPS = "/groups";
export const GET_GROUP = "/users/group/{groupId}";
export const ADD_GROUP = "/group/add";
export const UPDATE_GROUP = "/group/users/{groupId}";
export const UPDATE_GROUP_NAME = "/group/{groupId}";
export const DELETE_GROUP = "/group/{groupId}";
export const DELETE_GROUP_MEMBER = "/group/users/{groupId}";
export const IMPORT_FRIENDS_FROM_ADRIA = "/group/members";
export const GET_TELEPORTERS = "/teleporters";
export const GET_USER_HISTORY = "/users/history";
export const ADD_USER_HISTORY = "/users/history";
export const GET_TELEPORTER_HISTORY = "/teleporters/history/{teleporterId}";
export const ADD_TELEPORTER_HISTORY = "/teleporters/history/{teleporterId}";
export const GET_TELEPORTER_SETTINGS = "/teleporters/settings/{teleporterId}";
export const ADD_TELEPORTER_SETTINGS = "/teleporters/settings/{teleporterId}";
export const GET_USER_PERMISSIONS = "/user/teleporters/permissions/{teleporterId}";
export const UPDATE_USER_PERMISSIONS = "/user/teleporters/permissions/{teleporterId}";
export const GET_USER_TRANSACTIONS = "/users/transactions";
export const GET_FAVOURITES = "/users/favourites";
export const ADD_FAVOURITE = "/users/favourites";
export const UPDATE_FAVOURITE = "/users/favourites/{teleporterId}";
export const DELETE_FAVOURITE = "/users/favourites/{teleporterId}";

export const TOKENS = [
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyLCJvdGhlckRhdGEiOiJ3aGF0ZXZlciIsImlhdCI6MTcwMTI1MDE4MX0.1p4lqWyaGajDiFOle2AOvnuDrz3S_A0ONVLaOUb9BYA",
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzAxOTQ2NjcwLCJleHAiOjE3MDE5NTAyNzB9.zamzW9ojqt2HWSKKUfXHUK7GVyp747Zt5B0Pf3j33t4"
];
