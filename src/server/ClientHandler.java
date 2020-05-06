package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

//TODO make the server store the lists of friends

public class ClientHandler implements Runnable, Serializable {

    ArrayList<ClientHandler> clients;

    private Socket client;

    private BufferedReader in;

    public PrintWriter out;

    public int id;

    /*
        instantiate the buffered reader and the print writer
     */
    ClientHandler(Socket client, ArrayList<ClientHandler> clients, int id) throws IOException {
        this.id = id;
        this.client = client;
        this.clients = clients;

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    /*
       waits for socket to receive a message
       if the message start with !userinfo! then it means user is logging in
       if it starts with !newuser! then the user is signing up
       if it doesn't start with that then it is a message to other clients

       if user disconnects then server user count goes down by 1
       finally closes io

        requests the server can execute

        !userinfo!
        !newuser!
        !checkDupes!
        !name!
        !keyrequest!
        !getperson!
        !friendrequest!
        !offline!

     */
    @Override
    public void run() {

        try {
            while (true) {

                String request = in.readLine();
                System.out.println(request);

                if (request.startsWith("!userinfo!")) {

                    checkIfAccountExists(request);

                } else if (request.startsWith("!newuser!")) {

                    createNewAccount(request);

                } else if (request.startsWith("!checkDupes!")) {
                    checkIfNameTaken(request);

                } else if (request.startsWith("!name!")) {

                    //TODO make this method send a message to a cetatin person
                    sendMessage(request);

                } else if (request.startsWith("!keyrequest!")) {

                    sendUserKeys();

                } else if (request.startsWith("!friendrequest!")) {

                    //sendFriendRequest(request);

                } else if (request.startsWith("!offline!")) {

                    String name = request.substring(9);
                    setToOffline(name);
                }
            }
        } catch (Exception e) {
            Server.USER_COUNT--;
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void outToAll(String name, String color, String msg) {
        for (ClientHandler c : clients) {

            if (c.id != this.id) {
                c.out.println("!name!" + name + " !color!" + color + " !message!" + msg);
            }
        }
    }

    private void createNewAccount(String request) {
        String[] userData = getNewAccountInfo(request);

        setToOnline(userData[0]);

        Server.dataBase.addRecord(this, userData[0], userData[1], userData[2]);
    }

    //parses the create account request so the createNewAccount method can add the user
    //to the database
    private String[] getNewAccountInfo(String str) {

        int[] div = {str.indexOf(" "), str.substring(str.indexOf(" ") + 1).indexOf(" ") + str.indexOf(" ")};

        String name = str.substring(9, str.indexOf(" "));
        String password = str.substring(div[0] + 1, div[1] + 1);
        String color = str.substring(div[1] + 2);

        String[] result = {name, password, color};

        return result;
    }

    //logging in
    private void checkIfAccountExists(String request) {
        //make code that checks if that account exists
        String name = request.substring(10, request.indexOf(";"));
        String password = request.substring(request.indexOf(";") + 1);

        if (Server.dataBase.verifyLoginData(name, password)) {
            setToOnline(name);
            out.println("!userIsVerified!true !userColor!" + Server.dataBase.getUserProperty(name, "color") + " !id!" + id);
        } else {
            out.println("!userIsVerified!false");
        }
    }

    //creating an account
    private void checkIfNameTaken(String request) {
        //parse request and find out if the name is taken
        String name = request.substring(12);

        if (Server.dataBase.checkForDupe(name)) {
            out.println("!nametaken!true");
        } else if (!Server.dataBase.checkForDupe(name)) {
            out.println("!nametaken!false !id!" + id);
        }
    }

    private void sendMessage(String request) {
        int[] div = {request.indexOf(" "), request.substring(request.indexOf(" ") + 1).indexOf(" ") + request.indexOf(" ")};
        String name = request.substring(6, request.indexOf(" "));
        String color = request.substring(div[0] + 8, div[1] + 1);
        String message = request.substring(div[1] + 11);
        outToAll(name, color, message);
    }

    //TODO make this use mysql database
    private void sendUserKeys() {

        out.println("!length!" + Server.dataBase.getCount()
                + " !keylist!" + Server.dataBase.getKeys().toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace(" ", ".")
                + ".");
    }

    private int getUserIndex(int id) {

        int index = 0;

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).id == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void sendFriendRequest(String request) {
        /*
        String senderName = request.substring(23, request.indexOf(" "));
        String senderColor = Server.dataBase.getUserProperty(senderName, "color").toString();
        String recieverName = request.substring(request.indexOf(" ")).substring(11);
        int recieverid = Server.dataBase.table.get(recieverName).id;

        //TODO fix server error not sending friend request to the right person after logging in

        for(ClientHandler c: clients) {
            if(c.id == recieverid) {
                out.println("!friendrequest!!sender!" + senderName +" !color!"+senderColor);
                break;
            }
        }
        */
    }

    private void setToOffline(String name) {

        Server.dataBase.setAsOffline(name);
    }

    private void setToOnline(String name) {

        Server.dataBase.setAsOnline(name);
    }
}

