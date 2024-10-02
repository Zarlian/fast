import { useState, useEffect } from 'react';
import styled from "styled-components";
import {getUserTransaction, getTeleporters} from "../../util/apiWrapper";
import GroupPanel from "../sidebars/GroupPanel";
import {MdFavorite} from "react-icons/md";
import {HiUserGroup} from "react-icons/hi";
import {GiTeleport} from "react-icons/gi";
import FavouritePanel from "../sidebars/FavouritePanel";
import {FaRoute} from "react-icons/fa";
import mapboxgl from "mapbox-gl";
import {user} from "../map/Map";

mapboxgl.accessToken =
    'pk.eyJ1IjoidHdhcnJpbm5pZXIiLCJhIjoiY2xvbzlocWMzMDVwbjJqcGEwdzhpYml5ciJ9.gJOziqzcEHEkwa0dhe0sGg';

let drawRoute;
let adriaMap;
let getDistance;

const map = {
    baseUrl: 'https://api.mapbox.com/directions/v5/mapbox/walking/',
    queryParams: 'alternatives=true&continue_straight=true&geometries=geojson&language=en&overview=full&steps=true'
};

function setMap(map){
    adriaMap = map;
}
function removeRoute(){
    if (adriaMap.getSource("route")){
        adriaMap.removeLayer("route");
        adriaMap.removeSource("route");
    }
}

export let updateJumpCount;

function ActionButtons() {
    const [jumpsLeft, setJumpsLeft] = useState(null);
    const [isGroupPanelOpen, setIsGroupPanelOpen] = useState(false);
    const [isFavouritePanelOpen, setIsFavouritePanelOpen] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const jumps = await getUserTransaction(1);
                setJumpsLeft(jumps);
            } catch (error) {
                console.error('Error fetching jumps:', error);
            }
        };

        fetchData();
    }, [jumpsLeft]);

    updateJumpCount = async () => {
        try {
            const jumpsObj = await getUserTransaction(1);
            const jumps = jumpsObj["usesLeft"];
            setJumpsLeft(jumps);
        } catch (error) {
            console.error('Error fetching jumps:', error);
        }
    };

    const ActionButtonsDiv = styled.div`
        display: flex;
        align-items: center;
        gap: 1rem;
        overflow: hidden;
        position: relative;
        width: fit-content;
        transition: width 1s ease-in-out;
        border-radius: 10px;
    `;

    const ActionButton = styled.div`
        background: #001f25;
        overflow: hidden;
        position: relative;
        width: 25%;
        aspect-ratio: 1;
        transition: width 1s ease-in-out;
        border-radius: 1rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: .5rem;
        color: #a6eeff;
        border: 1px solid #001f25;
      
        &:hover {
          color: #f5bf11;
          border-color: #f5bf11;
          cursor: pointer;
        }
    `;

const Teleports = styled(ActionButton)`
        background: none;
        flex-direction: row;
        border: none;
          &:hover {
            color: #a6eeff;
            cursor: default;
          }
        svg {
            width: 7rem;
            aspect-ratio: 1;
        }
    `;

const Names = styled.p`
        font-size: 1.5rem;
        font-weight: bold;
        color: white;
    `;

const TotalTeleports = styled(Names)`
        font-size: 2rem;
        color: #a6eeff;
    `;

