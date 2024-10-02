import BaseManagerModal from "./BaseManagerModal";

const TeleportersModal = ({ isOpen, onRequestClose }) => {

    return (
        <>
            <BaseManagerModal isOpen={isOpen} onRequestClose={onRequestClose} data={"Teleporters"}/>
        </>
    );
};

export default TeleportersModal;
