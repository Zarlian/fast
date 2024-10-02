import styled from "styled-components";
import Modal from "react-modal";
import {useSelector} from "react-redux";
import Button from "../elements/Button";
import React from "react";

const CustomModalRequest = styled(Modal)`
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

const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 1rem;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const AcceptButton = styled.button`
    background-color: #f5bf11;
    border: none;
    border-radius: 10px;
    padding: 0.5rem 1rem;
    font-size: 1.5rem;
    color: #001f25;
    cursor: pointer;
`;

const DeclineButton = styled.button`
    background-color: #f5bf11;
    border: none;
    border-radius: 10px;
    padding: 0.5rem 1rem;
    font-size: 1.5rem;
    color: #001f25;
    cursor: pointer;
`;

const FriendRequestRow = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
    padding: 0.5rem 1rem;
    border-radius: 10px;
    background-color: #0c2724;
    color: #a6eeff;
    font-size: 1.5rem;
`;

const CloseButtonContainer = styled.div`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
`;

const FriendRequestAcctions = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
`;

const FriendRequestModal = ({ isOpen, onRequestAccept, onRequestDecline, friendRequests, onRequestClose }) => {
    const user = useSelector(state => state.user);

    return (
        <CustomModalRequest
            isOpen={isOpen}
            contentLabel="Friend Request"
        >
            <ModalHeader>
                <Title>Friend Requests</Title>
            </ModalHeader>
            {(friendRequests === undefined || friendRequests.length === 0 ?
                <p>You have no friend requests</p>
                : (
                    // Use for...in loop instead of forEach
                    (() => {
                        const friendRequestKeys = Object.keys(friendRequests);
                        for (const key of friendRequestKeys) {
                            const friendRequest = friendRequests[key];

                            return (
                                <FriendRequestRow key={key}>
                                    <p>{key} wants to be your friend!</p>
                                    <FriendRequestAcctions>
                                        <AcceptButton onClick={() => onRequestAccept(friendRequest, 1, user)}>Accept</AcceptButton>
                                        <DeclineButton onClick={() => onRequestDecline(friendRequest)}>Decline</DeclineButton>
                                    </FriendRequestAcctions>
                                </FriendRequestRow>
                            );
                        }

                        return <></>;
                    })()
                ))}
            <CloseButtonContainer>
                <Button onClick={onRequestClose}>Close</Button>
            </CloseButtonContainer>
        </CustomModalRequest>
    );
};

export default FriendRequestModal;
