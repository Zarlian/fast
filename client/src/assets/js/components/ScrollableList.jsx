import React from 'react';
import styled from 'styled-components';
import { RiDeleteBinLine } from "react-icons/ri";

const List = styled.ul`
  max-height: calc(100% - 150px);
  overflow-Y: auto;
`;

const Li = styled.li`
      background: #0c2724;
  border-radius: 10px;
  display: flex;
  font-size: 1.5rem;
  margin-bottom: 0.75rem;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem 1rem 0rem 1rem;

  span {
    cursor: pointer;
    color: #a6eeff;
    &:hover {
      color: #f5bf11;
    }
  }  
`;

const FlexContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`;

const ScrollableList = ({ dataList }) => {
    return (
        <List>
            {dataList.map((data) => (
                <Li key={data.name}>
                    <FlexContainer>
                        <img src={`${data.profilePicture}`} alt={`Profile of ${data.name}`} />
                        <p>{data.name}</p>
                        <span className="iconPrivate"></span>
                    </FlexContainer>

                    <span><RiDeleteBinLine /></span>
                </Li>
            ))}

            {dataList.map((data) => (
                <Li key={data.name}>
                    <FlexContainer>
                        <img src={`${data.profilePicture}`} alt={`Profile of ${data.name}`} />
                        <p>{data.name}</p>
                        <span className="iconPrivate"></span>
                    </FlexContainer>

                    <span><RiDeleteBinLine /></span>
                </Li>
            ))}

        </List>
    );
};

export default ScrollableList;
