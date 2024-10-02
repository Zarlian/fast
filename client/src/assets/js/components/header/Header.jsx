import styled from "styled-components";
import Logo from "./Logo";
import SearchBar from "../searchbar/SearchBar";
import ActionButtons from "./Buttons";

const HEADER = styled.div`
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        position: absolute;
        top: 2%;
        padding: 0 1rem;
    `;

function Header() {


    return (
        <HEADER>
            <Logo></Logo>
            <SearchBar></SearchBar>
            <ActionButtons></ActionButtons>
        </HEADER>
    );
}

export default Header;
