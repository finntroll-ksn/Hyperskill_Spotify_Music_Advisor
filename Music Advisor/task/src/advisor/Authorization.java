package advisor;

public class Authorization {
    private Request req = Request.getInstance();

    void printAuthLink() {
        System.out.printf(
                "%s/authorize?client_id=%s&redirect_uri=%s&response_type=code\n\n",
                Config.SERVER_PATH,
                Config.CLIENT_ID,
                Config.RETURN_URI
        );
    }

    void doAuth() throws Exception {
        HttpServerConfigs connection = new HttpServerConfigs();
        connection.webStart();

        System.out.println("waiting for code...");

        do {
            Thread.sleep(100);
            if (Config.AUTH_CODE.equals("")) {
                Thread.sleep(100);
            } else {
                System.out.println("code received");

                connection.webStop(1);
                Thread.sleep(100);

                System.out.println("making http request for access_token...");

                req.webGetToken();

                System.out.printf(
                        "response: %s\n%s\n",
                        Config.ACCESS_TOKEN,
                        "---SUCCESS---"
                );
                break;
            }
        } while (true);
    }
}
