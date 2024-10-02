package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.events.EventFactory;
import be.howest.ti.adria.logic.events.IncomingEvent;
import be.howest.ti.adria.logic.events.OutgoingEvent;
import be.howest.ti.adria.logic.events.UnicastEvent;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import be.howest.ti.adria.logic.Map;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.
 *
 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java
 *
 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
public class RtcBridge {
    private static final String EB_EVENT_TO_MARTIANS = "events.to.martians";
    private SockJSHandler sockJSHandler;
    private EventBus eb;

    private static final String CHNL_TO_SERVER = "events.to.server";
    private static final String CHNL_TO_CLIENT_UNICAST = "events.to.client.";
    private static Map MAP;

    /**
     * Example function to put a message on the event bus every 10 seconds.
     * The timer logic is only there to simulate a repetitive stream of data as an example.
     * Please remove this timer logic or move it to an appropriate place.
     * Please call the controller to get some business logic data.
     * Afterwords publish the result to the client.
     */
    public void sendEventToClients() {
        final Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                eb.publish(EB_EVENT_TO_MARTIANS, new JsonObject().put("message", "Hello Martians!"));
            }
        };

        timer.schedule(task, 0, 30000);
    }

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex("events\\..+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
    }

    public SockJSHandler getSockJSHandler(Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
        eb = vertx.eventBus();
        createSockJSHandler();

        registerConsumers();

        MAP = new Map();

        // This is for demo purposes only.
        // Do not send messages in this getSockJSHandler function.
        sendEventToClients();

        return sockJSHandler;
    }

    public void registerConsumers() {
        eb.consumer(CHNL_TO_SERVER, this::handleIncomingLocation);
    }

    private void handleIncomingLocation(Message<JsonObject> msg) {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(msg.body());

        List<OutgoingEvent> result = MAP.handleEvent(incoming);
        handleOutgoingLocation(result);
    }

    private void handleOutgoingLocation(List<OutgoingEvent> outgoingEvents) {
        for (OutgoingEvent e : outgoingEvents) {
            switch (e.getType()) {
                case LOCATION:
                    unicastLocation((UnicastEvent) e);
                    break;
                case FRIENDREQUEST:
                    unicastFriendRequest((UnicastEvent) e);
                    break;
                case GET_FRIENDREQUESTS:
                    unicastGetFriendRequests((UnicastEvent) e);
                    break;
                case FRIENDREQUESTACCEPT:
                    break;
                default:
                    throw new IllegalArgumentException("Unknown event type: " + e.getType());
            }
        }
    }

    private void unicastGetFriendRequests(UnicastEvent e) {
        JsonObject json = new JsonObject();
        json.put("type", "friendRequests");
        json.put("requests", MAP.getFriendRequests(e.getRecipient()));
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), json);
    }

    private void unicastLocation(UnicastEvent e) {
        JsonObject json = new JsonObject();
        json.put("latitude", e.getLatidude());
        json.put("longitude", e.getLongitude());
        json.put("sender", e.getSender());
        json.put("type", "location");
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), json);
    }

    private void unicastFriendRequest(UnicastEvent e) {
        JsonObject json = new JsonObject();
        json.put("sender", e.getSender());
        json.put("senderId", e.getSenderId());
        json.put("group", e.getGroupId());
        json.put("type", "friendRequest");
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), json);
    }
}
