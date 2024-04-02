import controller.IController;
import controller.TerminalController;

public class Main {
    public static IController controller = new TerminalController();

    /**
     * Start the application program
     *
     * @param args
     */
    public static void main(String[] args) {
        controller.run();
    }

}
