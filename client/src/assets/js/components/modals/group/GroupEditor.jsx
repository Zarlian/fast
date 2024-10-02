import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import {RiDeleteBinLine} from "react-icons/ri";
import {IoAdd} from "react-icons/io5";
import {colors, fonts, styles} from "../../../util/Theme";
import {deleteGroupMember} from "../../../util/apiWrapper";
import {toast} from "react-toastify";
import AddGroupMemberModal from "./AddGroupMemberModal";
import {MdEdit} from "react-icons/md";
import EditGroupNameModal from "./EditGroupNameModal";

const CustomModal = styled.div`

  background-color: #001f25;
  padding: 1.5rem;
  width: 66%;
  height: 100%;
  border-radius: 10px;
  border: 1px solid #f5bf11;

  span {
    cursor: pointer;
    display: flex;
    align-items: center;

    &:hover {
      color: ${colors.dark.hover};
    }
  }

  ul {
    height: calc(100% - 95px);
    overflow-y: auto;
  }
`;

const List = styled.li`
  font-size: ${fonts.size.text};
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
  line-height: 2rem;
  padding: 0.5rem;
  background-color: ${colors.dark.onBackground};
  border-radius: ${styles.borderRadius};
`;

const Title = styled.h2`
  font-size: 2rem;
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

const iconStyleDelete = {
    fontSize: fonts.size.iconSmall,
};

const Container = styled.div`
    display: flex;
  align-items: center;
    `;

const GroupEditor = ({selectedGroup, updateGroupList, friendList}) => {


    const [addMemberModalOpen, setAddMemberModalOpen] = useState(false);
    const [group, setGroup] = useState();
    const [editGroupName, setEditGroupName] = useState(false);


    useEffect(() => {
        setGroup(selectedGroup);
    }, [selectedGroup]);


    async function handleDeleteMember(adriaId) {
        try {
            const body = {
                IDs: [adriaId]
            };

            if (group && group.members !== undefined && group.members !== null) {
                group.members = group.members.filter(member => member.adriaId !== adriaId);
            }

            await deleteGroupMember(selectedGroup.id, body);
            updateGroupList();
            toast.success('Group member deleted successfully!', {
                position: toast.POSITION.TOP_RIGHT
            });
        } catch (error) {
            toast.error('Failed to delete group member. Please try again later.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
    }


    function handleAdd() {
        setAddMemberModalOpen(true);
    }

    function handleCloseAddMemberModal() {
        setAddMemberModalOpen(false);
    }

    function handleEdit() {
        setEditGroupName(true);
    }

    function handleCloseEdit() {
        setEditGroupName(false);
    }

    return (
        <>
            <CustomModal isOpen={selectedGroup !== null} contentLabel="My groups">
                <ModalHeader>
                    <Container>
                        <Title>{selectedGroup ? selectedGroup.name : 'No group selected'}</Title>
                        {selectedGroup && (
                            <span>
                                <MdEdit style={iconStyleDelete} onClick={handleEdit}/>
                            </span>
                        )}
                    </Container>
                    {selectedGroup && (
                        <span onClick={handleAdd} title={"Add member"}>
                            <IoAdd style={iconStyle}/>
                        </span>
                    )}
                </ModalHeader>
                <ul>
                    {selectedGroup && selectedGroup.members.length > 0 && selectedGroup.members ? selectedGroup.members.map(member => (
                    <List key={member.adriaId}>
                        <p>{member.name}</p>
                        <span onClick={() => handleDeleteMember(member.adriaId)}>
                                    <RiDeleteBinLine style={iconStyleDelete}/>
                                </span>
                    </List>
                    )) : <p>No members</p>}
                </ul>
            </CustomModal>
            {addMemberModalOpen && (
                <AddGroupMemberModal
                    onClose={handleCloseAddMemberModal}
                    groupId={selectedGroup.id}
                    friendList={friendList}
                    updateGroupList={updateGroupList}
                />
            )}
            {editGroupName && (
                <EditGroupNameModal
                    isOpen={editGroupName}
                    onRequestClose={handleCloseEdit}
                    groupId={selectedGroup.id}
                    updateGroupList={updateGroupList}
                />
            )}
        </>
    );
};

export default GroupEditor;
