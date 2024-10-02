package be.howest.ti.adria.web.request;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public abstract class Request {
    final RoutingContext ctx;

    final RequestParameters params;

    Request(RoutingContext ctx) {
        this.ctx = ctx;
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public abstract void sendResponse();


    public String getToken() {
        String authHeader = ctx.request().getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Skip "Bearer " prefix
        } else {
            // Handle the case where the Authorization header is missing or doesn't have the expected format
            return null; // or throw an exception, log an error, etc.
        }
    }
}
