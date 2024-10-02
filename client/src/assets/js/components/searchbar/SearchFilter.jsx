import {useEffect, useState} from "react";

export const useSearchFilter = (initialItems = []) => {
    const [items, setItems] = useState(initialItems);
    const [filteredItems, setFilteredItems] = useState(initialItems);
    const [searchValue, setSearchValue] = useState('');

    useEffect(() => {
        setFilteredItems(
            items.filter((item) =>
                item.name.toLowerCase().includes(String(searchValue).toLowerCase())
            )
        );
    }, [searchValue, items]);

    const handleSearchChange = (value) => {
        setSearchValue(value);
    };

    const updateItems = (updatedItems) => {
        setItems(updatedItems);
    };


    return { filteredItems, searchValue, handleSearchChange, updateItems };
};
