import {
    ADD_FAVOURITE,
    ADD_GROUP,
    ADD_TELEPORTER_HISTORY,
    ADD_TELEPORTER_SETTINGS,
    ADD_USER_HISTORY,
    API_URL,
    DELETE_GROUP,
    DELETE_GROUP_MEMBER, GET_FAVOURITES,
    GET_GROUP,
    GET_GROUPS,
    GET_TELEPORTER_HISTORY,
    GET_TELEPORTER_SETTINGS,
    GET_TELEPORTERS,
    GET_USER,
    GET_USER_HISTORY,
    GET_USER_PERMISSIONS,
    GET_USER_TRANSACTIONS, GET_USERS, IMPORT_FRIENDS_FROM_ADRIA, UPDATE_FAVOURITE,
    UPDATE_GROUP, UPDATE_GROUP_NAME, UPDATE_USER_PERMISSIONS
} from "../config/config";

const TELEPORTER_ID_STRING = '{teleporterId}';

export async function getUser(adriaId){
    const url = `${GET_USER}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function getUsers(){
    const url = `${GET_USERS}`;
    return await getFetch(url, {
        method: "GET"
    });
}

export async function  getGroups(adriaId){
    const url = `${GET_GROUPS}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function getGroup(adriaId, groupId){
    const url = `${GET_GROUP}`.replace('{adriaId}', adriaId).replace('{groupId}', groupId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function addGroup (groupInfo){
    const url = `${ADD_GROUP}`;
    return await getFetch(url, {
        method: "POST",
        body: JSON.stringify(groupInfo)
    });
}

export async function updateGroup (groupId, groupIds){
    const url = `${UPDATE_GROUP}`.replace('{groupId}', groupId);
    return await getFetch(url, {
        method: "PUT",
        body: JSON.stringify(groupIds)
    });
}

export async function updateGroupName (groupId, newGroupName){
    const url = `${UPDATE_GROUP_NAME}`.replace('{groupId}', groupId);
    return await getFetch(url, {
        method: "PUT",
        body: JSON.stringify(newGroupName)
    });
}

export async function deleteGroup (groupId){
    const url = `${DELETE_GROUP}`.replace('{groupId}', groupId);
    return await getFetch(url, {
        method: "DELETE"
    });
}

export async function deleteGroupMember (groupId, groupIds){
    const url = `${DELETE_GROUP_MEMBER}`.replace('{groupId}', groupId);
    return await getFetch(url, {
        method: "PUT",
        body: JSON.stringify(groupIds)
    });
}

export async function getTeleporters(){
    const url = `${GET_TELEPORTERS}`;
    return await getFetch(url, {
        method: "GET"
    });
}

export async function getUserHistory(adriaId){
    const url = `${GET_USER_HISTORY}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function importFriendsFromAdria(){
    const url = `${IMPORT_FRIENDS_FROM_ADRIA}`;
    return await getFetch(url, {
        method: "POST"
    });
}

export async function addUserHistory(adriaId, history){
    const url = `${ADD_USER_HISTORY}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "POST",
        // history conists of: {from, to, departure, arrival, group (nullable), type}
        body: JSON.stringify(history)
    });
}

export async function getTeleporterHistory(teleporterId){
    const url = `${GET_TELEPORTER_HISTORY}`.replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function addTeleporterHistory(teleporterId, history){
    const url = `${ADD_TELEPORTER_HISTORY}`.replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "POST"
    });
}

export async function getTeleporterSettings(teleporterId){
    const url = `${GET_TELEPORTER_SETTINGS}`.replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function addTeleporterSettings(teleporterId, settings){
    const url = `${ADD_TELEPORTER_SETTINGS}`.replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "POST"
    });
}

export async function getUserPermissions(adriaId, teleporterId){
    const url = `${GET_USER_PERMISSIONS}`.replace('{adriaId}', adriaId).replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function updateUserPermissions(adriaId, teleporterId, permissions){
    const url = `${UPDATE_USER_PERMISSIONS}`.replace('{adriaId}', adriaId).replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "PUT"
    });
}

export async function getUserTransaction(adriaId){
    const url = `${GET_USER_TRANSACTIONS}`.replace('{adriaId}', adriaId);
    return await  getFetch(url, {
        method: "GET"
    });
}

export async function getFavourites(adriaId){
    const url = `${GET_FAVOURITES}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "GET"
    });
}

export async function addFavourite(adriaId, favourite){
    const url = `${ADD_FAVOURITE}`.replace('{adriaId}', adriaId);
    return await getFetch(url, {
        method: "POST"
    });
}

export async function updateFavourite(adriaId, teleporterId, favourite){
    const url = `${UPDATE_FAVOURITE}`.replace('{adriaId}', adriaId).replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "PUT"
    });
}

export async function deleteFavourite(adriaId, teleporterId){
    const url = `${DELETE_FAVOURITE}`.replace('{adriaId}', adriaId).replace(TELEPORTER_ID_STRING, teleporterId);
    return await getFetch(url, {
        method: "DELETE"
    });
}



async function getFetch(url, options = {}) {
    const res = await fetch(`${API_URL}${url}`, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                Authorization: `Bearer ${getToken()}`
            }
        });

    return await res.json();
}


export function getToken() {
    return localStorage.getItem('token');
}

export function setToken(token) {
    localStorage.setItem('token', token);
}

export function clearToken() {
    localStorage.removeItem('token');
}
