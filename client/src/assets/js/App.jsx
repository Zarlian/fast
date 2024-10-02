import { createRoot } from 'react-dom/client';
import { useState } from 'react';
import ManagerApp from "./components/manager/ManagerApp";
import TravelerApp from "./components/traveler/TravelerApp";
import {Provider, useDispatch, useSelector} from "react-redux";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {clearToken, setToken} from "./util/apiWrapper";
import {determineToken, openSocket, simulateWalkingBasedOnLocations} from "./util/socketHandler";
import {TOKENS} from "./config/config";
import {dispatchSet, setUser} from "./components/map/Map";
import userReducer from "./config/user/userReducer";
import {createStore} from "redux";

// Clear the existing HTML content
document.body.innerHTML = '<div id="app"></div>';
// Render your React component instead
const root = createRoot(document.getElementById('app'));

const store = createStore(userReducer);

export let sendToServer;

function App() {
    const [showManagerApp, setShowManagerApp] = useState(false);

    const dispatch = useDispatch();
    dispatchSet(dispatch);
    const user = useSelector(state => state.user);
    setUser(user);



    clearToken();

    const token = determineToken();

    if (token) {
        setToken(token);
    }

    sendToServer = openSocket();

    setTimeout(() => {
        sendToServer({ type: "getFriendRequests" });
    }, 5000);

    function handleManagerClick() {
        setShowManagerApp(true);
    }

    function handleTravelerClick() {
        setShowManagerApp(false);
    }

    let locationInterval;

    if (token === TOKENS[1]) {
        const simulator = simulateWalkingBasedOnLocations();
        setTimeout(() => {
            // Set interval for sending the location every 10 seconds
            locationInterval = setInterval(() => {
                // use the function that the location returns to update the map
                const location = simulator();

                sendToServer({ type: "location", longitude: location.longitude, latitude: location.latitude });
            }, 5000);
        }, 12000);
    }

    clearInterval(locationInterval);

    return (
        <Provider store={store}>
        <>
            {showManagerApp ? (
                <ManagerApp handleTravelerClick={handleTravelerClick}/>
            ) : (
                <TravelerApp showManagerApp={showManagerApp} handleManagerClick={handleManagerClick}/>
            )}
            <ToastContainer />
        </>
        </Provider>
    );
}


root.render(
    <>
        <Provider store={store}>
            <App />
        </Provider>
    </>
);
