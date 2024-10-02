import {getToken} from "./apiWrapper";
import {TOKENS} from "../config/config";
import {setUseStateFR} from "../components/modals/FriendListModal";
import {setUseStateNotificationBadge} from "../components/sidebars/LeftSideBar";

const CHNL_TO_SERVER = "events.to.server";
const EVENTBUS_PATH = "https://project-2.ti.howest.be/2023-2024/group-04/events";
const CHNL_TO_CLIENT_UNICAST = "events.to.client.";

let sender;
export let adriaMapSocket;

export function openSocket() {
    const eb = new EventBus(EVENTBUS_PATH);

    sender = getToken();

    function sendToServer(message) {
        message.sender = sender;
        eb.send(CHNL_TO_SERVER, message);
    }

    eb.onopen = function () {
        // Only register the handler and set up the sendToServer function when the connection is open
        eb.registerHandler(CHNL_TO_CLIENT_UNICAST + sender, onMessage);
    };

    return sendToServer;
}

function onMessage(error, message) {
    const body = message.body;

    const type = body.type;

    switch(type){
        case "location":
            handleIncomingLocation(body);
            break;
        case "friendRequest":
            handleIncomingFriendRequest();
            break;
        case "friendRequests":
            handleIncomingGetFriendRequests(body);
            break;
        default:
            console.log("Unknown message received");
            break;
    }
}

export function updateMarkerLocation(longitude, latitude, sender) {

    //remove duplicate markers
    removeDuplicateMarkers(sender);

    if (!adriaMapSocket) {
        return;
    }

    const markers = adriaMapSocket._markers;

    let senderMarker;

    for (const marker in markers) {
        if (!markers[marker]) {
            console.error("Marker is undefined");
        }

        const attributes = markers[marker]?._element?.attributes;

        let title = null;
        if (attributes) {
            title = attributes.getNamedItem("title")?.value;
        }

        if (title === sender && title !== null) {
            senderMarker = markers[marker];
            break;
        }
    }

    handleSetLonLat(senderMarker, longitude, parseFloat(latitude));
}

function handleSetLonLat(senderMarker, longitude, latitude){
    if (!senderMarker) {
        return;
    }

    senderMarker.setLngLat([longitude, latitude]);
}

function removeDuplicateMarkers(sender){
    if (!adriaMapSocket) {
        return;
    }

    const markers = adriaMapSocket._markers;

    const senderMarkers = [];

    for (const marker in markers) {
        if (!markers[marker]) {
            console.error("Marker is undefined");
        }

        const attributes = markers[marker]._element.attributes;

        const title = attributes.getNamedItem("title")?.value;
        if (title === sender && title !== undefined) {
            senderMarkers.push(markers[marker]);
        }
    }

    handleRemoveDuplicateMarkers(senderMarkers);
}

function handleRemoveDuplicateMarkers(senderMarkers){
    if (senderMarkers.length > 0) {
        for (let i = 1; i < senderMarkers.length; i++) {
            senderMarkers[i].remove();
        }
    } else {
        console.log("No marker found for sender " + sender);
    }
}

function handleIncomingLocation(body){
    updateMarkerLocation(body.longitude, body.latitude, body.sender);
}

function handleIncomingGetFriendRequests(body){
    setUseStateFR(body);
    let amountOfFriendRequests = 0;
    const requests = body["requests"];
    for (const bodyRequestsKey in requests) {
        if (requests[bodyRequestsKey]) {
            amountOfFriendRequests++;
        }
    }

    setUseStateNotificationBadge(amountOfFriendRequests);
}

function handleIncomingFriendRequest(){
    setUseStateNotificationBadge(true);
}

export function determineToken() {
    const userAgent = navigator.userAgent;
    let token;

    // Check if the browser is Internet Explorer
    if (userAgent.indexOf("Chrome") !== -1) {
        token = TOKENS[0]; // Chrome
    } else if (userAgent.indexOf("Firefox") !== -1) {
        token = TOKENS[1]; // Firefox
    }

    return token;
}

const locationsOfWalk = [
    [51.211223, 3.217941],
    [51.211529, 3.220174],
    [51.210964, 3.220854],
    [51.209759, 3.219848],
    [51.208715, 3.221745],
    [51.207861, 3.220588],
    [51.208709, 3.218995],
    [51.209771, 3.219819],
    [51.210524, 3.218456],
    [51.209781, 3.216410],
    [51.211145, 3.215655],
    [51.211698, 3.217325]
];

export function simulateWalkingBasedOnLocations() {
    let currentIndex = 0;

    function getNextLocation() {
        const nextLocation = locationsOfWalk[currentIndex];

        currentIndex = (currentIndex + 1) % locationsOfWalk.length;

        return {
            latitude: nextLocation[0],
            longitude: nextLocation[1]
        };
    }

    return getNextLocation;
}

export function setAdriaMap(map) {
    adriaMapSocket = map;
}
