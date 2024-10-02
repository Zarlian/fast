import React, {useEffect, useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import Button from "../elements/Button";
import SearchBar from "../searchbar/SearchBar";
import {getUsers} from "../../util/apiWrapper";
import {useSearchFilter} from "../searchbar/SearchFilter";
import {useSelector} from "react-redux";
import { toast } from 'react-toastify';
import {ClipLoader} from "react-spinners";
import {sendToServer} from "../../App";

const CustomModalAdd = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #001f25;
  padding: 1.5rem;
  width: 50%;
  max-width: 500px;
  height: 58vh;
  color: #a6eeff;
  border-radius: 10px;
  border: 2px solid #f5bf11;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
`;

const Title = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 1rem;
`;

const Div = styled.div`
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ButtonContainer = styled(Div)`
  flex-direction: row;
  justify-content: center;
  align-items: center; /
  margin-top: 1rem;
  gap: 2rem;
`;



const List = styled.ul`
  height: calc(100% - 150px);
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

const AddFriendModal = ({ onClose , currentFriends, updateFriendList}) => {
    const [isOpen, setIsOpen] = useState(true);
    const [users, setUsers] = useState([]);
    const { filteredItems, searchValue, handleSearchChange , updateItems} = useSearchFilter(users);
    const [isLoading, setIsLoading] = useState(false);
    const [loadingStates, setLoadingStates] = useState({});

    const user = useSelector(state => state.user);

    useEffect(() => {
        async function fetchUsers() {
            try {
                const fetchedUsers = await getUsers();
                const filteredUsers = fetchedUsers.filter(
                    fetchedUser => fetchedUser.adriaId !== user.adriaId && !currentFriends.some(friend => friend.adriaId === fetchedUser.adriaId)
                );
                setUsers(filteredUsers);
                updateItems(filteredUsers);
            } catch (error) {
                toast.error('Failed to fetch users. Please try again later.', {
                    position: toast.POSITION.TOP_RIGHT
                });
            }
        }

        if(isOpen){
            fetchUsers();
        }
    }, [isOpen]);

    const handleCancel = () => {
        if (!isLoading) {
            setIsOpen(false);
            onClose();
        }
    };

    async function handleAddFriend(adriaId) {
        try {
            sendToServer({type: "friendRequest", receiver: adriaId});

            toast.success('Friend added successfully!', {
                position: toast.POSITION.TOP_RIGHT
            });

            const updatedFriends = filteredItems.filter(friend => friend.adriaId !== adriaId);
            updateItems(updatedFriends);
            updateFriendList();

        } catch (error) {
            toast.error('Failed to add friend. Please try again later.', {
                position: toast.POSITION.TOP_RIGHT
            });
        } finally {
            setIsLoading(false);
            setLoadingStates(prevStates => ({
                ...prevStates,
                [adriaId]: false
            }));
        }
    }

    return (
        <CustomModalAdd isOpen={isOpen} onRequestClose={handleCancel} contentLabel="Add friend modal">
            <Div>
                <Title>Add friend</Title>
                <p>Search friend .</p>
            </Div>
            <SearchBar width={45} value={searchValue} onChange={handleSearchChange} />
            <List>
                {filteredItems.map(friend => (
                    <Li key={friend.adriaId}>
                        <FlexContainer>
                            <img src={`${friend.profilePicture}`} alt={`Profile of ${friend.name}`} width={40}/>
                            <p>{friend.name}</p>
                            <span className="iconPrivate"></span>
                        </FlexContainer>

                        {loadingStates[friend.adriaId] ? (
                            <ClipLoader color="#f5bf11" size={20} />
                        ) : (
                            <Button onClick={() => handleAddFriend(friend.adriaId)}>Add</Button>
                        )}
                    </Li>
                ))}
            </List>
            <ButtonContainer>

                <Button
                    onClick={handleCancel}
                    textColor="#a6eeff"
                    bgColor="#001f25"
                    border="1px solid #a6eeff"
                >
                    Cancel
                </Button>
            </ButtonContainer>

        </CustomModalAdd>
    );
};

export default AddFriendModal;
