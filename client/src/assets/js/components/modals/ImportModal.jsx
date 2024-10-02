import React, {useState} from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { PacmanLoader } from 'react-spinners';
import Button from "../elements/Button";
import {importFriendsFromAdria} from "../../util/apiWrapper";
import {toast} from "react-toastify";

const CustomModalImport = styled(Modal)`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #001f25;
  padding: 1.5rem;
  width: 50%; /* Adjusted width */
  max-width: 400px; /* Set a maximum width */
  height: 50%; /* Adjusted height */
  max-height: 300px; /* Set a maximum height */
  color: #a6eeff;
  border-radius: 10px;
  border: 2px solid #f5bf11;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const LoaderContainer = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: transparent;
  z-index: 9999;
  display: ${(props) => (props.show ? 'block' : 'none')};
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
      align-items: center; /
      margin-top: 1rem; 
      gap: 2rem;
    `;

const ImportModal = ({ onClose , updateFriendList}) => {
    const [isOpen, setIsOpen] = useState(true);
    const [isImporting, setIsImporting] = useState(false);
    const [importClicked, setImportClicked] = useState(false);


    const handleCancel = () => {
        setIsOpen(false);
        onClose();
    };

    async function  handleImport () {

        try{
            setIsImporting(true);
            setImportClicked(true);

            await importFriendsFromAdria();
            updateFriendList();
            toast.success('Imported friends from Adriabook!', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
        catch (error) {
            toast.error('Failed to import friends from Adriabook!', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
        finally {
            setIsImporting(false);
            setIsOpen(false);
            onClose();
        }
    }



    return (
        <CustomModalImport isOpen={isOpen} onRequestClose={handleCancel} contentLabel="Import Modal">
            <Div>
                <Title>Import from Adriabook</Title>
                <p>You can import your friendlist from Adriabook.</p>
            </Div>
            <ButtonContainer>
                <Button
                    onClick={handleCancel}
                    textColor="#a6eeff"
                    bgColor="#001f25"
                    border="1px solid #a6eeff"
                    disabled={isImporting}
                    visible={!isImporting}
                >
                    Cancel
                </Button>

                <LoaderContainer show={importClicked}>
                    <PacmanLoader color="#f5bf11" />
                </LoaderContainer>
                <Button onClick={handleImport} disabled={isImporting} visible={!isImporting}>
                    Import
                </Button>
            </ButtonContainer>
            <p>By clicking 'Import,' you agree to share your Adriabook friend list for a better app experience.</p>
        </CustomModalImport>
    );
};

export default ImportModal;
