import React, {useState, useEffect} from 'react';
import styled from 'styled-components';
import Modal from 'react-modal';
import Button from "../elements/Button";
import { getUserHistory } from '../../util/apiWrapper';
import {useSelector} from "react-redux";

const CustomModal = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #001f25;
  padding: 1.5rem;
  width: 50%;
  height: 60%;
  color: #a6eeff;
  border-radius: 10px;
  border: 2px solid #f5bf11;
`;

const CloseButtonContainer = styled.div`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
`;

const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 1rem;
`;

const Table = styled.table`
  border-collapse: separate;
  margin-top: 1rem; /* Add margin to separate the table from other content */
  border-spacing: 1.5rem;  
  overflow: auto;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const HistoryModal = ({ isOpen, onRequestClose }) => {
    const [history, setHistory] = useState([]);

    const user = useSelector(state => state.user);

    useEffect(() => {
        async function fetchHistory() {
            try {
                const data = await getUserHistory(user.adriaId);
                setHistory(data);
            } catch (error) {
                console.error('Error fetching user history', error);
            }
        }

        if (isOpen) {
            fetchHistory();
        }
    }, [isOpen, user.adriaId]);

    const scroll = {
        overflowY: "scroll",
        height: "80%"
    };

    return (
        <CustomModal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="History Modal"
        >
            <ModalHeader>
                <Title>History</Title>
            </ModalHeader>
            <div style={scroll}>
            <Table>
                <thead>
                <tr>
                    <th>Arrival</th>
                    <th>Destination</th>
                </tr>
                </thead>
                <tbody>
                {history.map((item, index) => (
                    <tr key={index}>
                        <td>{item.arrival}</td>
                        <td>{item.destination}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
            </div>
            <CloseButtonContainer>
                <Button onClick={onRequestClose}>Close</Button>
            </CloseButtonContainer>
        </CustomModal>
    );
};

export default HistoryModal;
