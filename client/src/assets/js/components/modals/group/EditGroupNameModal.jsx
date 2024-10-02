import styled from "styled-components";
import Modal from "react-modal";
import Button from "../../elements/Button";
import React, {useState} from "react";
import {toast} from "react-toastify";
import {updateGroupName} from "../../../util/apiWrapper";

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

const EditGroupNameModal = ({ isOpen, onRequestClose, groupId, updateGroupList}) => {
    const [newGroupName, setNewGroupName] = useState("");

    async function handleGroupNameChange() {
        try {
            const body = {
                "name": newGroupName
            };
            await updateGroupName(groupId, body);
            toast.success("Group name changed successfully");
            updateGroupList();
            onRequestClose();
        }catch (e) {
            toast.error("Error changing group name");
        }
    }
    const handleInputChange = (event) => {
        setNewGroupName(event.target.value);
    };

    return (
        <>
            <CustomModalAdd isOpen={isOpen} onRequestClose={onRequestClose} contentLabel="Edit name">
                <Title>Rename Group</Title>
                <Div>
                    <label htmlFor={newGroupName}>Group name: </label>
                    <input
                        type={"text"}
                        placeholder="New group name"
                        value={newGroupName}
                        onChange={handleInputChange}
                    />
                </Div>
                <ButtonContainer>
                    <Button onClick={onRequestClose}>Cancel</Button>
                    <Button onClick={handleGroupNameChange}>Edit group name</Button>
                </ButtonContainer>
            </CustomModalAdd>
        </>
    );
};

export default EditGroupNameModal;
