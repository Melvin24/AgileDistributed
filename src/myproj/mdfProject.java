
package myproj;

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


public class mdfProject  {
    private static Connection conn = mySQL.ConnectDb();
    private static final ObservableList<String> observableList = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListCopy = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListFrAdd = FXCollections.observableArrayList();
    public static void launchMdfPrj (Stage Input, int prjID, int userID) { 
        //need to clear everything in the observable list for a fresh 
        //start when users go back to this class
        observableList.removeAll(observableList);
        observableListCopy.removeAll(observableListCopy);
        observableListFrAdd.removeAll(observableListFrAdd);
        startModify(Input, prjID, userID);
    }

    public static void startModify(Stage primaryStageMdfyPrj, int prjID, int userID) {
        primaryStageMdfyPrj.setTitle("Modify Project");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 550);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,100,100,100));
        root.getChildren().add(grid);
        
        
        //adding title
        Text scenetitle = new Text("Modify Project");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        grid.add(scenetitle, 0, 2);
        
        //adding details labels and textareas
        Label prjctName = new Label("Project Name:");
        grid.add(prjctName,0, 3);
        TextField prjctNameTextField = new TextField();
        grid.add(prjctNameTextField, 1, 3); 
        
                
        Label prjctDeadline = new Label("Project Deadline:");
        grid.add(prjctDeadline, 0, 4);
        // Create the DatePicker.
        DatePicker datePicker = new DatePicker();
        datePicker.setEditable(false);
        grid.add(datePicker, 1, 4);
        
        Label prjctStatus = new Label("Project Status:");
        grid.add(prjctStatus,0, 5);
        ObservableList<String> prjStatusOptions = FXCollections.observableArrayList(
            "Incomplete",
            "Complete"  );
        ComboBox statusCboBox = new ComboBox(prjStatusOptions);
        statusCboBox.setValue("Incomplete");
        grid.add(statusCboBox, 1, 5); 
        
        Label prjBrief = new Label("Brief:");
        grid.add(prjBrief, 0, 6);
        TextArea briefTextArea = TextAreaBuilder.create()
                .prefWidth(10)
                .wrapText(true)
                .build();
        ScrollPane briefScrollPane = new ScrollPane();
        briefScrollPane.setContent(briefTextArea);
        briefScrollPane.setFitToWidth(true);
        briefScrollPane.setPrefWidth(10);
        briefScrollPane.setPrefHeight(100);
        grid.add(briefScrollPane,1, 6);
        
        Label prjctManifesto = new Label("Project Manifesto:");
        grid.add(prjctManifesto, 0, 7);
        TextField prjctManifestoTextField = new TextField();
        grid.add(prjctManifestoTextField, 1, 7);
                
        Button chooseBtn = new Button("    Select Manifesto    ");
        HBox hbChooseBtn = new HBox(10);
        hbChooseBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbChooseBtn.getChildren().add(chooseBtn);
        grid.add(hbChooseBtn, 2, 7); 
        
        Button mdfyBtn = new Button("        Modify        ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(mdfyBtn);
        grid.add(hbBtn, 1, 8);
        
        Text subtitle = new Text("Modify Team");
        subtitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        grid.add(subtitle, 0, 9);
        
        Label prjctOwnr = new Label("Project Owner:");
        grid.add(prjctOwnr,0, 10);
        TextField prjctOwnrTextField = new TextField();
        prjctOwnrTextField.setEditable(false);
        grid.add(prjctOwnrTextField, 1, 10); 
        
        //button to change project owner
        Button changeBtn = new Button("     Change Owner     ");
        HBox hbChangeBtn = new HBox(10);
        hbChangeBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbChangeBtn.getChildren().add(changeBtn);
        grid.add(hbChangeBtn, 2, 10);
        
        Label prjTemLeadrs = new Label("Team Leaders:");
        grid.add(prjTemLeadrs, 0, 11);
        
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(10, 70); 
        grid.add(listView,1,11);      

        Button addBtn = new Button("   Add Team Leader   ");
        HBox hbAddBtn = new HBox(10);
        hbAddBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbAddBtn.getChildren().add(addBtn);
        grid.add(hbAddBtn, 2, 11); 
        
        Button rmvBtn = new Button("Remove Team Leader");
        HBox hbDeleteBtn = new HBox(10);
        hbDeleteBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbDeleteBtn.getChildren().add(rmvBtn);
        grid.add(hbDeleteBtn, 2, 12); 
        
        Button cancelBtn = new Button("Go Back");
        HBox hbCancelBtn = new HBox(10);
        hbCancelBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbCancelBtn.getChildren().add(cancelBtn);
        grid.add(hbCancelBtn, 0, 13); 
        
        Button saveBtn = new Button("Save Changes");
        HBox hbSaveBtn = new HBox(10);
        hbSaveBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbSaveBtn.getChildren().add(saveBtn);
        grid.add(hbSaveBtn, 1, 13); 
        //Populating required fields
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from project where Project_ID='"+prjID+"'");

            while(rs.next()){
                prjctNameTextField.setText(rs.getString("Project_Name"));
                prjctOwnrTextField.setText(getName(rs.getInt("PrjOwnrUser_ID")));
                //getting the sql date from db
                java.sql.Date sqlDate = rs.getDate("Project_Deadline");
                //converting to util date
                java.util.Date newDate = new java.util.Date(sqlDate.getTime());
                //converting to localDate
                LocalDate dates = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                //assigning the date
                datePicker.setValue(dates);
                briefTextArea.setText(rs.getString("Project_Brief"));
                prjctManifestoTextField.setText(rs.getString("Project_Manifesto"));
            }
            st.close();
            rs.close();
            populateMtd(prjID);
            listPrepare(prjID);
        }catch(Exception e){
            Dialogs.create()
                    .owner(primaryStageMdfyPrj)
                    .title("Error")
                    .masthead("Oops there was an Error!")
                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                    .showError();
        }
        
        String oldprojectOwner = prjctOwnrTextField.getText();
        
        String oldProjectName = prjctNameTextField.getText();
        
        mdfyBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    String prjName = prjctNameTextField.getText();
                    //getting selected date 
                    LocalDate date = datePicker.getValue();
                    String prjStatus = prjStatusOptions.get(statusCboBox.getSelectionModel().getSelectedIndex());
                    //System.out.println(prjStatusOptions.get(statusCboBox.getSelectionModel().getSelectedIndex()));
                    String getBrief = briefTextArea.getText();
                    String getManifestoPath = prjctManifestoTextField.getText();
                    if(!prjName.equals("") && date != null && getBrief.length()<=3000 ){
                        // converting localDate to java.util.date and then to sql.Date
                        Date todate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        java.sql.Date sqlDate = new java.sql.Date(todate.getTime());
                        try{
                                //cheking only if project name changed and see if the new project name already taken 
                                //if it does them show error
                                if (!prjName.equalsIgnoreCase(oldProjectName) && createNewProject.check(prjName).equals(false))
                                {
                                Dialogs.create()
                                    .owner(primaryStageMdfyPrj)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry the Project Name is already taken, Please use another")
                                    .showError();
                                }else{
                                    String sql = "update project set Project_Name = ?, Project_Deadline =?, Project_Status = ?, Project_Brief = ?, Project_Manifesto =? where Project_ID = ?";
                                    PreparedStatement pst = conn.prepareStatement(sql);
                                    pst.setString(1, prjName);
                                    pst.setDate(2, sqlDate);
                                    pst.setString(3, prjStatus);
                                    pst.setString(4, getBrief);
                                    pst.setString(5, getManifestoPath);
                                    pst.setInt(6, prjID);

                                    pst.execute();
                                    Dialogs.create()
                                        .owner(primaryStageMdfyPrj)
                                        .title("Information")
                                        .masthead("Good news!")
                                        .message("Successfully Modified")
                                        .showInformation();
                                    //refreshing the page
                                    mdfProject.launchMdfPrj(primaryStageMdfyPrj, prjID, userID);
                                    pst.close();
                            }
                            
                        }catch (SQLException ex) {
                            Dialogs.create()
                            .owner(primaryStageMdfyPrj)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry there was a Server Error, Please restart program or contact Admin")
                            .showError();
                        }       
                    }                  
                }
            });
        
        chooseBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(primaryStageMdfyPrj);
                    if(file != null)
                    {
                        prjctManifestoTextField.setText(file.getPath());
                    }
                    
                }
            });
        
        cancelBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    mainMenu.launchMainMenu(primaryStageMdfyPrj, userID);                   
                }
            });
        
        addBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    Optional<String> response = Dialogs.create()
                        .owner(primaryStageMdfyPrj)
                        .title("Team Names")
                        .masthead("Please Select a Team Leader from List")
                        .message("Choose one at a time:")
                        .showChoices(observableListFrAdd);
                    if (response.isPresent()) {
                        observableList.add(0, response.get());
                        observableListFrAdd.remove(response.get());
                    }
                }
        });
        
        rmvBtn.setOnAction(
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
        
        changeBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override                
                public void handle(final ActionEvent e) {    
                    Action confirm = Dialogs.create()
                        .owner(primaryStageMdfyPrj)
                        .title("Confirm Dialog")
                        .masthead("Are you sure?")
                        .message("Are you Sure you want to Change the Project Owner")
                        .showConfirm();
                    
                    if (confirm == Dialog.ACTION_YES) {
                         Optional<String> response = Dialogs.create()
                            .owner(primaryStageMdfyPrj)
                            .title("Team Names")
                            .masthead("Please Select Team Members from List")
                            .message("Choose one at a time:")
                            .showChoices(observableListFrAdd);                        
                            //when project ownere changed
                            if (response.isPresent()){  
                                String oldOwnrName = prjctOwnrTextField.getText();
                                //then change the old name to new name in the prjctOwnrTextField 
                                prjctOwnrTextField.setText(response.get());    
                                //setting the old owner's name to observableListFrAdd after changed, so that he can now be added as a team member 
                                observableListFrAdd.add(oldOwnrName);
                                //removing the new owner's name from observableListFrAdd
                                observableListFrAdd.remove(response.get());
                         }
                    }               
                }
        });
        
        saveBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                     Action confirm = Dialogs.create()
                        .owner(primaryStageMdfyPrj)
                        .title("Confirm Dialog")
                        .masthead("Are you sure?")
                        .message("Are you Sure you want to save the following changes?")
                        .showConfirm();                   
                     
                    if (confirm == Dialog.ACTION_YES) { 
                      try{
                        boolean ownrChanged = false;
                        boolean temLdrChanged = false;
                        
                        String newOwnrName = prjctOwnrTextField.getText();
                        //creating a variable to hold all new members 
                        String allNewMembrs = "";
                        //creating a varibale to hold old memebers
                        String allOldMembrs = "";
                        //checking if the project owner changed, if so 
                        if(!newOwnrName.equals(oldprojectOwner))
                        {
                            //getting the user ID of the project owner that is about to change
                            int oldPrjOwnrUsrID = getUsrID(oldprojectOwner);
                            //getting the user ID of the new project owner 
                            int newPrjOwnrUsrID = getUsrID(newOwnrName);
                            //this query will then delete the row of the project owner that is changed from the user_Info table
                            Statement st=conn.createStatement();
                            st.execute("delete from User_Info where User_ID ='"+oldPrjOwnrUsrID+"' and Role_Type = 'Project Owner' and Project_ID = '"+prjID+"'");
                            st.close();
                            //then change the project owner old name to the new name in the project table
                            String sql = "update project set PrjOwnrUser_ID = ? where Project_ID = ?";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setInt(1, newPrjOwnrUsrID);
                            pst.setInt(2, prjID);
                            pst.execute();
                            pst.close();

                            //then add new row to the user_Info table
                            String sql2 = "Insert into User_Info (User_ID, Project_ID, Role_Type, Invite_Status) values (?, ?, ?, ?) ";
                            PreparedStatement pst2 = conn.prepareStatement(sql2);
                            pst2.setInt(1, newPrjOwnrUsrID);
                            pst2.setInt(2, prjID);
                            pst2.setString(3, "Project Owner");
                            pst2.setString(4, "Confirm");
                            pst2.execute();               
                            pst2.close();   
                            ownrChanged = true;
                        }
                        //looping through the observableList to identify if any new team leaders are added
                        for(String str : observableList)
                        {
                           //if the observableList holding the team leaders is not in the observableListCopy
                           //then a new member have been added
                           if(!observableListCopy.contains(str))
                           {
                               //add the new members to the allMembrs variable
                               allNewMembrs = allNewMembrs + str + ",";
                           }    
                        }
                        //looping through the observableListCopy to identify any team leaders that are deleted 
                        for(String str2 : observableListCopy)
                        {
                            //if the observableListCopy holding old team members is not in the observableList
                            //then an old member is deleted
                            if(!observableList.contains(str2))
                            {
                                //add that old member to the String
                                allOldMembrs = allOldMembrs + str2 + ",";
                            }
                        }
                        //if new team leaders are added 
                        if(!allNewMembrs.isEmpty()){
                            
                            String[] arrayOfNewMembers = allNewMembrs.split(",");
                            //reusing existing method to add team leaders to DB
                            createNewProject.userInfo(arrayOfNewMembers, prjID, -1);
                            temLdrChanged = true;
                        }
                        //if old team leaders are deleted
                        if(!allOldMembrs.isEmpty())
                        {
                            Statement stateMnt=conn.createStatement();
                            String[] arrayOfOldtrmLedr = allOldMembrs.split(",");
                            for (int i = 0; i<arrayOfOldtrmLedr.length;i++)
                            {
                                //for each deleted old team leader get their team ID
                                //first get the user ID
                                int oldtemLedrUsrID = getUsrID(arrayOfOldtrmLedr[i]);
                                //varibale to store teamID
                                int teamID = 0;
                                ResultSet rs=stateMnt.executeQuery("select * from User_Info where Project_ID = '"+prjID+"' and User_ID = '"+oldtemLedrUsrID+"' and Role_Type = 'Team Leader' and Invite_Status = 'Confirm'");
                                
                                while(rs.next()){
                                    //add all the userid's related to this project
                                    teamID = rs.getInt("Team_ID");
                                    //System.out.println("this is teamID: " + teamID);
                                }
                                rs.close();
                                
                                //delete from user_info table that team leaders entire team
                                stateMnt.execute("delete from User_Info where Project_ID = '"+prjID+"' and Team_ID = '"+teamID+"'");
                                
                                //deleting from team table
                                stateMnt.execute("delete from team where Team_ID ='"+teamID+"' and Project_ID = '"+prjID+"'");                                
                            }
                            stateMnt.close();
                            temLdrChanged = true;
                        }
                        
                        if(ownrChanged != true && temLdrChanged != true)
                        {
                            Dialogs.create()
                                .owner(primaryStageMdfyPrj)
                                .title("Information")
                                .masthead("No Changes!")
                                .message("Sorry No changes where made, please try again.")
                                .showInformation();
                        }else{
                            Dialogs.create()
                                .owner(primaryStageMdfyPrj)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Changes are successfully saved.")
                                .showInformation();
                            //refreshing page
                            mdfProject.launchMdfPrj(primaryStageMdfyPrj, prjID, userID);
                        }
                     } catch (SQLException ex) {
                                Dialogs.create()
                                .owner(primaryStageMdfyPrj)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                .showError();
                     }
                    }  
                }
        });
        
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageMdfyPrj);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageMdfyPrj)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {   
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageMdfyPrj)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    primaryStageMdfyPrj.close();
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);
        menuBar.prefWidthProperty().bind(primaryStageMdfyPrj.widthProperty());
        
        root.getChildren().add(menuBar);
            
        primaryStageMdfyPrj.setScene(scene);
        primaryStageMdfyPrj.setResizable(false);
        primaryStageMdfyPrj.show();
    }
    
    //a methods for adding team leaders to a list 
    public static void populateMtd(int prjID){
        try{
            List<Integer> usrIdOfThsPrj = new ArrayList<>();
            Statement st=conn.createStatement();
            //get every thing where project id is the current project ID and the user is a team leader 
            ResultSet rs=st.executeQuery("select * from User_Info where Project_ID = '"+prjID+"' and Role_Type = 'Team Leader' and Invite_Status = 'Confirm'");

            while(rs.next()){
                //add all the userid's related to this project
                usrIdOfThsPrj.add(rs.getInt("User_ID"));
            }
            rs.close();

            //now get the users names that are in the usrIdOfThsPrj list
            ResultSet rs2=st.executeQuery("select * from login");
            while(rs2.next()){
                if(usrIdOfThsPrj.contains(rs2.getInt("User_ID")))
                {
                  observableList.add(rs2.getString("name") + " " + rs2.getString("surname")); 
                  //an observableListCopy to be user later
                  observableListCopy.add(rs2.getString("name") + " " + rs2.getString("surname")); 
                }
            }
            rs2.close();
            st.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        } 
    }
    
    private static void listPrepare(int passdPrjID)
    {
        //getting items for the list ready before user select 'add'
        //the pourpose of this method is to get the individuals that are not already in this project 
        //we dont want the same people involved in this project to be displayed
        try{
            List<Integer> usrId = new ArrayList<>();
            Statement st=conn.createStatement();
            //get every thing where project id is the current project ID
            ResultSet rs=st.executeQuery("select * from User_Info where Project_ID = '"+passdPrjID+"'");

            while(rs.next()){
                //add all the userid's related to this project
                usrId.add(rs.getInt("User_ID"));
            }
            rs.close();
            
            //usrIdOfThsPrj.co
            //now get the users that are not in the usrIdOfThsPrj list
            ResultSet rs2=st.executeQuery("select * from login");
            while(rs2.next()){
                if(!usrId.contains(rs2.getInt("User_ID")))
                {
                  observableListFrAdd.add(rs2.getString("name") + " " + rs2.getString("surname"));  
                }
            }
            rs2.close();
            st.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        } 
    }
    
    private static int getUsrID(String name){
        try{
            String[] splitSpace = name.split(" ");
            String splitName = splitSpace[0];
            String splitSurname = splitSpace[1];
            int getUserID = 0;

            //time to get ID of the user
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where name='"+splitName+"' and surname='"+splitSurname+"'");
            while(rs.next()){
                getUserID = rs.getInt("User_ID");
            }
            st.close();
            rs.close();
            return getUserID;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return 0; 
        } 
    }
    
    private static String getName(int usrID )
    {
        //getting items for the list ready before user select 'add'
        try{
            String nameUser = "";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where User_ID='"+usrID+"'");

            while(rs.next()){
                nameUser = (rs.getString("name") + " " + rs.getString("surname"));      
            }
            st.close();
            rs.close();
            return nameUser;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
            return null;
        } 
    }
        
}