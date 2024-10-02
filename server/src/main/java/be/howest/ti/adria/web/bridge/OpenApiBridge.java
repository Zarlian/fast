package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.controller.DefaultController;
import be.howest.ti.adria.logic.controller.Controller;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.web.exceptions.MalformedRequestException;
import be.howest.ti.adria.web.request.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the AdriaOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class OpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(OpenApiBridge.class.getName());
    private final Controller controller;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Generating authentication tokens");
        controller.generateTokens();

        // Handle security
        LOGGER.log(Level.INFO, "Installing security handlers");
        routerBuilder.securityHandler("bearer", ctx -> {
            String token = ctx.request().getHeader("Authorization");
            if (Objects.isNull(token) || !controller.isValidToken(token.substring(7))) {
                Response.sendFailure(ctx, 401, "Unauthorized");
            } else {
                ctx.next();
            }
        });

        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        // ENDPOINTS FOR FAST
        LOGGER.log(Level.INFO, "Installing handler for: getTeleporters");
        routerBuilder.operation("getTeleporters")
                .handler(ctx -> getTeleporters(new GetTeleportersRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getUserHistory");
        routerBuilder.operation("getUserHistory")
                .handler(ctx -> getUserHistoryRequest(new GetUserHistoryRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getTeleporterHistory");
        routerBuilder.operation("getTeleporterHistory")
                .handler(ctx -> getTeleporterHistory(new GetTeleporterHistoryRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getTeleporterSettings");
        routerBuilder.operation("getTeleporterSettings")
                .handler(ctx -> getTeleporterSettings(new GetTeleporterSettingsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getUserPermissions");
        routerBuilder.operation("getUserPermissions")
                .handler(ctx -> getUserPermissions(new GetUserPermissionsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getUser");
        routerBuilder.operation("getUser")
                .handler(ctx -> getUser(new GetUserRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getUsers");
        routerBuilder.operation("getUsers")
                .handler(ctx -> getUsers(new GetUsersRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getGroups");
        routerBuilder.operation("getGroups")
                .handler(ctx -> getGroups(new GetGroupsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getGroup");
        routerBuilder.operation("getGroup")
                .handler(ctx -> getGroup(new GetGroupRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getFavourites");
        routerBuilder.operation("getFavourites")
                .handler(ctx -> getFavourites(new GetFavouritesRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: updateTeleporterSettings");
        routerBuilder.operation("updateTeleporterSettings")
                .handler(ctx -> updateTeleporterSettings(new UpdateTeleporterSettingsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: updateUserPermissions");
        routerBuilder.operation("updateUserPermissions")
                .handler(ctx -> updateUserPermissions(new UpdateUserPermissionsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: addGroup");
        routerBuilder.operation("addGroup")
                .handler(ctx -> addGroup(new AddGroupRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: addFavourite");
        routerBuilder.operation("addFavourite")
                .handler(ctx -> addFavourite(new AddFavouriteRequest(ctx)));

        LOGGER.log(Level.INFO,"Installing handler for: updateGroup");
        routerBuilder.operation("updateGroup")
                .handler(ctx -> updateGroup(new UpdateGroupRequest(ctx)));

        LOGGER.log(Level.INFO,"Installing handler for: updateFavourite");
        routerBuilder.operation("updateFavourite")
                .handler(ctx -> updateFavourite(new UpdateFavouriteRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: updateGroupMember");
        routerBuilder.operation("updateGroupMembers")
                .handler(ctx -> updateGroupMember(new UpdateGroupMemberRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: deleteGroup");
        routerBuilder.operation("deleteGroup")
                .handler(ctx -> deleteGroup(new DeleteGroupRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: deleteFavourite");
        routerBuilder.operation("deleteFavourite")
                .handler(ctx -> deleteFavourite(new DeleteFavouriteRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: getUserTransactions");
        routerBuilder.operation("getUserTransactions")
                .handler(ctx -> getUserTransactions(new GetUserTransactionsRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: addTrip");
        routerBuilder.operation("addTrip")
                .handler(ctx -> addTrip(new AddTripRequest(ctx)));

        LOGGER.log(Level.INFO, "Installing handler for: importFriends");
        routerBuilder.operation("importFriends")
                .handler(ctx -> importFriends(new ImportFriendsRequest(ctx)));

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    private void importFriends(ImportFriendsRequest importFriendsRequest) {
        String token = importFriendsRequest.getToken();

        String response = controller.importFriends(token);

        importFriendsRequest.setResponse(response);

        importFriendsRequest.sendResponse();
    }

    private void getUsers(GetUsersRequest getUsersRequest) {
        String token = getUsersRequest.getToken();
        getUsersRequest.setResponse(controller.getUsers(token));

        getUsersRequest.sendResponse();
    }

    private void getGroup(GetGroupRequest getGroupRequest) {
        int groupId = getGroupRequest.getGroupId();
        String token = getGroupRequest.getToken();

        getGroupRequest.setResponse(controller.getGroup(groupId, token));

        getGroupRequest.sendResponse();
    }

    private void addTrip(AddTripRequest request) {
        String token = request.getToken();

        int from = request.getFrom();
        int to = request.getTo();
        String departure = request.getDeparture();
        String arrival = request.getArrival();
        int group = request.getGroup();
        String type = request.getType();

        User u = controller.getUser(token);

        String response = controller.addTrip(token, from, to, departure, arrival, group);

        if (type.equals("userHistory")) {
            controller.increaseUserUsesLeft(u.getAdriaId());
        }

        request.setResponse(response);

        request.sendResponse();
    }

    private void getUserTransactions(GetUserTransactionsRequest request) {

        String token = request.getToken();

        request.setResponse(controller.getUserTransactions(token));

        request.sendResponse();
    }

    private void deleteFavourite(DeleteFavouriteRequest request) {
        String token = request.getToken();
        int teleporterId = request.getTeleporterId();

        String response = controller.deleteFavourite(token, teleporterId);

        request.setResponse(response);

        request.sendResponse();
    }

    private void deleteGroup(DeleteGroupRequest request) {
        int groupId = request.getGroupId();

        String response = controller.deleteGroup(groupId);

        request.setResponse(response);

        request.sendResponse();
    }

    private void updateGroupMember(UpdateGroupMemberRequest request) {
        int groupId = request.getGroupId();

        String token = request.getToken();

        String response = controller.updateGroupMembers(token, groupId, request.getMembersId());

        request.setResponse(response);

        request.sendResponse();
    }

    private void updateFavourite(UpdateFavouriteRequest request) {
        String token = request.getToken();
        int teleporterId = request.getTeleporterId();
        String type = request.getType();
        String name = request.getName();

        String response = controller.updateFavourite(token, teleporterId, type, name);

        request.setResponse(response);

        request.sendResponse();
    }

    private void updateGroup(UpdateGroupRequest request) {
        int groupId = request.getGroupId();

        String token = request.getToken();

        String response = controller.updateGroup(groupId, request.getGroupName(), token);

        request.setResponse(response);

        request.sendResponse();
    }

    private void addFavourite(AddFavouriteRequest request) {
        String token = request.getToken();
        int teleporterId = request.getTeleporterId();
        String type = request.getType();
        String name = request.getName();

        String response = controller.addFavourite(token, teleporterId, type, name);

        request.setResponse(response);

        request.sendResponse();
    }

    private void addGroup(AddGroupRequest request) {
        String groupName = request.getGroupName();
        String token = request.getToken();
        int leaderId = request.getLeaderId();
        List<Integer> members = request.getMembers();

        int groupId = controller.addGroup(groupName, members, token, leaderId);

        request.setResponse(groupId);

        request.sendResponse();
    }

    private void updateUserPermissions(UpdateUserPermissionsRequest request) {
        String token = request.getToken();
        int teleporter = request.getTeleporterId();

        boolean accessLog = request.getAccessLogs();
        boolean assingPerm = request.getAssignPermissions();
        boolean controlTele = request.getControlTeleporter();
        boolean manageList = request.getManageList();

        String response = controller.updateUserPermissions(token, teleporter, accessLog, assingPerm, controlTele, manageList);

        request.setResponse(response);

        request.sendResponse();
    }

    private void updateTeleporterSettings(UpdateTeleporterSettingsRequest request) {
        int telepporter = request.getTeleporterId();
        boolean visibility = request.getVisibility();

        String token = request.getToken();

        String response = controller.updateTeleporterSettings(telepporter, visibility, token);

        request.setResponse(response);

        request.sendResponse();
    }

    private void getFavourites(GetFavouritesRequest request) {
        String token = request.getToken();

        request.setResponse(controller.getFavourites(token));

        request.sendResponse();
    }

    private void getGroups(GetGroupsRequest request) {
        String token = request.getToken();

        request.setResponse(controller.getGroups(token));

        request.sendResponse();
    }

    private void getUserPermissions(GetUserPermissionsRequest request) {
        String token = request.getToken();
        int telepporter = request.getTeleporterId();

        request.setResponse(controller.getUserPermissions(token, telepporter));

        request.sendResponse();
    }

    private void getTeleporterSettings(GetTeleporterSettingsRequest request) {
        int teleporterId = request.getTeleporterId();

        String token = request.getToken();

        request.setResponse(controller.getTeleporterSettings(teleporterId, token));

        request.sendResponse();
    }

    private void getTeleporterHistory(GetTeleporterHistoryRequest request) {
        String token = request.getToken();

        int teleporterId = request.getTeleporterId();

        request.setResponse(controller.getTeleporterHistory(token, teleporterId));

        request.sendResponse();
    }

    private void getUserHistoryRequest(GetUserHistoryRequest request) {
        String token = request.getToken();

        request.setResponse(controller.getUserHistory(token));

        request.sendResponse();
    }

    public OpenApiBridge() {
        this.controller = new DefaultController();
    }

    public OpenApiBridge(Controller controller) {
        this.controller = controller;
    }

    // ENDPOINTS FOR FAST

    private void getTeleporters(GetTeleportersRequest request) {
        String token = request.getToken();

        request.setResponse(controller.getTeleporters(token));

        request.sendResponse();
    }

    public void getUser(GetUserRequest request) {
        String token = request.getToken();

        request.setResponse(controller.getUser(token));

        request.sendResponse();
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
