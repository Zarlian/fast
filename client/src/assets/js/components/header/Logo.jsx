import styled from "styled-components";

const LogoStyle = styled.div`
        overflow: hidden;
        position: relative;
        width: auto;
        transition: width 1s ease-in-out;
        &:hover {
          width: fit-content;
          transition: width 1s ease-in-out;
        }
    `;

function Logo() {

    return (
        <LogoStyle>
            <img src="assets/images/logo.png" alt="logo" width="100px" height="100px"/>
        </LogoStyle>
    );
}

export default Logo;
