
package myproj;

import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class createNewProject {
    private static final Connection conn = mySQL.ConnectDb();
    private static final ObservableList<String> observableList = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListFrAdd = FXCollections.observableArrayList();
    private static String usrFullName  = null;
    public static void launchCreateProject (Stage Input, int passdUsrID) {
        //need to clear everything in the observable list for a fresh 
        //start when users go back to this class
        observableList.removeAll(observableList);
        observableListFrAdd.removeAll(observableListFrAdd);
        usrFullName = null;
        listPrepare(passdUsrID);
        startCreateProject(Input, passdUsrID);
    }

    private static void startCreateProject(Stage primaryStageCreateProject, int passdUsrID) {
        primaryStageCreateProject.setTitle("Create Project");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 550);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,100,100,90));
        root.getChildren().add(grid);
        
        
        //adding title
        Text scenetitle = new Text("Create Project");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 3);
        
        //adding details labels and textareas
        Label prjctName = new Label("Project Name:");
        grid.add(prjctName,0, 4);
        TextField prjctNameTextField = new TextField();
        grid.add(prjctNameTextField, 1, 4); 
        

        Label temLeaders = new Label("Team Leaders:");
        grid.add(temLeaders, 0, 5);
   
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(10, 80); 
        grid.add(listView,1,5); 
        
        //button to add teamleader
        Button addBtn = new Button("   Add Team Leader   ");
        HBox hbaddBtn = new HBox(10);
        hbaddBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbaddBtn.getChildren().add(addBtn);
        grid.add(hbaddBtn, 2, 5);
        
        //button to remove team members
        Button removeBtn = new Button("Remove Team Leader");
        HBox hbRemoveBtn = new HBox(10);
        hbRemoveBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbRemoveBtn.getChildren().add(removeBtn);
        grid.add(hbRemoveBtn, 2, 6);
        
        
        Label prjSprintsDuration = new Label("Sprint Duration (Hours):");
        grid.add(prjSprintsDuration, 0, 7);       
        ObservableList<String> prjSprintOptions = FXCollections.observableArrayList(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Other..");
        ComboBox prjSprintDurationCboBox = new ComboBox(prjSprintOptions);
        grid.add(prjSprintDurationCboBox, 1, 7);
        TextField prjSprintsDurationTextField = new TextField();
        prjSprintsDurationTextField.setPromptText("Enter Duration in Hours...");
        prjSprintsDurationTextField.setVisible(false);
        grid.add(prjSprintsDurationTextField, 1, 8); 
        
        prjSprintDurationCboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                if(t1.equals("Other..")){
                    prjSprintsDurationTextField.setVisible(true); 
                }else{
                    prjSprintsDurationTextField.setVisible(false); 
                }
            }    
        });
       
        Label prjBrief = new Label("Brief:");
        grid.add(prjBrief, 0, 9);
        TextArea briefTextArea = TextAreaBuilder.create()
                .prefWidth(10)
                .wrapText(true)
                .build();
        ScrollPane briefScrollPane = new ScrollPane();
        briefScrollPane.setContent(briefTextArea);
        briefScrollPane.setFitToWidth(true);
        briefScrollPane.setPrefWidth(10);
        briefScrollPane.setPrefHeight(120);
        grid.add(briefScrollPane,1, 9);
        Label briefInputCount = new Label("Max Input: 0/3000");
        briefInputCount.setAlignment(Pos.TOP_LEFT);
        grid.add(briefInputCount, 2, 9);
        
        //a listener for the projectBrief text area
        briefTextArea.setOnKeyTyped((javafx.scene.input.KeyEvent ke) -> {
            int briefLength = briefTextArea.getLength() + 1;
            //change the input according to the input length
            briefInputCount.setText("Max Input: " + briefLength + "/3000");
            
            //if length greater than 3000 char then change color of label to RED
            if(briefLength > 3000){
                briefInputCount.setTextFill(Color.rgb(255, 0, 0));
                
            }else{
                briefInputCount.setTextFill(Color.rgb(0, 0, 0));
            }
                
        });
        
        Label prjctDirtry = new Label("Project Directory:");
        grid.add(prjctDirtry, 0, 10);
        TextField prjctDirtryTextField = new TextField();
        prjctDirtryTextField.setEditable(false);
        grid.add(prjctDirtryTextField, 1, 10);
        
        
        Button chooseDirtryBtn = new Button("    Select Directory   ");
        HBox hbChooseDirtryBtn = new HBox(10);
        hbChooseDirtryBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbChooseDirtryBtn.getChildren().add(chooseDirtryBtn);
        grid.add(hbChooseDirtryBtn, 2, 10); 
        
        Label prjctManifesto = new Label("Project Manifesto:");
        grid.add(prjctManifesto, 0, 11);
        TextField prjctManifestoTextField = new TextField();
        prjctManifestoTextField.setEditable(false);
        grid.add(prjctManifestoTextField, 1, 11);
        
        
        Button chooseBtn = new Button("   Select Manifesto   ");
        HBox hbChooseBtn = new HBox(11);
        hbChooseBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbChooseBtn.getChildren().add(chooseBtn);
        grid.add(hbChooseBtn, 2, 11);  
        
        Button createBtn = new Button("Create");
        HBox hbCreateBtn = new HBox(10);
        hbCreateBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbCreateBtn.getChildren().add(createBtn);
        grid.add(hbCreateBtn, 1, 12);   
        
        Button cancelBtn = new Button("Cancel");
        HBox hbCancelBtn = new HBox(10);
        hbCancelBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbCancelBtn.getChildren().add(cancelBtn);
        grid.add(hbCancelBtn, 0, 12);
        
        //select manifesto button actionlistener
        chooseBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(primaryStageCreateProject);
                    if(file != null)
                    {
                        prjctManifestoTextField.setText(file.getPath());
                    }
                    
                }
            });
        
        //select a folder to be project Directory
        chooseDirtryBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File selectedDirectory = directoryChooser.showDialog(primaryStageCreateProject);
                 
                    if(selectedDirectory != null){
                        prjctDirtryTextField.setText(selectedDirectory.getAbsolutePath());
                    }       
                }
            });
        
        
        addBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    Optional<String> response = Dialogs.create()
                        .owner(primaryStageCreateProject)
                        .title("Team Names")
                        .masthead("Please Select a Team Members from List")
                        .message("Choose one at a time:")
                        .showChoices(observableListFrAdd);
                    if (response.isPresent()) {
                        observableList.add(0, response.get());
                        observableListFrAdd.remove(response.get());
                    }
                }
        });
        
        removeBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    int selectedIdx = listView.getSelectionModel().getSelectedIndex();
                    if (selectedIdx != -1) {
                        String itemToRemove = listView.getSelectionModel().getSelectedItem();
                        observableListFrAdd.add(itemToRemove);
                        int newSelectedIdx = (selectedIdx == listView.getItems().size() - 1)
                            ? selectedIdx - 1
                            : selectedIdx;
                        listView.getItems().remove(selectedIdx);
                        listView.getSelectionModel().select(newSelectedIdx);                       
                    }          
                }
        });
        
        //cancel button listener
        cancelBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    mainMenu.launchMainMenu(primaryStageCreateProject, passdUsrID);                 
                }
            });
       
        //create button listener
        createBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    String getprjName = prjctNameTextField.getText();
                    //getting other values from field
                    String numOfSprintDuration = prjSprintOptions.get(prjSprintDurationCboBox.getSelectionModel().getSelectedIndex());
                    String getBrief = briefTextArea.getText();
                    String getPrjDirtry = prjctDirtryTextField.getText();
                    String getManifestoPath = prjctManifestoTextField.getText();
                    //Getting current Date
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    java.util.Date utilDate = cal.getTime();
                    java.sql.Date sqlCurrentDate = new java.sql.Date(utilDate.getTime());
                    if(!getPrjDirtry.equals("") && !getprjName.equals("") && !observableList.isEmpty() && getBrief.length()<=3000 && prjSprintDurationCboBox.getSelectionModel().getSelectedIndex() >= 0){

                        if(numOfSprintDuration.equals("Other..") && !prjSprintsDurationTextField.getText().matches("[0-9]+")){
                            Dialogs.create()
                                .owner(primaryStageCreateProject)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry Please specify the Appropriate Sprint Duration")
                                .showError();
                        }else{
                            int numOfDurationSprint = 0;
                            if(numOfSprintDuration.equals("Other..")){
                                numOfDurationSprint = Integer.parseInt(prjSprintsDurationTextField.getText());
                            }else{
                                numOfDurationSprint = Integer.parseInt(numOfSprintDuration);
                            }
                            String allTemLdr = usrFullName + ",";
                            //converting observablelist to array for future iteration
                            for(String str : observableList)
                            {
                                allTemLdr = allTemLdr + str + ",";
                            }
                            String[] arrayOfTemLeaders = allTemLdr.split(",");
                        
                            try {
                                //cheking if project name already taken
                                //if it does them show error
                                if (check(getprjName).equals(false))
                                {
                                    Dialogs.create()
                                        .owner(primaryStageCreateProject)
                                        .title("Error")
                                        .masthead("Oops there was an Error!")
                                        .message("Sorry the Project Name is already taken, Please use another")
                                        .showError();
                                }else{
                                    String sql = "Insert into project (Project_Name, PrjOwnrUser_ID, Project_Sprint_Duration, Project_Status, Project_Brief, Project_Directory, Project_Manifesto, Project_Created) values (?, ?, ?, ?, ?, ?, ?) ";
                                    PreparedStatement pst = conn.prepareStatement(sql);
                                    pst.setString(1, getprjName);//adding the project name
                                    pst.setInt(2, passdUsrID);//adding the owner name,  by default it is the one who created
                                    pst.setInt(3, numOfDurationSprint);//adding the Sprint Duration
                                    pst.setString(4, "Incomplete");// adding the project status, by default it is incomplete
                                    pst.setString(5, getBrief);//adding the project brief
                                    pst.setString(6, getPrjDirtry);//adding the prj directory
                                    pst.setString(7, getManifestoPath);//adding the path for manifesto
                                    pst.setDate(8, sqlCurrentDate);//adding vurrent date 
                                    //execute 
                                    pst.execute();  
                                    pst.close();
                            
                                    //first get the project_ID
                                    Statement st=conn.createStatement();
                                    ResultSet rs=st.executeQuery("select * from project where Project_Name='"+getprjName+"' and PrjOwnrUser_ID='"+passdUsrID+"'");
                                    int prjctID = 0;
                                    while(rs.next()){
                                        prjctID = rs.getInt("Project_ID");
                                    }
                                    st.close();
                                    rs.close();

                                    //time to add to user_info table
                                    userInfo(arrayOfTemLeaders, prjctID, passdUsrID);
                            
                                    Dialogs.create()
                                        .owner(primaryStageCreateProject)
                                        .title("Information")
                                        .masthead("Good news!")
                                        .message("Successfully Create a new Project.")
                                        .showInformation();
                                    observableList.removeAll(observableList);
                                    observableListFrAdd.removeAll(observableListFrAdd);
                                    mainMenu.launchMainMenu(primaryStageCreateProject, passdUsrID);
                                }
                            
                            } catch (SQLException ex) {
                                Dialogs.create()
                                    .owner(primaryStageCreateProject)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                    .showError();
                            }
                        }
                    }else{
                        Dialogs.create()
                            .owner(primaryStageCreateProject)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Please make the following Changes: Populate Missing Fields Or Reduce Project Brief length to 3000 characters or less (including space!) ")
                            .showError();
                    }
                           
                }
            });
        
