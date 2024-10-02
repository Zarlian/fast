import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Modal from 'react-modal';
import {deleteGroupMember, getGroups} from '../../util/apiWrapper';
import SearchBar from '../searchbar/SearchBar';
import { BiImport } from 'react-icons/bi';
import { IoAdd } from 'react-icons/io5';
import ImportModal from "./ImportModal";
import Button from "../elements/Button";
import AddFriendModal from "./AddFriendModal";
import {useSelector} from "react-redux";
import {RiDeleteBinLine} from "react-icons/ri";
import {useSearchFilter} from "../searchbar/SearchFilter";
import {toast} from "react-toastify";
import FriendRequestModal from "./FriendRequestModal";
import {sendToServer} from "../../App";
import {addGroupMembersToMap} from "../map/Map";
import {FaUserFriends} from "react-icons/fa";
import {setUseStateNotificationBadge} from "../sidebars/LeftSideBar";
import {adriaMapSocket} from "../../util/socketHandler";

const CustomModal = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #001f25;
  padding: 1.5rem;
  width: 50%;
  height: 65vh;
  color: #a6eeff;
  border-radius: 10px;
  border: 2px solid #f5bf11;
`;


const CloseButtonContainer = styled.div`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
`;

const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 1rem;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const IconContainer = styled.div`
  display: flex;
  gap: 1rem;
  span {
    cursor: pointer;
    color: #a6eeff;
    &:hover {
      color: #f5bf11;
    }
  }
`;

const iconStyle = {
    fontSize: '2.2rem',
};

const List = styled.ul`
  max-height: calc(100% - 150px);
  overflow-Y: auto;
`;

const Li = styled.li`
      background: #0c2724;
  border-radius: 10px;
  display: flex;
  font-size: 1.5rem;
  margin-bottom: 0.75rem;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem 1rem 0 1rem;

  span {
    cursor: pointer;
    color: #a6eeff;
    &:hover {
      color: #f5bf11;
    }
  }  
`;

const FlexContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`;

let setUseStateFR = () => {};

