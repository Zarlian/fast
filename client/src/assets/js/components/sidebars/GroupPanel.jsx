import React, {useState, useEffect} from 'react';
import RightPanel from "./RightPanel";
import {getGroups} from '../../util/apiWrapper';
import {MdEdit, MdExpandLess, MdExpandMore} from "react-icons/md";
import styled from "styled-components";
import Button from "../elements/Button";
import GroupModal from "../modals/group/GroupModal";
import {useSelector} from "react-redux";

const Title = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 1rem;
  display: flex;
  justify-content: space-between;

  span {
    cursor: pointer;

    :hover {
      color: #f5bf11;
    }
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
`;

const ListContainer = styled.ul`
  height: calc(100% - 90px);
  overflow-y: auto;
`;

const List = styled.li`
  cursor: pointer;
  &:hover {
    color: ${props => (props.isSelected ? "#a6eeff" : "#f5bf11  ")};
  }
  background: #0c2724;
  padding: 0.75rem 1rem 0.75rem 1rem;
  border-radius: 10px;
  margin-bottom: 0.75rem;
  border: 1px solid ${props => (props.isSelected ? "#f5bf11" : "transparent")};

`;

const InnerList = styled.ul`
  cursor: pointer;
  margin-top: 1rem;
  
  li {
    margin-bottom: 0.5rem;
  }

`;

function GroupPanel({isOpen, onClose}) {
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState(null);
    const [groupModalOpen, setGroupModalOpen] = useState(false);

    const user = useSelector(state => state.user);

    const [friendList, setFriends] = useState([]);

    useEffect(() => {
        async function fetchGroupMembers() {
            try {
                const data = await getGroups(user.adriaId);

                if (data) {
                    const groupData = data.filter((group) => group.name !== 'Friends');
                    setGroups(groupData);
                    const friends = data.find((group) => group.name === 'Friends');
                    setFriends(friends);
                }
            } catch (error) {
                console.error('Error fetching group members', error);
            }
        }

        if (isOpen) {
            fetchGroupMembers();
        }
    }, [isOpen]);

    const updateGroupList = async () => {
        try{
            const data = await getGroups(user.adriaId);
            const groupData = data.filter((group) => group.name !== 'Friends');
            if (groupData) {
                setGroups(groupData);
            }
        }catch (error){
            console.error('Error fetching group members', error);
        }
    };

    const handleItemClick = (groupId) => {
        setSelectedGroup(selectedGroup === groupId ? null : groupId); // Toggle selection
    };

    const handleEdit = () => {
        setGroupModalOpen(true);
    };

    const handleCloseEdit = () => {
        setGroupModalOpen(false);
    };

    return (
        <>
            <RightPanel onClose={onClose} content={
                <>
                    <Title>My travel groups <span title={"Edit groups"}><MdEdit onClick={handleEdit} /></span></Title>
                    <ListContainer>
                        {groups.map((group) => (
                            <List
                                key={group.id}
                                onClick={() => handleItemClick(group.id)}
                                isSelected={group.id === selectedGroup}
                            >
                                {group.name} <span>{selectedGroup === group.id ? <MdExpandLess /> : <MdExpandMore />}</span>
                                {group.id === selectedGroup && (
                                    <InnerList>
                                        <li>Members:</li>
                                        {group.members.map((member) => (
                                            <li key={member.id}>- {member.name}</li>
                                        ))}
                                        <ButtonContainer>
                                            <Button textColor="#a6eeff" bgColor="#001f25" border="1px solid #a6eeff">cancel</Button>
                                            <Button>Set teleport group</Button>
                                        </ButtonContainer>
                                    </InnerList>
                                )}
                            </List>
                        ))}
                    </ListContainer>
                </>
            } />
            {groupModalOpen && (
                <GroupModal
                    isOpen={groupModalOpen}
                    onRequestClose={handleCloseEdit}
                    groups={groups}
                    updateGroupList={updateGroupList}
                    friendList={friendList}
                />
            )}
        </>
    );
}

export default GroupPanel;
