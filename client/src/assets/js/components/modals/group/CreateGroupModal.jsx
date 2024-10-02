import styled from "styled-components";
import Modal from "react-modal";
import Button from "../../elements/Button";
import {useSelector} from "react-redux";
import React, {useState} from "react";
import {addGroup} from "../../../util/apiWrapper";
import {toast} from "react-toastify";
import AddGroupMemberModal from "./AddGroupMemberModal";

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
  height: 30vh;
  color: #a6eeff;
  border-radius: 10px;
  border: 2px solid #f5bf11;
`;

const Title = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 1rem;
`;

const Div = styled.div`
  margin-bottom: 1rem;
  align-items: center;
`;

const ButtonContainer = styled(Div)`
  flex-direction: row;
  justify-content: center;
  align-items: center; /
  margin-top: 1rem;
  gap: 2rem;
`;

const CreateGroupModal = ({ isOpen, onRequestClose, updateGroupList, friendList }) => {
    const [groupName, setGroupName] = useState("");
    const [addGroupMemberModalOpen, setAddGroupMemberModalOpen] = useState(false);
    const [createdGroupId, setCreatedGroupId] = useState(null);

    const user = useSelector(state => state.user);


    function handleAddGroupMember() {
        createGroup();

    }

    function handleCloseAddMemberModal() {
        setAddGroupMemberModalOpen(false);
    }

    async function createGroup() {
        const body = {
            name : groupName,
            leader : user.adriaId,
            members : []
        };

        try{
            const newGroupId = await addGroup(body);
            setCreatedGroupId(newGroupId);
            setAddGroupMemberModalOpen(true);
        } catch (e) {
            toast("Error creating group!");
        }
    }

    const handleInputChange = (event) => {
        setGroupName(event.target.value);
    };

    return (
        <>
        <CustomModalAdd isOpen={isOpen} onRequestClose={onRequestClose} contentLabel="Create Group">
            <Title>Create Group</Title>
            <Div>
                <label htmlFor={groupName}>Group name: </label>
                <input
                    type={"text"}
                    placeholder="Group Name"
                    value={groupName}
                    onChange={handleInputChange}
                />
            </Div>
            <ButtonContainer>
                <Button onClick={onRequestClose}>Cancel</Button>
                <Button onClick={handleAddGroupMember}>Create group and add members</Button>
            </ButtonContainer>
        </CustomModalAdd>
    {addGroupMemberModalOpen && (
        <AddGroupMemberModal
            onClose={handleCloseAddMemberModal}
            groupId={createdGroupId.ID}
            friendList={friendList}
            updateGroupList={updateGroupList}
        />
    )}
        </>
    );
};

export default CreateGroupModal;
