import styled from "styled-components";
import React from 'react';

const SearchBarContainer = styled.div`
  position: relative;
  width: ${(props) => props.width || "25%"};
  margin-bottom: 0.75rem;
`;

const SearchInput = styled.input.attrs((props) => ({
    style: {
        borderColor: props.isFocused ? "#f5bf11" : "#a6eeff",
        color: props.isFocused ? "#f5bf11" : "#fff",
    },
}))`
  width: 100%;
  padding: 0.5rem;
  border: 1px solid;
  outline: none;
  font-size: 1rem;
  border-radius: 10px;
  background: #001f25;
  transition: border-color 0.3s, color 0.3s;
`;

function SearchBar({ width, value, onChange }) {
    const [isFocused, setIsFocused] = React.useState(false);

    const handleInputChange = (event) => {
        const { value } = event.target;
        onChange(value); // Pass the value to the onChange function
    };

    return (
        <SearchBarContainer width={width}>
            <SearchInput
                type="text"
                placeholder="Search..."
                isFocused={isFocused}
                value={value}
                onChange={handleInputChange} // Use the modified handler
                onFocus={() => setIsFocused(true)}
                onBlur={() => setIsFocused(false)}
            />
        </SearchBarContainer>
    );
}

export default SearchBar;
