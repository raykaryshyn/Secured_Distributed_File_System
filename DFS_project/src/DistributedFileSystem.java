import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class DistributedFileSystem extends UnicastRemoteObject implements FileSystem {
    private Map<String, String> users;

    @SuppressWarnings("unchecked")
    DistributedFileSystem() throws RemoteException, IOException {
        super();

        // Set up config file for storing authorized users
        File usersDB = new File("configurations/users");
        usersDB.getParentFile().mkdirs();
        if (usersDB.createNewFile())
            System.out.println("Created configuration file " + usersDB.getPath());
        if (usersDB.length() != 0) {
            try {
                FileInputStream fis = new FileInputStream(usersDB.getPath());
                ObjectInputStream ois = new ObjectInputStream(fis);
                users = (HashMap<String, String>) ois.readObject();
                fis.close();
                ois.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found.");
                e.printStackTrace();
                return;
            }
        } else {
            users = new HashMap<String, String>();
        }
        System.out.println("Loaded configuration file " + usersDB.getPath());
    }

    @Override
    public String registerUser(String userName, String userPublicKey) {
        String ans;
        if (users.containsKey(userName)) {
            ans = "User '" + userName + "' already exists";
            return ans;
        }

        users.put(userName, userPublicKey);
        updateUsers();
        ans = "Registered new user '" + userName + "'";
        System.out.println(ans);
        return ans;
    }

    private void updateUsers() {
        try {
            FileOutputStream myFileOutStream = new FileOutputStream("configurations/users");
            ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);
            myObjectOutStream.writeObject(users);
            myObjectOutStream.close();
            myFileOutStream.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * @param fileName
     * @throws RemoteException
     */
    @Override
    public String createFile(String fileName) throws IOException {
        File fileObject = new File(fileName);
        // Method createNewFile() method creates blank file
        if (fileObject.createNewFile()) {
            System.out.println("File created: " + fileObject.getName());
        } else {
            System.out.println("File already exists.");
            return "File already exists";
        }
        return "File created successfully.";
    }

    /**
     * @param fileName
     * @throws RemoteException
     */
    @Override
    public String readFile(String filePath) throws IOException {
        String ans;
        StringBuilder str = new StringBuilder();
        // Creating an object of BufferedReader class
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((ans = br.readLine()) != null)
                str.append(ans);
            System.out.println("Read successfully");
            br.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * @param fileName
     * @throws RemoteException
     */
    @Override
    public String writeFile(String fileName, String data) throws RemoteException {
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println("Started writing");
            fw.write(data);
            bw.close();
            System.out.println("Written successfully");
            return "Written successfully";
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "An error occurred.";
        }
    }

    /**
     * @param fileName
     * @throws RemoteException
     */
    @Override
    public void restoreFiles(String fileName) throws RemoteException {

    }

    /**
     * @param fileName
     * @return
     * @throws RemoteException
     */
    @Override
    public String deleteFile(String fileName) throws RemoteException {
        String ans = "";
        try {
            File file = new File(fileName);

            if (file.delete()) {
                ans = "File deleted successfully";
            } else {
                ans = "Failed to delete the file";
            }
        } catch (SecurityException e) {
            ans = "An error occurred.";
            e.printStackTrace();
        }
        System.out.println(ans);
        return ans;
    }
}
