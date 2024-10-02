import React, {useState} from "react";
import Button from "../elements/Button";
import {FaHistory, FaList} from "react-icons/fa";
import {GiTeleport} from "react-icons/gi";
import {RiAdminLine} from "react-icons/ri";
import styled from "styled-components";
import { IoMdExit } from "react-icons/io";
import ListsModal from "../modals/ManagerApp/ListsModal";
import TeleportersModal from "../modals/ManagerApp/TeleportersModal";
import AdminModal from "../modals/ManagerApp/AdminModal";
import ManagerHistoryModal from "../modals/ManagerApp/ManagerHistoryModal";
import ManagerLeftSideBar from "../sidebars/ManagerLeftSideBar";


const IconContainer = styled.div`

  height: 100vh;
  display: flex;
  gap: 2rem;
  align-items: center;
  justify-content: center;
  background-color: #505454;
`;

const IconButton = styled(Button)`
    width: 10rem;
    height: 10rem;
`;

const iconStyle = {
    fontSize: '7rem',
};


const ManagerApp = ({ handleTravelerClick }) => {
    const [isListsModalOpen, setIsListsModalOpen] = useState(false);
    const [isTeleportersModalOpen, setIsTeleportersModalOpen] = useState(false);
    const [isAdminModalOpen, setIsAdminModalOpen] = useState(false);
    const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);

    function handleListsModal() {
        setIsListsModalOpen(true);
    }

    function closeListsModal() {
        setIsListsModalOpen(false);
    }

    function handleTeleportersModal() {
        setIsTeleportersModalOpen(true);
    }

    function closeTeleportersModal() {
        setIsTeleportersModalOpen(false);
    }

    function handleAdminModal() {
        setIsAdminModalOpen(true);
    }

    function closeAdminModal() {
        setIsAdminModalOpen(false);
    }

    function handleHistoryModal() {
        setIsHistoryModalOpen(true);
    }

    function closeHistoryModal() {
        setIsHistoryModalOpen(false);
    }

    const canShowLeftSideBar = isListsModalOpen || isTeleportersModalOpen || isAdminModalOpen || isHistoryModalOpen;

    return (
        <>
            <IconContainer>
                <IconButton onClick={handleListsModal}><FaList style={iconStyle} />Lists</IconButton>
                <IconButton onClick={handleTeleportersModal}><GiTeleport style={iconStyle} />Teleporters</IconButton>
                <IconButton onClick={handleAdminModal}><RiAdminLine style={iconStyle} />Admin</IconButton>
                <IconButton onClick={handleHistoryModal}><FaHistory style={iconStyle} />History</IconButton>
                <IconButton onClick={handleTravelerClick}><IoMdExit style={iconStyle} />Exit</IconButton>
            </IconContainer>



            <ListsModal
                isOpen={isListsModalOpen}
                onRequestClose={closeListsModal}
            />
            <TeleportersModal
                isOpen={isTeleportersModalOpen}
                onRequestClose={closeTeleportersModal}
            />
            <AdminModal
                isOpen={isAdminModalOpen}
                onRequestClose={closeAdminModal}
            />
            <ManagerHistoryModal
                isOpen={isHistoryModalOpen}
                onRequestClose={closeHistoryModal}
            />

            {canShowLeftSideBar ? (
                <ManagerLeftSideBar isOpen={true} />
            ) : null}


        </>
    );
};

export default ManagerApp;
