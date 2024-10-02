import React, { useEffect, useState } from 'react';
import ReactDOMServer from 'react-dom/server';
import mapboxgl from 'mapbox-gl';
import {getGroups, getTeleporters, getUserTransaction} from '../../util/apiWrapper';
import MarkerPopup from './MarkerPopup';
import {drawRoute, setMap} from "../header/Buttons";
import {toast} from "react-toastify";
import {setAdriaMap, updateMarkerLocation, adriaMapSocket} from "../../util/socketHandler";
import TeleporterPanel from "../sidebars/TeleporterPanel";
import {updateLocation} from "../../config/user/userActions";
mapboxgl.accessToken =
    'pk.eyJ1IjoidHdhcnJpbm5pZXIiLCJhIjoiY2xvbzlocWMzMDVwbjJqcGEwdzhpYml5ciJ9.gJOziqzcEHEkwa0dhe0sGg';

function findClosestTeleporter(element) {
    while (element && !element.hasAttribute('data-teleporter-id')) {
        element = element.parentElement;
    }
    return element;
}

export let addGroupMembersToMap = () => {};

function showLocation(currentLocation, teleporterId){
    const ownLocation = {
        lon: currentLocation.coords.longitude,
        lat: currentLocation.coords.latitude
    };
    getTeleporters().then(r => {
        r.forEach(teleporter =>{
            if (parseInt(teleporter.id) === parseInt(teleporterId)){
                const correctTeleporter = teleporter;
                const teleporterLocation = {
                    lat: correctTeleporter.teleporterLatitude,
                    lon: correctTeleporter.teleporterLongitude
                };
                drawRoute(ownLocation, teleporterLocation);
            }
        });
    });
}

function locationDenied(){
    console.log("The user doesn't want to share his location");
}

let setNewUserMarker = () => {};

export let dispatchSet = () => {};
export let setUser = () => {};

export let disp;
export let user;
export let closePanel;

let jumpsLeft;

