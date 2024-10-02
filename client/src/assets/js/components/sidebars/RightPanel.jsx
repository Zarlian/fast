import React from 'react';
import styled from 'styled-components';
import Button from "../elements/Button";

const PanelWrapper = styled.div`
  font-family: 'Roboto', sans-serif;
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  right: 0;
  height: 50%;
  width: 300px; /* Adjust the width as needed */
  background-color: #001f25;
  padding: 20px;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);
  border-bottom-left-radius: 10px;
  border-top-left-radius: 10px;
  border: 1px solid #f5bf11;
  color: #a6eeff;
  border-right: none;
`;

const CloseButtonContainer = styled.div`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
`;

const ContentWrapper = styled.div`
    height: 100%;
`;

const RightPanel = ({ onClose, content }) => {


    return (
        <PanelWrapper>
            <ContentWrapper>
                {content}
            </ContentWrapper>
            <CloseButtonContainer>
                <Button onClick={onClose}>Close</Button>
            </CloseButtonContainer>

        </PanelWrapper>
    );
};

export default RightPanel;
