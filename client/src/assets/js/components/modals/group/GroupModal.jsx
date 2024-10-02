import React, { useState } from 'react';
import styled from 'styled-components';
import Modal from 'react-modal';
import GroupList from './GroupList';
import GroupEditor from './GroupEditor';

const CustomModal = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50%;
  height: 60%;
  color: #a6eeff;
  outline: none;
`;

const ModalContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  width: 100%;
`;

const GroupModal = ({ isOpen, onRequestClose, groups, updateGroupList, friendList }) => {
    const [selectedGroup, setSelectedGroup] = useState(null);

    const handleGroupSelect = (group) => {
        setSelectedGroup(group);
    };

    return (
        <CustomModal isOpen={isOpen} onRequestClose={onRequestClose} contentLabel="Edit Groups">
            <ModalContainer>
                <GroupList groups={groups} onSelect={handleGroupSelect} selectedGroup={selectedGroup} updateGroupList={updateGroupList} friendList={friendList}/>
                <GroupEditor selectedGroup={selectedGroup} updateGroupList={updateGroupList} friendList={friendList} />
            </ModalContainer>
        </CustomModal>
    );
};

export default GroupModal;
