import BaseManagerModal from "./BaseManagerModal";

const AdminModal = ({ isOpen, onRequestClose }) => {

    return (
        <>
            <BaseManagerModal isOpen={isOpen} onRequestClose={onRequestClose} data={"Admin"}/>
        </>
    );
};

export default AdminModal;