const FriendListModal = ({ isOpen, onRequestClose }) => {
    const [groupMembers, setGroupMembers] = useState([]);
    const [importModalOpen, setImportModalOpen] = useState(false);
    const [addFriendModalOpen, setAddFriendModalOpen] = useState(false);
    const [friendGroup, setFriendGroup] = useState({});
    const [friendRequestsModalOpen, setFriendRequestsModalOpen] = useState(false);
    const [friendRequestBody, setFriendRequestBody] = useState({});


    const { filteredItems, searchValue, handleSearchChange , updateItems} = useSearchFilter(groupMembers);

    const user = useSelector(state => state.user);

    useEffect(() => {
        const fetchGroupMembers = async () => {
            try {
                const groups = await getGroups(user.adriaId);
                const friendGroup = groups.find((group) => group.name === 'Friends');
                if (friendGroup) {
                    setFriendGroup(friendGroup);
                    setGroupMembers(friendGroup.members);
                    updateItems(friendGroup.members);
                }
            } catch (error) {
                toast.error('Failed to get friends. Please try later again.', {
                    position: toast.POSITION.TOP_RIGHT
                });
            }
        };

        if (isOpen) {
            fetchGroupMembers();
        }
    }, [isOpen, user.adriaId]);

    setUseStateFR = (request) => {
        setFriendRequestBody(request["requests"]);
    };

    const handleAddFriend = () => {
        setAddFriendModalOpen(true);
    };

    const handleCloseAddFriendModal = () => {
        setAddFriendModalOpen(false);
    };

    const handleImport = () => {
        setImportModalOpen(true);
    };

    const handleViewFriendRequests = () => {
        sendToServer({ type: "getFriendRequests" });

        setFriendRequestsModalOpen(true);
    };

    const handleAcceptFriendRequest = async (senderId) => {
        setUseStateNotificationBadge(-1);
        removeFriendRequest(senderId);

        setTimeout(async () => {
            addGroupMembersToMap(groupMembers, adriaMapSocket);
        }, 2000);

        sendToServer({ type: "friendRequestAccept", receiver: senderId, accepted: true});
    };

    const handleDeclineFriendRequest = async (senderId) => {
        setUseStateNotificationBadge(-1);
        removeFriendRequest(senderId);

        sendToServer({ type: "friendRequestAccept", receiver: senderId, accepted: false});
    };

    const removeFriendRequest = (senderId) => {
        const newFriendRequests = { ...friendRequestBody };
        for (const friendRequestBodyKey in friendRequestBody) {
            if (friendRequestBody[friendRequestBodyKey] === senderId) {
                delete newFriendRequests[friendRequestBodyKey];
            }
        }

        setFriendRequestBody(newFriendRequests);
    };

    function handleCloseImportModal() {
        setImportModalOpen(false);
    }

    const updateFriendList = async () => {
        try {
            const groups = await getGroups(user.adriaId);
            const updatedFriendGroup = groups.find((group) => group.name === 'Friends');
            if (updatedFriendGroup) {
                setFriendGroup(updatedFriendGroup);
                setGroupMembers(updatedFriendGroup.members);
                updateItems(updatedFriendGroup.members);
            }
        } catch (error) {
            // Handle error
        }
    };

    async function handleDeleteFriend(adriaId) {
        try {
            const body = {
                IDs: [adriaId]
            };
            const updatedFriends = filteredItems.filter(friend => friend.adriaId !== adriaId);
            updateItems(updatedFriends);

            await deleteGroupMember(friendGroup.id, body);

            toast.success('Friend deleted successfully!', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
        catch (error) {
            toast.error('Failed to delete friend. Please try again later.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }



    }
    return (
        <>
            <CustomModal isOpen={isOpen} onRequestClose={onRequestClose} contentLabel="Friend List">
                <ModalHeader>
                    <Title>Friends</Title>
                    <IconContainer>
                        <span onClick={handleImport}>
                            <BiImport style={iconStyle} />
                        </span>
                        <span onClick={handleAddFriend}>
                            <IoAdd style={iconStyle} />
                        </span>
                        <span onClick={handleViewFriendRequests}>
                            <FaUserFriends style={iconStyle} />
                        </span>
                    </IconContainer>
                </ModalHeader>
                <SearchBar
                    width="100%"
                    color="#001f25"
                    borderColor="#4fd8eb"
                    value={searchValue}
                    onChange={handleSearchChange}
                />
                <List>
                    {filteredItems.map((friend) => (
                        <Li key={friend.adriaId}>
                            <FlexContainer>
                                <img src={`${friend.profilePicture}`} alt={`Profile of ${friend.name}`} />
                                <p>{friend.name}</p>
                                <span className="iconPrivate"></span>
                            </FlexContainer>

                            <span><RiDeleteBinLine onClick={() => handleDeleteFriend(friend.adriaId)}/></span>
                        </Li>
                    ))}
                </List>
                <CloseButtonContainer>
                    <Button onClick={onRequestClose}>Close</Button>
                </CloseButtonContainer>
            </CustomModal>

            {importModalOpen && (
                <ImportModal
                    onClose={handleCloseImportModal}

                    updateFriendList={updateFriendList}
                />
            )}

            {addFriendModalOpen && (
                <AddFriendModal
                    onClose={handleCloseAddFriendModal}

                    currentFriends={groupMembers}

                    //groupId={friendGroup.id}

                    updateFriendList={updateFriendList}
                />
            )}

            {friendRequestsModalOpen && (
                <FriendRequestModal
                    isOpen={friendRequestsModalOpen}
                    onRequestAccept={(senderId) =>
                        handleAcceptFriendRequest(senderId)
                    }
                    onRequestDecline={(senderId) => handleDeclineFriendRequest(senderId)}
                    friendRequests={friendRequestBody}
                    onRequestClose={() => setFriendRequestsModalOpen(false)}
                />
            )}
        </>
    );
};

export default FriendListModal;
export { setUseStateFR };
