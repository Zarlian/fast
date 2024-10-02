import { useState } from 'react';
import styled from 'styled-components';
import FriendListModal from "../modals/FriendListModal";
import HistoryModal from "../modals/HistoryModal";
import { IoLocationSharp } from "react-icons/io5";
import {HiOutlineClipboardDocumentList} from "react-icons/hi2";
import {addGroupMembersToMap, user} from "../map/Map.jsx";
import {SiNginxproxymanager} from "react-icons/si";
import {getGroups} from "../../util/apiWrapper";
import {adriaMapSocket} from "../../util/socketHandler";
import {RiContactsBook2Line} from "react-icons/ri";

let setUseStateNotificationBadge = () => {};

function LeftSideBar({onManagerClick}) {
    const [isFriendsModalOpen, setIsFriendsModalOpen] = useState(false);
    const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);
    const [isFriendRequests, setIsFriendRequests] = useState(0);

    const handleHomeClick = () => {
        if ("geolocation" in navigator){
            showLocation(user.location);
        } else {
            console.error("error getting user location");
        }
    };

    const handleManagerClick = () => {
        onManagerClick();
    };

    setUseStateNotificationBadge = (amountOfRequests) => {
        if(amountOfRequests < 0) {
            setIsFriendRequests(isFriendRequests + amountOfRequests);
        } else {
            setIsFriendRequests(amountOfRequests);
        }
    };


    function showLocation(currentLocation){
        const cords = [currentLocation.lon, currentLocation.lat];
        adriaMapSocket.flyTo({
            center: cords,
            zoom: 16,
            essential: true,
            pitch: 55,
            bearing: 0
            }
        );
    }

    const handleFriendListClick = () => {
        setIsFriendsModalOpen(true);
    };

    const closeFriendListModal = async () => {
        setIsFriendsModalOpen(false);

        const groups = await getGroups(user.adriaId);
        addGroupMembersToMap(groups, adriaMapSocket);
    };

    const handleHistoryClick = () => {
        setIsHistoryModalOpen(true);
    };

    const closeHistoryModal = async () => {
        setIsHistoryModalOpen(false);

        const groups = await getGroups(user.adriaId);
        addGroupMembersToMap(groups, adriaMapSocket);
    };


    const Nav = styled.nav`
        font-family: 'Roboto', sans-serif;
        font-size: 1.25rem;
        background: #001f25;
        overflow: hidden;
        position: absolute; 
        width: auto;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
        transition: width 1s ease-in-out;
        box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);

        border-bottom-right-radius: 10px;
        border-top-right-radius: 10px;
        &:hover {
          width: fit-content;
          transition: width 1s ease-in-out;
          border: 1px solid #f5bf11;
          border-left: none;
        }
        &:hover a span {
          display: block;
        }
    `;

    const Ul = styled.ul`
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        gap: 2rem;
    `;

    const A = styled.a`
        float: left;
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 1rem;
        color: #a6eeff;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;

      &:hover {
        color: #f5bf11;
        cursor: pointer;
      }
    `;

    // inherit the styles from the A component
    const BadgeIcon = styled.div`
        position: relative;
        display: flex;
        justify-content: center;
        align-items: center;
      
          &::after {
            content: "1";
            position: relative;
            top: 0;
            right: 40%;
            transform: translate(50%, -80%);
            width: 20px;
            height: 20px;
            background: #f5bf11;
            color: #001f25;
            border-radius: 50%;
            text-align: center;
            line-height: 20px;
            font-size: 1rem;
          }
    `;



    const SPAN = styled.span`
        display: none;
    `;

    const iconStyle = {
        fontSize: '2.2rem',

    };


    const homeTitle = <SPAN>My Location</SPAN>;
    const friendListTitle = <SPAN>Friend List</SPAN>;
    const historyTitle = <SPAN>History</SPAN>;


    return (
        <>
            <Nav>
                <Ul>
                    <li><A onClick={handleHomeClick}><IoLocationSharp style={iconStyle} /> {homeTitle}</A></li>
                    <li>
                        <A onClick={handleFriendListClick}>
                            {isFriendRequests > 0 ? <BadgeIcon><RiContactsBook2Line  style={iconStyle}/></BadgeIcon> : <RiContactsBook2Line  style={iconStyle}/>}
                            {friendListTitle}
                        </A>
                    </li>
                    <li><A onClick={handleHistoryClick}><HiOutlineClipboardDocumentList style={iconStyle}/> {historyTitle}</A></li>
                    <li><A onClick={handleManagerClick}><SiNginxproxymanager style={iconStyle} color={'#a6eeff'} /><SPAN>Manager</SPAN></A></li>
                </Ul>
            </Nav>

            <FriendListModal
                isOpen={isFriendsModalOpen}
                onRequestClose={closeFriendListModal}
            />

            <HistoryModal
                isOpen={isHistoryModalOpen}
                onRequestClose={closeHistoryModal}
            />
        </>
    );
}

export default LeftSideBar;

export { setUseStateNotificationBadge };
