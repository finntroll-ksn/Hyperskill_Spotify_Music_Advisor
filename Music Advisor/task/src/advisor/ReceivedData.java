package advisor;

import java.util.List;

class ReceivedData {
    private static ReceivedData instance = new ReceivedData();

    private ReceivedData() {
    }

    static ReceivedData getInstance() {
        return instance;
    }

    private static List<SpotifyResponseData> data;
    private static int totalPages;
    private static int currentPage = 1;

    void setData(List<SpotifyResponseData> data) {
        ReceivedData.data = data;
        totalPages = (int) Math.floor(data.size() / (double) Config.PAGE_SIZE);
        currentPage = 1;
    }

    void printData() {
        for (int i = 0; i < Config.PAGE_SIZE; i++) {
            System.out.print(data.get(i));
        }

        System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
    }

    void printPrevPage() {
        if (currentPage - 1 < 1) {
            System.out.println("No more pages.");
        } else {
            currentPage--;

            for (int i = Config.PAGE_SIZE * (currentPage - 1); i < Config.PAGE_SIZE * currentPage; i++) {
                System.out.print(data.get(i));
            }

            System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
        }
    }

    void printNextPage() {
        if (currentPage + 1 > totalPages) {
            System.out.println("No more pages.");
        } else {
            currentPage++;

            for (int i = Config.PAGE_SIZE * (currentPage - 1); i < Config.PAGE_SIZE * currentPage; i++) {
                System.out.print(data.get(i));
            }

            System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
        }
    }
}
