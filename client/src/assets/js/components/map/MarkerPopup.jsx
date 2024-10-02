import styled from "styled-components";
import {IoIosHeartEmpty, IoMdHeart} from "react-icons/io";

const PopupContainer = styled.div`
      padding: 0;
      margin: 0;
      background-color: #00363d;
      border-radius: 10px;
      border: 1px solid #001f25;
    `;

const Button = styled.button`
      background-color: #f5bf11;
      color: #3e2e00;
      font-size: 20px;
      border-radius: 10px;
      border: none;
      text-align: center;
      width: 100%;
      cursor: pointer;
      margin-bottom: 1rem;
    `;

const Text = styled.div`
        color: #4fd8eb;
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 1rem;
    `;

const Icon = styled.div`
      margin-bottom: 1rem;
      margin-left: 1rem;
      padding-top: 1rem;
      cursor: pointer;
    `;

const IconStyle = {
    fontSize: '2.2rem',
    color: 'white'
};

const MarkerPopup = ({ teleporterName, isFavorite }) => {

    return (
        <PopupContainer>
            <Icon className={'favorite'}>
                {!isFavorite ?
                <span><IoIosHeartEmpty style={IconStyle}/></span>
                :
                <span><IoMdHeart style={IconStyle}/></span>
                }
            </Icon>

            <div style={{ padding: '10px' }}>
                <Text>{teleporterName}</Text>
                <div>
                    <Button className={'show-route'}>Show route</Button>
                </div>
                <Button className={'set-destination'}>Set destination</Button>
            </div>
        </PopupContainer>
    );
};

export default MarkerPopup;
