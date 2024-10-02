import styled from "styled-components";
import Modal from "react-modal";
import Button from "../../elements/Button";
import React from "react";
import ManagerLeftSideBar from "../../sidebars/ManagerLeftSideBar";

const CustomModal = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 1.5rem;
  width: 100%;
  height: 100vh;
  border-radius: 10px;
  border: 2px solid #f5bf11;
`;

const ModalContent = styled.div`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #001f25;
  padding: 1.5rem;
  width: 75%;
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

const BaseManagerModal = ({ isOpen, onRequestClose, data }) => {
    return (
        <>
            {isOpen && <ManagerLeftSideBar isOpen={isOpen} />}

            <CustomModal isOpen={isOpen}  contentLabel="Friend List">
                <ManagerLeftSideBar isOpen={isOpen}/>
                    <ModalContent isOpen={isOpen}>
                        {data}
                        <CloseButtonContainer>
                            <Button onClick={onRequestClose}>Close</Button>
                        </CloseButtonContainer>
                    </ModalContent>
            </CustomModal>
        </>
        );
};

export default BaseManagerModal;
