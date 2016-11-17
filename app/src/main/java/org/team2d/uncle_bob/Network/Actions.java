package org.team2d.uncle_bob.Network;

import java.io.IOException;

/**
 * Created by nikolaev on 17.11.16.
 */

public class Actions {
    private static OkHTTP okHTTPClient = new OkHTTP();
    static void addToBasket () {
        try {
            okHTTPClient.run("test");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
