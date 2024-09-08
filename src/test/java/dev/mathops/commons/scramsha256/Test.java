package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.log.Log;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Code to perform text exchanges.
 */
final class Test {

    /** Test case. */
    @org.junit.jupiter.api.Test
    @DisplayName("authentication exchange")
    void testScramSHA256() {

        // Set up the server side

        final long seed = (System.currentTimeMillis() << 24) + System.nanoTime();
        final Random rnd = new Random(seed);

        Log.fine("Testing SCRAM-SHA-256 Exchanges...");
        Log.fine(CoreConstants.EMPTY);

        final UserCredentialsManager credentialsManager = new UserCredentialsManager();
        final UserCredentials user = new UserCredentials("ADM", "jdoe", "some_password", 8192);
        credentialsManager.addCredential(user);

        Log.fine("    Created user credentials and credentials manager (on server)");

        final ScramServerStub serverStub = new ScramServerStub(credentialsManager);
        Log.fine("    Created server stub, loaded with credentials manager");
        Log.fine(CoreConstants.EMPTY);

        Log.fine("Simulating client authentication:");
        Log.fine(CoreConstants.EMPTY);

        Log.fine("    1) The client issues an HTTP request for a protected resource, without a valid token in");
        Log.fine("       the request.  The server will reply with an HTTP status code 401 (Unauthorized), and");
        Log.fine("       includes a WWW-Authenticate header with a SCRAM-SHA-256 realm.");
        Log.fine(CoreConstants.EMPTY);

        Log.fine("    2) The client creates a ClientFirstMessage object (with their username and random nonce)");
        Log.fine("        and uses that to generate a 'client-first' message, which is then Base-64 encoded.");
        Log.fine(CoreConstants.EMPTY);

        // Code that a client would need to include...
        final ClientFirstMessage clientFirst = new ClientFirstMessage("jdoe", rnd);
        final byte[] clientFirstBase64 = Base64.getEncoder().encode(clientFirst.clientFirst);

        // *** START test cases block: client-first = "n,,n=username,r=[30-char-cnonce]"

        final byte[] expectUsername = ScramUtils.normalize("jdoe");
        assertArrayEquals(expectUsername, clientFirst.normalizedUsername, "client-first username incorrect");

        final int nameLen = expectUsername.length;
        assertEquals(38 + nameLen, clientFirst.clientFirst.length, "client-first length incorrect");
        assertEquals((int) 'n', (int) clientFirst.clientFirst[0], "client-first byte 0 incorrect");
        assertEquals((int) ',', (int) clientFirst.clientFirst[1], "client-first byte 1 incorrect");
        assertEquals((int) ',', (int) clientFirst.clientFirst[2], "client-first byte 2 incorrect");
        assertEquals((int) 'n', (int) clientFirst.clientFirst[3], "client-first byte 3 incorrect");
        assertEquals((int) '=', (int) clientFirst.clientFirst[4], "client-first byte 4 incorrect");
        for (int i = 0; i < nameLen; ++i) {
            final int pos = 5 + i;
            assertEquals(clientFirst.clientFirst[pos], expectUsername[i], "client-first byte " + pos + " incorrect");
        }
        assertEquals((int) ',', (int) clientFirst.clientFirst[5 + nameLen], "client-first byte data incorrect");
        assertEquals((int) 'r', (int) clientFirst.clientFirst[6 + nameLen], "client-first byte data incorrect");
        assertEquals((int) '=', (int) clientFirst.clientFirst[7 + nameLen], "client-first byte data incorrect");
        for (int i = 0; i < 30; ++i) {
            final int pos = 8 + nameLen + i;
            assertEquals(clientFirst.clientFirst[pos], clientFirst.cNonce[i],
                    "client-first byte " + pos + " incorrect");
        }

        final ClientFirstMessage recoveredClientFirst = new ClientFirstMessage(clientFirstBase64);
        assertArrayEquals(clientFirst.clientFirst, recoveredClientFirst.clientFirst,
                "recovered client-first byte array incorrect");
        assertArrayEquals(clientFirst.normalizedUsername, recoveredClientFirst.normalizedUsername,
                "recovered client-first normalized username incorrect");
        assertArrayEquals(clientFirst.cNonce, recoveredClientFirst.cNonce,
                "recovered client-first client nonce incorrect");
        assertArrayEquals(clientFirst.base64, recoveredClientFirst.base64,
                "recovered client-first base-64 incorrect");

        // *** END test cases block

        Log.fine("    3) The client sends a new HTTP request for the protected resource, but includes an");
        Log.fine("       Authorization header with 'SCRAM-SHA-256 realm=\"...\", data=[base64]'.");
        Log.fine(CoreConstants.EMPTY);

        Log.fine("    4) The server stub decodes this data and validates its contents.  If the data could not");
        Log.fine("       be interpreted or the username given had no matching credentials on the server side,");
        Log.fine("       an error message is created.  Otherwise, a 'server-first' message is created.");
        Log.fine(CoreConstants.EMPTY);

        // Code that the server would need to include...
        final ServerFirstMessage serverFirst = serverStub.handleClientFirst(clientFirstBase64);
        final byte[] serverFirstBase64 = serverFirst.base64;

        // *** START test cases block: server-first = "r=[30-char-cnonce][30-char-snonce],s=[24-char-salt],i=####"

        assertEquals(96, serverFirst.serverFirst.length, "server-first length incorrect");
        assertEquals((int) 'r', (int) serverFirst.serverFirst[0], "server-first byte 0 incorrect");
        assertEquals((int) '=', (int) serverFirst.serverFirst[1], "server-first byte 1 incorrect");
        for (int i = 0; i < 30; ++i) {
            // This is the client nonce - must match that from the client-first message
            final int pos = 2 + i;
            assertEquals(serverFirst.serverFirst[pos], clientFirst.cNonce[i],
                    "server-first byte " + pos + " incorrect");
        }
        assertEquals((int) ',', (int) serverFirst.serverFirst[62], "server-first byte 62 incorrect");
        assertEquals((int) 's', (int) serverFirst.serverFirst[63], "server-first byte 63 incorrect");
        assertEquals((int) '=', (int) serverFirst.serverFirst[64], "server-first byte 64 incorrect");
        assertEquals((int) ',', (int) serverFirst.serverFirst[89], "server-first byte 62 incorrect");
        assertEquals((int) 'i', (int) serverFirst.serverFirst[90], "server-first byte 63 incorrect");
        assertEquals((int) '=', (int) serverFirst.serverFirst[91], "server-first byte 64 incorrect");
        assertEquals((int) '8', (int) serverFirst.serverFirst[92], "server-first byte 64 incorrect");
        assertEquals((int) '1', (int) serverFirst.serverFirst[93], "server-first byte 64 incorrect");
        assertEquals((int) '9', (int) serverFirst.serverFirst[94], "server-first byte 64 incorrect");
        assertEquals((int) '2', (int) serverFirst.serverFirst[95], "server-first byte 64 incorrect");
        for (int i = 0; i < 30; ++i) {
            final int pos = 32 + i;
            assertEquals(serverFirst.serverFirst[pos], serverFirst.sNonce[i],
                    "server-first byte " + pos + " incorrect");
        }
        for (int i = 0; i < 24; ++i) {
            final int pos = 65 + i;
            assertEquals(serverFirst.serverFirst[pos], serverFirst.salt[i],
                    "server-first byte " + pos + " incorrect");
        }
        assertEquals(8192, serverFirst.iterCount, "server-first iteration count incorrect");

        // *** END test cases block

        Log.fine("    5) The server sends a reply with an HTTP status code 401 (Unauthorized), and includes a");
        Log.fine("       WWW-Authenticate header with 'SCRAM-SHA-256 sid=[token] data=[server-first-base64]'.");
        Log.fine(CoreConstants.EMPTY);

        Log.fine("    6) The client parses the 'server-first' message using its stored 'client-first' and uses");
        Log.fine("       it (along with the user's password) to create a 'client-final' message.");
        Log.fine(CoreConstants.EMPTY);

        // Code that a client would need to include...
        final ServerFirstMessage parsedServerFirst = new ServerFirstMessage(serverFirstBase64, clientFirst);

        // *** START test cases block

        assertArrayEquals(serverFirst.sNonce, parsedServerFirst.sNonce,
                "recovered server-first server nonce incorrect");
        assertArrayEquals(serverFirst.salt, parsedServerFirst.salt,
                "recovered server-first salt incorrect");
        assertEquals(serverFirst.iterCount, parsedServerFirst.iterCount,
                "recovered server-first iteration count incorrect");
        assertArrayEquals(serverFirst.serverFirst, parsedServerFirst.serverFirst,
                "recovered server-first byte data incorrect");
        assertArrayEquals(serverFirst.base64, parsedServerFirst.base64,
                "recovered server-first base-64 incorrect");

        // *** END test cases block

        final int errorLen = parsedServerFirst.error.length;
        if (errorLen > 0) {
            final String msg = new String(parsedServerFirst.error, 2, errorLen - 2, StandardCharsets.UTF_8);
            Log.warning("The server reported an error message in server-first: ", msg);
        } else {
            // Code that a client would need to include...
            final ClientFinalMessage clientFinal = new ClientFinalMessage("some_password", clientFirst,
                    parsedServerFirst);
            final byte[] clientFinalBase64 = clientFinal.base64;

            Log.fine("    7) The client sends a new HTTP request for the protected resource, but includes an");
            Log.fine("       Authorization header with 'SCRAM-SHA-256 sid=[token], data=[client-final-base64]'.");
            Log.fine(CoreConstants.EMPTY);

            Log.fine("    8) The server stub decodes this data and validates its contents.  If the data could");
            Log.fine("       not be interpreted or the credentials provided are not valid, an error message");
            Log.fine("       is created.  Otherwise, a 'server-final' message is created.");
            Log.fine(CoreConstants.EMPTY);

            // Code that the server would need to include...
            final ServerFinalMessage serverFinal = serverStub.handleClientFinal(clientFinalBase64);
            final byte[] serverFinalBase64 = serverFinal.base64;

            Log.fine("    9) The server sends a reply with an HTTP status code 200 (OK), and includes an");
            Log.fine("       Authentication-Info header with 'sid=[token] data=[server-final-base64]',");
            Log.fine("       which the client could use to verify the server's identify.");
            Log.fine(CoreConstants.EMPTY);

            Log.fine("   10) After this process succeeds, all requests to protected resources in the realm");
            Log.fine("       under which the authentication took place should include an Authorization header");
            Log.fine("       with 'SCRAM-SHA-256 sid=[token]'.");
            Log.fine(CoreConstants.EMPTY);

            // Code that the client would need to include...
            final ServerFinalMessage parsedServerFinal = new ServerFinalMessage(serverFinalBase64);

            final int errorLen2 = parsedServerFinal.error.length;
            if (errorLen2 > 0) {
                final String msg = new String(parsedServerFinal.error, 2, errorLen2 - 2, StandardCharsets.UTF_8);
                Log.warning("The server reported an error message in server-final: ", msg);
            } else {
                final String clientToken = parsedServerFinal.token;
                Log.fine("Negotiated Token: ", clientToken);

                // Code that the server would need to include:
                final UserCredentials credentials = serverStub.validateToken(clientToken);
                Log.fine("Authenticated user: ", credentials.username, " with role ", credentials.role);
            }
        }
    }
}
