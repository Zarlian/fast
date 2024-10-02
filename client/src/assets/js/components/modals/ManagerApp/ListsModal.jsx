import BaseManagerModal from "./BaseManagerModal";

const ListsModal = ({ isOpen, onRequestClose }) => {

    return (
        <>
            <BaseManagerModal isOpen={isOpen} onRequestClose={onRequestClose} data={"Lists"}/>
        </>
    );
};

export default ListsModal;
