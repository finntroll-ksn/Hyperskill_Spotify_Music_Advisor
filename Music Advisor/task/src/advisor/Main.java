package advisor;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 1) {

            for (int i = 0; i < args.length - 1; i++)
                switch (args[i]) {
                    case "-access":
                        Config.SERVER_PATH = args[i + 1];
                        break;
                    case "-resource":
                        Config.API_PATH = args[i + 1];
                        break;
                    case "-page":
                        Config.PAGE_SIZE = Integer.parseInt(args[i + 1]);
                        break;
                    default:
                        break;
                }
        }
        
        Advisor advisor = new Advisor();
        advisor.start();
    }
}
