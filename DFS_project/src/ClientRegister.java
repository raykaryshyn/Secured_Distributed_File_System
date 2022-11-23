import java.rmi.*;

public class ClientRegister {
    public static void main(String args[]) {
        String IP = "127.0.0.1";
        String port = "1234";
        int serverAnswer;

        if (args.length > 0) {
            try {
                // lookup method to find reference of remote object
                FileSystem access = (FileSystem) Naming.lookup("rmi://" + IP + ":" + port + "/usingRMI");

                serverAnswer = access.registerUser(args[0], "...");
                System.out.println(serverAnswer);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        } else {
            System.out.println("Sorry, please enter valid command with your preferred username.");
            System.out.println("  >>> java ClientRegister [username]");
        }
    }
}
