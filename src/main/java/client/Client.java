/*
    Company Name:   Maptrix
    Project Name:   WiseGuide
    Authors:        Joe Ingham
    Date Created:   27/01/2022
    Last Updated:   24/02/2022
 */
package client;

import serverclientstuff.User;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.Hashtable;


//TODO - Log out function
//     - Consider encryption rather than hashing so that we cna decrypt all of the information (Research as well)


/**
 * Client class handles the client side server operation methods.
 */
public class Client {

    /**
     * clientSocket is the client side socket.
     */
    private Socket clientSocket;

    /**
     * outText is a PrintWriter that allows sending text requests to the server.
     */
    private PrintWriter outText;
    //Only need a printWriter as we won't be sending files back to the server, just text requests

    /**
     * inputStream allows reading of the files sent by the server.
     */
    private InputStream inputStream;
    //InputStream to read files

    /**
     * connected is a boolean that stores whether the client is connected to the server.
     */
    private boolean connected;


    /**
     * Dictionary stores where all the files are located.
     */
    public Dictionary<String, File> fileLocations = new Hashtable<>();
    //Each client will keep a dictionary containing where all there files are located

    //Connects to the port

    /**
     * <p>
     * Starts the connection to the server by creating necessary objects and assigning the correct ip and port.
     * </p>
     *
     * @param ip The ip address of the server.
     * @param port The port of the client side.
     * @throws IOException Throws an IOException if it fails to connect to the server.
     */
    public void startConnection(String ip, int port) throws IOException {

        connected = false;

        try {
            clientSocket = new Socket(ip, port);
            outText = new PrintWriter(clientSocket.getOutputStream(), true);
            inputStream = clientSocket.getInputStream();
            System.out.println("Connection Opened");
            connected = true;
            clientSocket.setSoTimeout(500);

        } catch (ConnectException e) {
            System.out.println("Failed to connect/Server Offline");
        }

    }

    /**
     * <p>
     * Closes the connection to the server.
     * </p>
     * @throws IOException Throws an IOException if the client fails to close the connection.
     */
    public void closeConnection() throws IOException {

        outText.println("Close Connection");
        inputStream.close();
        outText.close();
        clientSocket.close();
        System.out.println("Connection Closed");
        connected = false;

    }

    /**
     * <p>
     * Sends a test message to the server and stores the result as a string and returns it.
     * </p>
     * @return Returns the result of the test response.
     * @throws IOException Throws an IOException if the client fails to send a message.
     */
    public String sendTestMessage() throws IOException {


        outText.println("ECHO " + "test");

        int stringSize = inputStream.read();

        byte[] data = readBytes(stringSize);


        String result = new String(data, StandardCharsets.UTF_8);


        return result;
    }


    /**
     * <p>
     * Sends an ECHO message to the server and returns the result as a string.
     * </p>
     * @param msg The message to send to the server.
     * @return The message returned by the server.
     * @throws IOException Throws an IOException if
     */
    public String echoMessage(String msg) throws IOException {

        System.out.println("ECHO REQUEST: " + msg);

        outText.println("ECHO " + msg);

        int fileSize = inputStream.read();


        byte[] data = readBytes(fileSize);


        String result = new String(data, StandardCharsets.UTF_8);


        return result;

    }

    //Requests a file from the server
    //The client should request specific files from the server, so we should know the name of the files
    //The filenames should be stored in the XML
    public File requestFile(String fileName) throws IOException {

        System.out.println("GET REQUEST: " + fileName);

        outText.println("GET " + fileName);

        //Tells us how many bytes are telling us how big the file is
        int numOfFileSizeBytes = inputStream.read();

        System.out.println("We have " + numOfFileSizeBytes + " file size bytes to read");

        //Reads the next set amount of bytes to decode the file size
        byte[] bytesToReadBytes = new byte[numOfFileSizeBytes];

        for (int i = 0; i < numOfFileSizeBytes; i++) {
            bytesToReadBytes[i] = (byte) inputStream.read();
        }

        int bytesToRead = ByteBuffer.wrap(bytesToReadBytes).getInt();


        //Magic number 3 - because we know that the file extension is only going to be three letters
        byte[] DataTypeBytes = new byte[3];

        for (int i = 0; i < 3; i++) {
            DataTypeBytes[i] = (byte) inputStream.read();
        }

        String dataType = new String(DataTypeBytes, StandardCharsets.UTF_8);

        System.out.println(dataType);


        byte[] data = readBytes(bytesToRead);


        System.out.println("The file is a : " + dataType + " file and it is : " + bytesToRead + " long.");

        //Once we have the array of bytes, we then reconstruct that into the actual file.
        return BytesToFile(data, fileName, dataType);
    }

    //Reads the bytes for the file from the inputStream
    public byte[] readBytes(int bytesToRead) {

        //Initialises a new byte array of size predetermined by our network protocol
        byte[] data = new byte[bytesToRead];

        boolean end = false;
        int bytesRead = 0;


        //Reads bytes up until the count has been reached
        while (!end) {

            try {

                data[bytesRead] = (byte) inputStream.read();


            } catch (IOException e) {
                e.printStackTrace();
            }

            //Increment Byte count
            bytesRead += 1;
            if (bytesRead == bytesToRead) {
                // System.out.println("We have read: " + bytesRead);
                end = true;
            }

        }
        return data;
    }

    //Turns the raw bytes into a file and saves it in the default temp folder on the system
    public File BytesToFile(byte[] data, String fileName, String fileType) throws IOException {

        //Creates a new temp file - Identifiable by custom prefix
        File currFile = new File(String.valueOf(Files.createTempFile("WG_", "." + fileType)));


        //Creates a temp file out of the data received, so that when the program closes the data isn't saved
        FileOutputStream os = new FileOutputStream(currFile);

        os.write(data);

        fileLocations.put(fileName, currFile);

        //Schedules the file to be deleted when the application closes
        currFile.deleteOnExit();

        os.close();

        //Saves file in temp position
        System.out.println("File saved at: " + currFile);

        return currFile;
    }


    //Attempts to log in with the given user data
    public String requestLogin(User currUser) throws IOException {
        outText.println("LOGIN");

        System.out.println("LOGIN MESSAGE SENT");

        outText.println(currUser.getUsername());

        outText.println(currUser.getPassword());

        String ack = receiveAcknowledgement();


        return ack;

    }


    //Receive a verification message from the server
    public String receiveAcknowledgement() throws IOException {

        int fileSize = inputStream.read();


        byte[] data = readBytes(fileSize);


        String dataString = new String(data, StandardCharsets.UTF_8);


        return dataString;
    }


    //TODO - possible refactor of user functions into single function?


    //Asks the server to verify if a user exists and if their password is correct
    public String verifyUser(User currUser) throws IOException {
        outText.println("VERIFYUSER");

        outText.println(currUser.getUsername());

        outText.println(currUser.getPassword());

        return receiveAcknowledgement();

    }


    //Requests the server to create and add a user to the database
    public String createUser(User currUser) throws IOException {

        outText.println("CREATEUSER");

        outText.println(currUser.getUsername());

        outText.println(currUser.getPassword());


        return receiveAcknowledgement();
    }


    //Attempts to log out of the server
    public String requestLogout() throws IOException {

        outText.println("LOGOUT");
        outText.flush();

        return receiveAcknowledgement();
    }


    public boolean isConnected() {
        return connected;
    }
}
