import React, { useState, useEffect } from 'react';
import RightPanel from "./RightPanel";
import {getFavourites } from '../../util/apiWrapper';
import {IoIosHome} from "react-icons/io";
import {FaShoppingBag, FaSuitcase} from "react-icons/fa";
import {TbFlag3} from "react-icons/tb";
import {useSelector} from "react-redux";

function FavouritePanel({ isOpen, onClose }) {
    const [favourites, setFavourites] = useState([]);

    const user = useSelector(state => state.user);

    useEffect(() => {
        async function fetchFavourites() {
            try {
                const data = await getFavourites(user.adriaId);
                    setFavourites(data);

            } catch (error) {
                console.error('Error fetching group members', error);
            }
        }

        if (isOpen) {
            fetchFavourites();
        }
    }, [isOpen]);


    const icon = {
        Home : <IoIosHome />,
        Work : <FaSuitcase />,
        Shop : <FaShoppingBag />,
        Default: <TbFlag3 />
    };

    return (
        <RightPanel onClose={onClose} content={
            <>
                <h2>My favourites</h2>
                <ul>
                    {favourites.map(favourite => (
                        <li key={favourite.id}><span>{icon[favourite.type]}</span> {favourite.name}</li>
                    ))}
                </ul>
            </>
        } />
    );
}

export default FavouritePanel;