//*****************************************MENU ITEMS****************************************************************\\        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageCreateProject);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageCreateProject)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {
                    try {
                        conn.close();
                        primaryStageCreateProject.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                            .owner(primaryStageCreateProject)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry there was a Server Error Couldn't Close, Please restart program or contact Admin")
                            .showError();
                    }    
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);       
        menuBar.prefWidthProperty().bind(primaryStageCreateProject.widthProperty());
//***************************************MENU ITEMS END**************************************************************\\  
        
        root.getChildren().add(menuBar);     
        primaryStageCreateProject.setScene(scene);
        primaryStageCreateProject.setResizable(false);
        primaryStageCreateProject.show();

    }
    
    //A method for preparing the list
    private static void listPrepare( int passdID)
    {
        //getting items for the list ready before user select 'add'
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where Account_Status = 'Active'");

            while(rs.next()){
                //the project owner i.e the one creating the project cannot be a team leader of this project
                if(passdID == rs.getInt("User_ID"))
                {
                   usrFullName = rs.getString("name") + " " + rs.getString("surname");
                }else{
                   observableListFrAdd.add(rs.getString("name") + " " + rs.getString("surname")); 
                }
            }
            st.close();
            rs.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        } 
    }
    
    //a method to add selected team leaders to the user_infor table
    static void userInfo (String [] arrayOfTemLeaders, int pssdPrjID, int ownerID)
    {
        
        try{
            for (int i = 0; i<arrayOfTemLeaders.length;i++)
            {
                String[] splitSpace = arrayOfTemLeaders[i].split(" ");
                String splitName = splitSpace[0];
                String splitSurname = splitSpace[1];
                int userID = 0;
                
                //time to get ID of the user
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery("select * from login where name='"+splitName+"' and surname='"+splitSurname+"'");
                while(rs.next()){
                    userID = rs.getInt("User_ID");
                }
                st.close();
                rs.close();
                
                //got the ID now add to user_info table leaving teamID blank for later however the team id for projectowner will be 
                //blank always as he will have many teams
                String sql2 = "Insert into User_Info (User_ID, Project_ID, Role_Type, Invite_Status) values (?, ?, ?, ?) ";
                PreparedStatement pst2 = conn.prepareStatement(sql2);
                pst2.setInt(1, userID);
                pst2.setInt(2, pssdPrjID);
                //if he is a project owner make him a project owner otherwise a leader
                if(userID == ownerID)
                {
                    pst2.setString(3, "Project Owner");
                    pst2.setString(4, "Confirm");
                }else{
                    pst2.setString(3, "Team Leader");
                    pst2.setString(4, "In-Process");
                }
                    
                
                pst2.execute();               
                pst2.close();
                
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }        
    }
    
    //check method to see of project name already taken
    static Boolean check(String prjName)
    {
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from project where Project_Name='"+prjName+"'");

            int count=0;
            while(rs.next()){
                count++;
            }                      

            if(count>0){
                st.close();
                rs.close();
                return false;

            }
            else{
                st.close();
                rs.close();
                return true;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return false;
        } 
    }
    
    
}
