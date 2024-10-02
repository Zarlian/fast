import styled from "styled-components";
import { IoMdExit } from "react-icons/io";
import {FaHistory, FaList} from "react-icons/fa";
import {GiTeleport} from "react-icons/gi";
import {RiAdminLine} from "react-icons/ri";

const Nav = styled.nav`
        font-family: 'Roboto', sans-serif;
        font-size: 1.25rem;
        background: #001f25;
        overflow: hidden;
        position: absolute; 
        width: auto;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
        transition: width 1s ease-in-out;
        box-shadow: -2px 0 5px rgba(0, 0, 0, 0.2);

        border-bottom-right-radius: 10px;
        border-top-right-radius: 10px;
        &:hover {
          width: fit-content;
          transition: width 1s ease-in-out;
          border: 1px solid #f5bf11;
          border-left: none;
        }
        &:hover a span {
          display: block;
        }
    `;

const Ul = styled.ul`
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        gap: 2rem;
    `;

const A = styled.a`
        float: left;
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 1rem;
        color: #a6eeff;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;

      &:hover {
        color: #f5bf11;
        cursor: pointer;
      }
    `;



const SPAN = styled.span`
        display: none;
    `;

const iconStyle = {
    fontSize: '2.2rem',

};

const exitTitle = <SPAN>Exit</SPAN>;
const listsTitle = <SPAN>Lists</SPAN>;
const teleportersTitle = <SPAN>Teleporters</SPAN>;
const adminTitle = <SPAN>Admin</SPAN>;
const historyTitle = <SPAN>History</SPAN>;

function ManagerLeftSideBar({ isOpen }) {

    const handleHomeClick = () => {
        // Logic for handling Home click
    };

    const handleListsClick = () => {
        // Logic for handling Lists click
    };

    const handleTeleportersClick = () => {
        // Logic for handling Teleporters click
    };

    const handleAdminClick = () => {
        // Logic for handling Admin click
    };

    const handleHistoryClick = () => {
        // Logic for handling History click
    };

    return (
        <>
            <Nav>
                <Ul>
                    <li><A onClick={handleListsClick}><FaList style={iconStyle}/> {listsTitle} </A></li>
                    <li><A onClick={handleTeleportersClick}><GiTeleport style={iconStyle}/> {teleportersTitle}</A></li>
                    <li><A onClick={handleAdminClick}><RiAdminLine style={iconStyle}/>{adminTitle}</A></li>
                    <li><A onClick={handleHistoryClick}><FaHistory style={iconStyle}/>{historyTitle}</A></li>
                    <li><A onClick={handleHomeClick}><IoMdExit style={iconStyle} /> {exitTitle}</A></li>
                </Ul>
            </Nav>
        </>
    );
}

export default ManagerLeftSideBar;
