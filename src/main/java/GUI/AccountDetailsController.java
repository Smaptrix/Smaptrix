package GUI;

import client.Client;
import javafx.fxml.FXML;

public class AccountDetailsController {

    //TODO - Add all the functionality
    //     - I.E Display User Details - Change User details
    //     - BEFORE THIS NEED TO DO USER INFO ENCRYPTION RATHER THAN HASHING

    Client client;


    @FXML
    public void initialize(){



    }

    public void setClient(Client client) {
        this.client = client;
    }
}