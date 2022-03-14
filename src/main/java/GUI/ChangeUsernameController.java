package GUI;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import serverclientstuff.User;

public class ChangeUsernameController {

    Client client;
    User currUser;


    @FXML
    Label usernameLabel;

    @FXML
    TextField desiredNameField;


    @FXML
    Label errLabel;


    @FXML
    public void initialize(){
        errLabel.setText("");
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public void setUser(User currUser){
        this.currUser = currUser;
        usernameLabel.setText(currUser.getUsername());
    }


}