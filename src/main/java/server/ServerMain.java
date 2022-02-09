package server;


import serverclientstuff.User;

import java.io.IOException;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class ServerMain {

    //Entry point for server application
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {


            System.out.println("Server Start\n");
            new ServerMain();

    }

    //TODO - ASk the client to login to the server


    //Sets up the initial server
    public ServerMain() throws IOException, NoSuchAlgorithmException {


        ServerUser testUser = new ServerUser(new User("test", "12345"));
        ServerUser meUser = new ServerUser(new User("jingham", "12345"));

        System.out.println(meUser.toString());


        meUser.createUser();


        ServerUser nonExistantUser = new ServerUser(new User("testagain", "test"));
        nonExistantUser.createUser();
        System.out.println("Does this user exist: " + nonExistantUser.userExistState);




        /*
        try {
            Server server = new Server();

            server.startup(5555);

            server.bufferListen();
        }catch(SocketException e){
            System.out.println("Socket exception - Lost connection with client");
        }
        */



    }

}


