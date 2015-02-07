
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
        Scene scene = new Scene(root, 550, 500);
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
        
        
        Label prjNumSprints = new Label("Number of Sprints:");
        grid.add(prjNumSprints, 0, 7);       
        ObservableList<String> prjSprintOptions = FXCollections.observableArrayList(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Other..");
        ComboBox prjNumSprintCboBox = new ComboBox(prjSprintOptions);
        grid.add(prjNumSprintCboBox, 1, 7);
        TextField prjNumSprintsTextField = new TextField();
        prjNumSprintsTextField.setVisible(false);
        grid.add(prjNumSprintsTextField, 2, 7); 
        
        prjNumSprintCboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                if(t1.equals("Other..")){
                    prjNumSprintsTextField.setVisible(true); 
                }else{
                    prjNumSprintsTextField.setVisible(false); 
                }
            }    
        });
       
        Label prjBrief = new Label("Brief:");
        grid.add(prjBrief, 0, 8);
        TextArea briefTextArea = TextAreaBuilder.create()
                .prefWidth(10)
                .wrapText(true)
                .build();
        ScrollPane briefScrollPane = new ScrollPane();
        briefScrollPane.setContent(briefTextArea);
        briefScrollPane.setFitToWidth(true);
        briefScrollPane.setPrefWidth(10);
        briefScrollPane.setPrefHeight(120);
        grid.add(briefScrollPane,1, 8);
        
        Label prjctManifesto = new Label("Project Manifesto:");
        grid.add(prjctManifesto, 0, 9);
        TextField prjctManifestoTextField = new TextField();
        grid.add(prjctManifestoTextField, 1, 9);
        
        
        Button chooseBtn = new Button("Select Manifesto");
        HBox hbChooseBtn = new HBox(10);
        hbChooseBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbChooseBtn.getChildren().add(chooseBtn);
        grid.add(hbChooseBtn, 1, 10);  
        
        Button createBtn = new Button("Create");
        HBox hbCreateBtn = new HBox(10);
        hbCreateBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbCreateBtn.getChildren().add(createBtn);
        grid.add(hbCreateBtn, 1, 11);   
        
        Button cancelBtn = new Button("Cancel");
        HBox hbCancelBtn = new HBox(10);
        hbCancelBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbCancelBtn.getChildren().add(cancelBtn);
        grid.add(hbCancelBtn, 0, 11);
        
        //select manifesto button actionlistener
        chooseBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(primaryStageCreateProject);
                    //Set extension filter
//                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AVI files (*.avi)");
//                    fileChooser.getExtensionFilters().add(extFilter);
                    if(file != null)
                    {
                        prjctManifestoTextField.setText(file.getPath());
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
                    String numOfSprintChoice = prjSprintOptions.get(prjNumSprintCboBox.getSelectionModel().getSelectedIndex());
                    String getBrief = briefTextArea.getText();
                    String getManifestoPath = prjctManifestoTextField.getText();
                    //Getting current Date
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    java.util.Date utilDate = cal.getTime();
                    java.sql.Date sqlCurrentDate = new java.sql.Date(utilDate.getTime());
                    if(!getprjName.equals("") && !observableList.isEmpty() && getBrief.length()<=3000 && prjNumSprintCboBox.getSelectionModel().getSelectedIndex() >= 0){

                        if(numOfSprintChoice.equals("Other..") && !prjNumSprintsTextField.getText().matches("[0-9]+")){
                            Dialogs.create()
                                .owner(primaryStageCreateProject)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry Please specify the Appropriate Number of Sprints")
                                .showError();
                        }else{
                            int numOfSprint = 0;
                            if(numOfSprintChoice.equals("Other..")){
                                numOfSprint = Integer.parseInt(prjNumSprintsTextField.getText());
                            }else{
                                numOfSprint = Integer.parseInt(numOfSprintChoice);
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
                                    String sql = "Insert into project (Project_Name, PrjOwnrUser_ID, Project_Num_Sprints, Project_Status, Project_Brief, Project_Manifesto, Project_Created) values (?, ?, ?, ?, ?, ?, ?) ";
                                    PreparedStatement pst = conn.prepareStatement(sql);
                                    pst.setString(1, getprjName);//adding the project name
                                    pst.setInt(2, passdUsrID);//adding the owner name,  by default it is the one who created
                                    pst.setInt(3, numOfSprint);//adding the deadline date
                                    pst.setString(4, "Incomplete");// adding the project status, by default it is incomplete
                                    pst.setString(5, getBrief);//adding the project brief
                                    pst.setString(6, getManifestoPath);//adding the path for manifesto
                                    pst.setDate(7, sqlCurrentDate);//adding vurrent date 
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
