import BaseManagerModal from "./BaseManagerModal";

const ManagerHistoryModal = ({ isOpen, onRequestClose }) => {

    return (
        <>
            <BaseManagerModal isOpen={isOpen} onRequestClose={onRequestClose} data={"History"}/>
        </>
    );
};

export default ManagerHistoryModal;
