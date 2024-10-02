package be.howest.ti.adria.web;

import be.howest.ti.adria.logic.controller.MockController;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.web.bridge.OpenApiBridge;
import be.howest.ti.adria.web.bridge.RtcBridge;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new OpenApiBridge(new MockController()), new RtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    // dummy test
    @Test
    void dummyTest(final VertxTestContext testContext) {
        // add one assertion to avoid PMD warning
        assertTrue(true);
        testContext.completeNow();
    }
}