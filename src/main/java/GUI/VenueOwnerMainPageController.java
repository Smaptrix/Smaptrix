package GUI;

import VenueXMLThings.VenuePage;
import VenueXMLThings.VenueXMLParser;
import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import serverclientstuff.User;
import serverclientstuff.Utils;

import java.io.IOException;



//TODO - Do we want to display the files that are needed by the XML? OR that we have in our directory?

public class VenueOwnerMainPageController {

    private Client client;
    private User currUser;
    private String filePathStart;

    @FXML
    Label titleLabel;

    @FXML
    ListView fileList;

    @FXML
    Button openFileButton;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
        titleLabel.setText("VENUE ADMIN PAGE: " + currUser.getUsername());
        //Replaces spaces with _ so that file paths remain correct
        filePathStart = "Venues/Clubs/" + (currUser.getUsername()).replace(" ", "_") + "/";
    }


    //Populates the list on the venue owner page of the current files in the XML
    public void populateFileList(){

        //Download the xml file - we dont care if the user has access to it
        //This is because all the venues will be on the main application publicly
        //No sensitive data is stored in the venue xml file
        try {
            client.requestVenueXMLFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VenueXMLParser xml = new VenueXMLParser(client.getFile("venuesLocation.xml"));


        VenuePage currVenuePage = xml.getPage("title", currUser.getUsername());

        int numOfFiles = currVenuePage.numberOfElements;

        for(int i = 0; i < numOfFiles; i++){
            String fileName = currVenuePage.children.get(0).children.get(i).attributes.get("include_source");

            int filePathLength = filePathStart.length();

            fileName = fileName.substring(filePathLength);

            fileList.getItems().add(fileName);
        }

    }



    @FXML
    //Downloads and opens the requested file by the venue owner
    private void onOpenFileButtonPress(){

        System.out.println("Open File Button Pressed");

        //Make sure that something is selected to it doesnt request a file that doesnt exist
       if(fileList.getSelectionModel().getSelectedItem() != null){
           //Requests the selected file from the server
           try {
               String filePath = filePathStart + fileList.getSelectionModel().getSelectedItem();
               client.requestFile(filePath);
               Utils.openFile(client.getFile(filePath));
           } catch (IOException e) {
               System.out.println("Unable to download file!");
           }
       }

    }


    //TODO - DELETE FILES, ADD NEW FILES (REMEMBER WE HAVE TO CHANGE THE FILE ON THE SERVER AS WELL)


}
