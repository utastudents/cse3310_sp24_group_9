package uta.cse3310;

import static junit.framework.Assert.assertEquals;

public class AppTest {

    public void testWebSocketPort(){
        int expectedPort = 9109;
        App app = new App(expectedPort);

        int actualPort = app.getPort();

        assertEquals(expectedPort, actualPort);
    }
}