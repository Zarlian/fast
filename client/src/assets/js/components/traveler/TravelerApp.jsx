// YourComponent.js
import React from "react";
import {MapComponent} from "../map/Map";
import LeftSideBar from "../sidebars/LeftSideBar";
import Header from "../header/Header";

const TravelerApp = ({ showManagerApp, handleManagerClick }) => {

    return (
        <div>
            <MapComponent showManagerApp={showManagerApp} />
            <LeftSideBar onManagerClick={handleManagerClick} />
            <Header />
        </div>
    );
};
export default TravelerApp;
