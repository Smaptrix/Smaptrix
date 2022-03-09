package GUI;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controls the about page of the GUI
 */
public class AboutController {


    /**
     * The label that displays the current version number
     */
    @FXML
    Label verNumLabel;


    /**
     * Sets the current version number
     * @param verString the current version number
     */
    public void setVerNum(String verString){
        verNumLabel.setText(verString);
    }





}