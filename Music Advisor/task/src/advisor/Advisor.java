package advisor;

import java.util.Scanner;

class Advisor {
    private boolean nextStep = false;
    private Scanner scanner = new Scanner(System.in);
    private SpotifyGetDataRequest dataRequest = new SpotifyGetDataRequest();
    private ReceivedData data = ReceivedData.getInstance();

    void start() throws Exception {
        nextStep = true;
        run();
    }

    private void run() throws Exception {
        while (!scanner.hasNext("auth")) {
            if (scanner.nextLine().equals("exit")) {
                System.out.println("---GOODBYE!---");
                nextStep = false;
                break;
            }

            System.out.println("Please, provide access for application.");
        }

        while (nextStep) {
            String[] input = scanner.nextLine().split("\\s+", 2);

            switch (input[0]) {
                case "new":
                    data.setData(dataRequest.getNew());
                    data.printData();
                    break;
                case "featured":
                    data.setData(dataRequest.getFeatured());
                    data.printData();
                    break;
                case "categories":
                    data.setData(dataRequest.getCategories());
                    data.printData();
                    break;
                case "playlists":
                    data.setData(dataRequest.getPlaylist(input[1]));
                    data.printData();
                    break;
                case "auth":
                    Authorization auth = new Authorization();
                    auth.printAuthLink();
                    auth.doAuth();
                    break;
                case "prev":
                    data.printPrevPage();
                    break;
                case "next":
                    data.printNextPage();
                    break;
                default:
                    break;
            }
        }
        ;
    }
}
