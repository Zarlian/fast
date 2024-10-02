import styled from 'styled-components';

const Button = styled.button`
  background-color: ${(props) => props.bgColor || '#a6eeff'};
  color: ${(props) => props.textColor || '#001f25'};
  padding: 0.5rem 1rem;
  border: ${(props) => props.border || '1px solid #a6eeff'};
  border-radius: 5px;
  cursor: pointer;
  display: ${(props) => (props.visible !== false ? 'inline-block' : 'none')};

  &:hover {
    background-color: #f5bf11;
    color: #3e2e00;
    border-color: #f5bf11;
  }
  
`;

export default Button;
