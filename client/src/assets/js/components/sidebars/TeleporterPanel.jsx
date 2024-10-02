import React from 'react';
import styled from 'styled-components';
import Button from "../elements/Button";
import {updateMarkerLocation} from "../../util/socketHandler";
import {addUserHistory, getTeleporters} from "../../util/apiWrapper";
import {getDistance, updateJumpCount, removeRoute} from "../header/Buttons";
import {closePanel, disp, user} from "../map/Map";
import {updateLocation} from "../../config/user/userActions";

const PanelWrapper = styled.div`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  right: 1vw;
  height: 30%;
  width: 200px;
  background-color: #001f25;
  padding: 20px;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  border: 1px solid #f5bf11;
  color: #a6eeff;
`;

const CloseButtonContainer = styled.div`
  position: absolute;
  bottom: 1rem;
  right: 30%;
`;

const ContentWrapper = styled.div`
    height: 100%;
`;
let fromTeleporterId;

const TeleporterPanel = ({ onClose, content }) => {
    const [walkedToClosestTeleporter, setWalkedToClosestTeleporter] = React.useState(false);


    const handleShiftPress = () => {
        if (content != null && walkedToClosestTeleporter) {

            const locationNextToTeleporter = {
                lon: parseFloat(content.teleporterLongitude) - 0.0008,
                lat: parseFloat(content.teleporterLatitude) + 0.0002
            };
            disp(updateLocation(locationNextToTeleporter));
            updateMarkerLocation(locationNextToTeleporter.lon, locationNextToTeleporter.lat, "FAST");

            const departure = new Date();
            // arrival is 2 minutes later
            const arrival = new Date(departure.getTime() + 2 * 60000);

            const history = {
                from: fromTeleporterId,
                to: content.id,
                arrival: arrival,
                departure: departure,
                group: null,
                type: "userHistory"
            };

            addUserHistory(user.adriaId, history);

            updateJumpCount();
            closePanel();
        }
    };

    document.querySelector('body').addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleEnterPress();
        } else if (e.key === 'Shift') {
            handleShiftPress();
        }
    });

    const currentLocation = user.location;
    async function handleWalking(){
        let closest = await getClosestTeleporter();

        closest = {
            lon: closest.lon - 0.0008,
            lat: closest.lat + 0.0002
        };
        // update the marker location
        updateMarkerLocation(closest.lon, closest.lat, "FAST");

        // set the state to true
        setWalkedToClosestTeleporter(true);
    }

    function setTeleportersLocations(teleporters) {
        const teleportersLocations = [];
        teleporters.forEach(teleporter => {
            if (teleporter.type === "Private") {
                return;
            }
            teleportersLocations.push({
                lat: teleporter.teleporterLatitude,
                lon: teleporter.teleporterLongitude
            });
        });

        return teleportersLocations;
    }

    async function getClosestTeleporter() {
        let minDistance = Infinity;
        let closestPoint = null;

        const teleporters = await getTeleporters();

        const teleportersLocations = setTeleportersLocations(teleporters);

        let teleporterIndex = 0;

        for (const teleportersLocation in teleportersLocations) {

            if(!teleportersLocation) {
                console.error("teleportersLocation is undefined");
            }

            const point = teleportersLocations[teleportersLocation];

            const distance = getDistance(currentLocation.lon, currentLocation.lat, point.lon, point.lat);
            if (distance < minDistance) {
                minDistance = distance;
                teleporterIndex = teleportersLocation;
                closestPoint = point;
            }
        }

        fromTeleporterId = teleporters[teleporterIndex].id;

        return closestPoint;
    }

    // when clicked on enter, walk to the closest teleporter
    const handleEnterPress = () => {
        removeRoute();
        if (content != null && !walkedToClosestTeleporter) {
            handleWalking();
        }
    };

    return (
        <PanelWrapper>
            <ContentWrapper>
                {content.name}
            </ContentWrapper>
            <CloseButtonContainer>
                <Button onClick={onClose}>Cancel</Button>
            </CloseButtonContainer>

        </PanelWrapper>
    );
};

export default TeleporterPanel;
