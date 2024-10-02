import React, { useState} from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import Button from "../../elements/Button";
import SearchBar from "../../searchbar/SearchBar";
import {useSearchFilter} from "../../searchbar/SearchFilter";
import {toast} from 'react-toastify';
import {ClipLoader} from "react-spinners";
import {updateGroup} from "../../../util/apiWrapper";

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
  align-items: center;
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
  cursor: pointer;
`;

const AddGroupMemberModal = ({onClose, groupId, friendList, updateGroupList}) => {

    const [isOpen, setIsOpen] = useState(true);
    const {filteredItems, searchValue, handleSearchChange, updateItems} = useSearchFilter(friendList.members);
    const [isLoading, setIsLoading] = useState(false);
    const [loadingStates, setLoadingStates] = useState({});
    const [selectedMembers, setSelectedMembers] = useState([]);

    const handleCancel = () => {
        if (!isLoading) {
            setIsOpen(false);
            onClose();
        }
    };

    const handleCheckboxChange = (adriaId) => {
        const isSelected = selectedMembers.includes(adriaId);

        setSelectedMembers((prevSelected) =>
            isSelected
                ? prevSelected.filter((id) => id !== adriaId)
                : [...prevSelected, adriaId]
        );
    };

    function  createBody(adriaId) {
        return {
            IDs: [adriaId]
        };
    }

    function showLoader(adriaId) {
        setIsLoading(true);
        setLoadingStates(prevStates => ({
            ...prevStates,
            [adriaId]: true
        }));
    }

    async function handleAddMember(adriaId) {
        try {
            showLoader(adriaId);
            const body =  createBody(adriaId);
            await updateGroup(groupId, body);
            updateGroupList();
            toast.success('Member added successfully!', {
                position: toast.POSITION.TOP_RIGHT
            });
            const updatedMembers = filteredItems.filter(friend => friend.adriaId !== adriaId);
            updateItems(updatedMembers);
        } catch (error) {
            toast.error('Failed to add Member. Please try again later.', {
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

    async function handleAddSelected() {
        try {
            const body = {
                IDs: selectedMembers
            };
            await updateGroup(groupId, body);
            updateGroupList();
            onClose();
            toast.success('Members added successfully!', {
                position: toast.POSITION.TOP_RIGHT
            });
            const updatedMembers = filteredItems.filter(friend => !selectedMembers.includes(friend.adriaId));
            updateItems(updatedMembers);
            setSelectedMembers([]);
        } catch (error) {
            toast.error('Failed to add Members. Please try again later.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
    }

    return (
        <CustomModalAdd isOpen={isOpen} onRequestClose={handleCancel} contentLabel="Add friend modal">
            <Div>
                <Title>Add member(s) to the group</Title>
                <p>Search from your friendList .</p>
            </Div>
            <SearchBar width={45} value={searchValue} onChange={handleSearchChange}/>
            <List>
                {filteredItems.map(friend => (
                    <Li key={friend.adriaId}>
                        <label>
                            <FlexContainer>
                                <input
                                    type={"checkbox"}
                                    onChange={() => handleCheckboxChange(friend.adriaId)}
                                    checked={selectedMembers.includes(friend.adriaId)}
                                />

                                <img src={`${friend.profilePicture}`} alt={`Profile of ${friend.name}`} width={40}/>
                                <p>{friend.name}</p>
                                <span className="iconPrivate"></span>
                            </FlexContainer>
                        </label>

                        {loadingStates[friend.adriaId] ? (
                            <ClipLoader color="#f5bf11" size={20}/>
                        ) : (
                            <Button onClick={() => handleAddMember(friend.adriaId)}>Add</Button>
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
                <Button onClick={handleAddSelected}>
                    Add Selected
                </Button>
            </ButtonContainer>

        </CustomModalAdd>
    );
};

export default AddGroupMemberModal;