const iconStyle = {
    fontSize: '2.2rem',
};

    const handleFavouritesClick = () => {
        setIsFavouritePanelOpen(true);
        setIsGroupPanelOpen(false);
    };

    const closeFavouritesPanel = () => {
        setIsFavouritePanelOpen(false);
    };

    const handleGroupsClick = () => {
        setIsGroupPanelOpen(true);
        setIsFavouritePanelOpen(false);
    };

    const closeGroupPanel = () => {
        setIsGroupPanelOpen(false);
    };


    function handleClosestRoute() {
        if ("geolocation" in navigator){
            showLocation(user.location);
        } else {
            console.error("error getting user location", error);
        }

    }

    function showLocation(ownLocation){
        getTeleporters().then(r =>{
            const teleportersLocations = [];
            r.forEach(teleporter =>{
                if (teleporter.type === "Private"){
                    return;
                }
                teleportersLocations.push({
                    lat: teleporter.teleporterLatitude,
                    lon: teleporter.teleporterLongitude
                });
            });
            let closestPoint;
            let minDistance = Infinity;

            teleportersLocations.forEach(point =>{
                const distance = getDistance(ownLocation.lon, ownLocation.lat, point.lon, point.lat);
                if (distance < minDistance){
                    minDistance = distance;
                    closestPoint = point;
                }
            });

            drawRoute(ownLocation, closestPoint);
        });
    }

    drawRoute = function (begin, end){
        fetch(`${map.baseUrl}${begin.lon},${begin.lat};${end.lon},${end.lat}?${map.queryParams}&access_token=${mapboxgl.accessToken}`)
            .then(r => r.json())
            .then(data =>{
                const routeCoordinates = data.routes[0].geometry.coordinates;
                if (adriaMap.getSource("route")){
                    adriaMap.getSource("route").setData({
                        type: 'Feature',
                        properties: {},
                        geometry: {
                            type: 'LineString',
                            coordinates: routeCoordinates,
                        }
                    });
                } else {
                    addRoute(routeCoordinates);
                }
            })
            .catch(error => console.error('Error:', error));
    };

    function addRoute(coordinates){
        adriaMap.addLayer({
            id: 'route',
            type: 'line',
            source: {
                type: 'geojson',
                data: {
                    type: 'Feature',
                    properties: {},
                    geometry: {
                        type: 'LineString',
                        coordinates: coordinates,
                    },
                },
            }, layout: {
                'line-join': 'round',
                'line-cap': 'round',
            }, paint: {
                'line-color': '#3887be',
                'line-width': 5,
                'line-opacity': 0.75,
            },
        });
    }

    getDistance = (long1, lat1, long2, lat2) => {
        const R = 6371e3; // Earth radius in meters

        const sigma1 = (lat1 * Math.PI) / 180;
        const sigma2 = (lat2 * Math.PI) / 180;

        const sigmadif = ((lat2 - lat1) * Math.PI) / 180;
        const lambdadif = ((long2 - long1) * Math.PI) / 180;

        const a = Math.sin(sigmadif / 2) * Math.sin(sigmadif / 2) +
            Math.cos(sigma1) * Math.cos(sigma2) * Math.sin(lambdadif / 2) * Math.sin(lambdadif / 2);

        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    };

    return (
        <>
            <ActionButtonsDiv>
                <ActionButton title="Favorites" onClick={handleFavouritesClick}>
                    <MdFavorite style={iconStyle}/>
                </ActionButton>
                <ActionButton title="Groups" onClick={handleGroupsClick}>
                    <HiUserGroup style={iconStyle}/>
                </ActionButton>
                <ActionButton title="Closest teleporter">
                    <FaRoute style={iconStyle} onClick={handleClosestRoute}/>
                </ActionButton>


                {jumpsLeft !== null && (
                    <Teleports title={`Total Teleports left: ${jumpsLeft.usesLeft}`}>
                        <GiTeleport style={iconStyle}/>
                        <TotalTeleports>{jumpsLeft.usesLeft}</TotalTeleports>
                    </Teleports>
                )}
            </ActionButtonsDiv>
            {isGroupPanelOpen && <GroupPanel isOpen={isGroupPanelOpen} onClose={closeGroupPanel} />}
            {isFavouritePanelOpen && <FavouritePanel isOpen={isFavouritePanelOpen} onClose={closeFavouritesPanel} />}
        </>

    );
}



export {drawRoute, setMap, getDistance, adriaMap, removeRoute};
export default ActionButtons;