const MapComponent = ({showManagerApp}) => {

    const [teleporters, setTeleporters] = useState([]);
    const [groups, setGroups] = useState([]);
    const [userLocation, setUserLocation] = useState(null);
    const [mapInitialized, setMapInitialized] = useState(false);
    const [showTeleporterPanel, setShowTeleporterPanel] = useState(false);
    const [clickedTeleporter, setClickedTeleporter] = useState(null);

    dispatchSet = (dispatch) => {
        disp = dispatch;
    };

    setUser = (u) => {
        user = u;
    };

    useEffect(() => {
        const setJumps = async () => {
            const jumpsObj = await getUserTransaction(user.adriaId);
            jumpsLeft = jumpsObj["usesLeft"];
        };

        setJumps().then(() => {});
    });

    dispatchSet = (dispatch) => {
        disp = dispatch;
    };

    setUser = (u) => {
        user = u;
    };

    async function updateTeleporters() {
        try {
            const teleportersResult = await getTeleporters();
            setTeleporters(teleportersResult);
        } catch (error) {
            toast.error('Failed to fetch teleporters. Plz reload.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
    }

    async function updateGroups() {
        try {
            const groupsResult = await getGroups(user.adriaId);
            setGroups(groupsResult);
        } catch (error) {
            toast.error('Failed to fetch groups. Plz reload.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
    }

    // useEffect for fetching data
    useEffect(() => {
        const fetchData = () => {

            updateTeleporters().then(() =>{});

            updateGroups().then(() =>{});
        };

        fetchData();

        if ('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const { latitude, longitude } = position.coords;
                    setUserLocation({ lat: latitude, lon: longitude });

                    const newUserLocation = {lon: longitude, lat: latitude};
                    disp(updateLocation(newUserLocation));
                },
                () => {
                    toast.error('Failed to get user location. Plz reload.', {
                        position: toast.POSITION.TOP_RIGHT
                    });
                }
            );
        }
    }, []); // Only run once on mount


    function handleUserMarker(map){
        // User marker
        const userMarker = new mapboxgl.Marker({color: 'red'})
            .setLngLat([userLocation.lon, userLocation.lat])
            .addTo(map)
            .getElement();

        userMarker.title = user.name;
    }

    function createMap() {
        return new mapboxgl.Map({
            container: 'map-container',
            style: 'mapbox://styles/twarrinnier/clooi68b400hd01qoe0jdglwe',
            center: [userLocation.lon, userLocation.lat],
            zoom: 14,
            pitch: 55,
        });
    }

    function addEventToPopupSetDestination(popup, teleporter) {
        popup.getElement().querySelector('.set-destination').addEventListener('click', () => {
            if (jumpsLeft > 0) {
                setShowTeleporterPanel(true);

                setClickedTeleporter(teleporter);

                popup.remove();
            } else {
                toast.error('You have no jumps left!', {
                    position: toast.POSITION.TOP_RIGHT
                });
            }
        });
    }

    function addEventToPopupShowRoute(popup) {
        popup.getElement().querySelector('.show-route').addEventListener('click', (e) => {
            const teleporterId = findClosestTeleporter(e.target).dataset.teleporterId;
            if ("geolocation" in navigator) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    showLocation(position, teleporterId);
                    popup.remove();
                }, locationDenied);
            } else {
                console.error("error getting user location");
            }
        });
    }

    function handlePopup(popup, teleporter) {
        popup.on('open', () => {

            popup.getElement().setAttribute('data-teleporter-id', teleporter.id);

            addEventToPopupShowRoute(popup);

            popup.getElement().querySelector('.favorite').addEventListener('click', () => {
                console.log('clicked favorite');
            });

            addEventToPopupSetDestination(popup, teleporter);
        });
    }

    function handleAddTeleporter(teleporter, teleporterName, map) {
        const popupContent = <MarkerPopup teleporterName={teleporterName} />;

        const popup = new mapboxgl.Popup({ offset: 25 }).setHTML(
            ReactDOMServer.renderToString(popupContent)
        );

        handlePopup(popup, teleporter);

        const style = teleporter.type === 'Private' ? { color: '#f5bf11' } : { color: '#4fd8eb'};

        const marker = new mapboxgl.Marker(style)
            .setLngLat([teleporter.teleporterLongitude, teleporter.teleporterLatitude])
            .setPopup(popup)
            .addTo(map);

        marker.getElement().setAttribute('data-teleporter-id', teleporter.id);
    }

    function addEverythingToMap(initializeMap, teleporters, addGroupMembersToMap, addTeleportersToMap, map) {
        if (!showManagerApp && mapInitialized && groups.length) {
            addGroupMembersToMap();
        }
        if (teleporters.length) {
            addTeleportersToMap(map);
        }
    }

    function addFriendToMap(group, map) {
        group.members.forEach((friend) => {
            const customMarker = document.createElement('div');
            customMarker.className = 'custom-marker';
            customMarker.style.backgroundImage = `url(${friend.profilePicture})`;
            customMarker.style.width = '40px';
            customMarker.style.height = '40px';
            customMarker.style.backgroundSize = 'contain';
            customMarker.style.cursor = 'pointer';

            new mapboxgl.Marker(customMarker)
                .setLngLat([friend.location.lon, friend.location.lat])
                .addTo(map)
                .getElement()
                .setAttribute('title', friend.name);
        });
    }

    function cleanupMap(map) {
        // Cleanup the map instance only if it was initialized
        if (map && mapInitialized) {
            map.remove();
            setMapInitialized(false);
        }
    }

    const addTeleportersToMap = (map) => {
        teleporters
            .filter((teleporter) => teleporter.visible === true)
            .forEach((teleporter) => {
                let teleporterName = teleporter.name;

                if (teleporter.type === 'Private') {
                    teleporterName = teleporter.ownerName + "'s teleporter";
                }

                handleAddTeleporter(teleporter, teleporterName, map);
            });
    };

    const removeMembersFromMap = () => {
        const markers = adriaMapSocket._markers;
        for (const marker in markers) {
            if (markers[marker]._element?.className === 'custom-marker') {
                markers[marker].remove();
            }
        }
    };

    // useEffect for initializing the map
    useEffect(() => {
        let adriaMap;

        addGroupMembersToMap = (givenGroups = groups, map = adriaMap) => {
            //remove all members from map
            removeMembersFromMap();
            givenGroups
                .filter((group) => group.name === 'Friends')
                .forEach((group) =>
                    addFriendToMap(group, map)
                );
        };

        const initializeMap = () => {
            adriaMap = createMap();

            setAdriaMap(adriaMap);
            setMap(adriaMap);

            setMapInitialized(true);

            handleUserMarker(adriaMap);
        };

        if (userLocation) {
            initializeMap();
        }

        addEverythingToMap(initializeMap, teleporters, addGroupMembersToMap, addTeleportersToMap, adriaMap);

        return () => {
           cleanupMap(adriaMap);
        };
    }, [showManagerApp, teleporters, groups, userLocation, mapInitialized]);

    setNewUserMarker = (newPosition) =>{
        updateMarkerLocation(newPosition.lon - 0.0002, newPosition.lat + 0.0002, user.name);
    };

    closePanel = () => {
        setShowTeleporterPanel(false);
    };

    return (
        <>
            <div id="map-container" style={{ width: '100%', height: '100vh' }}></div>
            {showTeleporterPanel && (
                <TeleporterPanel content={clickedTeleporter} onClose={closePanel}>

                </TeleporterPanel>
            )}
        </>
    );
};

export {MapComponent};
