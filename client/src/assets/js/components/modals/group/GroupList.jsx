import React, {useState} from 'react';
import styled from 'styled-components';
import {IoAdd} from "react-icons/io5";
import {colors, styles, fonts} from "../../../util/Theme";
import CreateGroupModal from "./CreateGroupModal";
import {RiDeleteBinLine} from "react-icons/ri";
import {deleteGroup} from "../../../util/apiWrapper";
import {toast} from "react-toastify";

const CustomModal = styled.div`

  background-color: ${colors.dark.background};
  padding: 1.5rem;
  height: 100%;
  width: 32%;
  border-radius: ${styles.borderRadius};
  border: ${styles.borderThickness} solid ${colors.dark.border};
  
    span {
    cursor: pointer;
      &:hover {
        color: ${colors.dark.hover};
      }
    }
  
  
  ul {
    height: calc(100% - 125px);
    overflow-y: auto;
  }
`;


const Title = styled.h2`
  font-size: ${fonts.size.title};
  margin-bottom: 1rem;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
`;

const iconStyle = {
    fontSize: fonts.size.icon
};

const IconContainer = styled.div`
  span {
    cursor: pointer;

    &:hover {
      color: ${colors.dark.hover};
    }
  }
`;


const List = styled.li`
  font-size: ${fonts.size.text};
  cursor: pointer;
  background: ${colors.dark.onBackground};
  padding: 0.75rem 1rem 0.75rem 1rem;
  border-radius: ${styles.borderRadius};
  margin-bottom: 0.75rem;
  border: ${styles.borderThickness} solid ${props => (props.isSelected ? colors.dark.border : "transparent")};
  
  display: flex;
    justify-content: space-between;
`;


const GroupList = ({groups, onSelect, selectedGroup,friendList, updateGroupList}) => {
    const [addGroupModalOpen, setAddGroupModalOpen] = useState(false);

    function handleCloseGroupModal() {
        setAddGroupModalOpen(false);
    }

    function handleAddGroupModal() {
        setAddGroupModalOpen(true);
    }

    async function handleDeleteGroup(groupId) {
        try{
            await deleteGroup(groupId);
            updateGroupList();
            toast.success("Group deleted");
        } catch (e) {
            toast.error("Could not delete group");
        }
    }

    return (
        <>
            <CustomModal contentLabel="My groups">
                <ModalHeader>
                    <Title>My Groups</Title>
                    <IconContainer>
                    <span onClick={handleAddGroupModal} title={"Add group"}>
                        <IoAdd style={iconStyle}/>
                    </span>
                    </IconContainer>
                </ModalHeader>
                <ul>
                    {groups.map(group => (
                        <List
                            key={group.id}
                            isSelected={selectedGroup && selectedGroup.id === group.id}
                            onClick={() => onSelect(group)}
                        >
                            {group.name}<span
                            onClick={() => handleDeleteGroup(group.id)}><RiDeleteBinLine
                            /></span>
                        </List>
                    ))}
                </ul>
            </CustomModal>
            {addGroupModalOpen && (
                <CreateGroupModal
                    isOpen={addGroupModalOpen}
                    onRequestClose={handleCloseGroupModal}
                    updateGroupList={updateGroupList}
                    friendList={friendList}
                />
            )}
        </>
    );
};

export default GroupList;
