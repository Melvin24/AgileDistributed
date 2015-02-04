/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static myproj.mdfProject.populateMtd;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Melvin
 */
public class mdfTeam {
    private static Connection conn = mySQL.ConnectDb();
    private static final ObservableList<String> observableList = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListCopy = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListFrAdd = FXCollections.observableArrayList();
    public static void launchMdfTem (Stage Input, int prjID, int userID, int temID) { 
        //need to clear everything in the observable list for a fresh 
        //start when users go back to this class
        observableList.removeAll(observableList);
        observableListCopy.removeAll(observableListCopy);
        observableListFrAdd.removeAll(observableListFrAdd);
        startModify(Input, prjID, userID, temID);
    }

    public static void startModify(Stage primaryStageMdfyTem, int prjID, int userID, int temID) {
        primaryStageMdfyTem.setTitle("Modify Team");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,100,100,100));
        root.getChildren().add(grid);
        
        //adding title
        Text scenetitle = new Text("Modify Team");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        grid.add(scenetitle, 0, 2);
        
        //adding details labels and textareas
        Label teamName = new Label("Team Name:");
        grid.add(teamName,0, 3);
        TextField temNameTextField = new TextField();
        grid.add(temNameTextField, 1, 3); 
        
        Label teamCountry = new Label("Team Country:");
        grid.add(teamCountry,0, 4);
        TextField temCountryTextField = new TextField();
        grid.add(temCountryTextField, 1, 4); 
        
        Label teamState = new Label("Team State:");
        grid.add(teamState,0, 5);
        TextField temStateTextField = new TextField();
        grid.add(temStateTextField, 1, 5); 
        
        Label teamPstCde = new Label("Team PostCode:");
        grid.add(teamPstCde,0, 6);
        TextField teamPstCdeTextField = new TextField();
        grid.add(teamPstCdeTextField, 1, 6);
        
        Button mdfyBtn = new Button("        Modify        ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(mdfyBtn);
        grid.add(hbBtn, 1, 7);
        
        Text subtitle = new Text("Modify Members  ");
        subtitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        grid.add(subtitle, 0, 8);
        
        Label prjTemMembrs = new Label("Team Members:");
        grid.add(prjTemMembrs, 0, 9);
        
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(10, 120); 
        grid.add(listView,1,9); 
        
        Button addBtn = new Button("   Add Team Member   ");
        HBox hbAddBtn = new HBox(10);
        hbAddBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbAddBtn.getChildren().add(addBtn);
        grid.add(hbAddBtn, 2, 9); 
        
        Button rmvBtn = new Button("Remove Team Member");
        HBox hbDeleteBtn = new HBox(10);
        hbDeleteBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbDeleteBtn.getChildren().add(rmvBtn);
        grid.add(hbDeleteBtn, 2, 10); 
        
        Button cancelBtn = new Button("Go Back");
        HBox hbCancelBtn = new HBox(10);
        hbCancelBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbCancelBtn.getChildren().add(cancelBtn);
        grid.add(hbCancelBtn, 0, 11); 
        
        Button saveBtn = new Button("Save Changes");
        HBox hbSaveBtn = new HBox(10);
        hbSaveBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbSaveBtn.getChildren().add(saveBtn);
        grid.add(hbSaveBtn, 1, 11); 
        
        //Populating required fields
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from team where Team_ID='"+temID+"' and Team_Status = 'Active'");

            while(rs.next()){
                temNameTextField.setText(rs.getString("Team_Name"));
                temCountryTextField.setText(rs.getString("Team_Country"));
                temStateTextField.setText(rs.getString("Team_State"));
                teamPstCdeTextField.setText(rs.getString("Team_Post_Code"));
            }
            st.close();
            rs.close();
            populateMtd(prjID);
            listPrepare(prjID);
        }catch(Exception e){
            Dialogs.create()
                    .owner(primaryStageMdfyTem)
                    .title("Error")
                    .masthead("Oops there was an Error!")
                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                    .showError();
        }
        
        String oldTeamName = temNameTextField.getText();
        
        cancelBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    mainMenu.launchMainMenu(primaryStageMdfyTem, userID);                   
                }
            });
        
        mdfyBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    String teamName = temNameTextField.getText();
                    String teamCountry = temCountryTextField.getText();
                    String teamState = temStateTextField.getText();
                    String teamPstCode = teamPstCdeTextField.getText();
                    
                    if(!teamName.isEmpty() && !teamCountry.isEmpty() && !teamState.isEmpty() && !teamPstCode.isEmpty())
                    {
                        try{
                            //cheking if only team name changed and see if the new team name already taken 
                            //if it does them show error
                            if (!teamName.equalsIgnoreCase(oldTeamName) && check(teamName).equals(false))
                            {
                                Dialogs.create()
                                    .owner(primaryStageMdfyTem)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry the Team Name is already taken, Please use another")
                                    .showError();
                            }else{
                                String sql = "update team set Team_Name = ?, Team_Country =?, Team_State = ?, Team_Post_Code = ? where Team_ID = ?";
                                PreparedStatement pst = conn.prepareStatement(sql);
                                pst.setString(1, teamName);
                                pst.setString(2, teamCountry);
                                pst.setString(3, teamState);
                                pst.setString(4, teamPstCode);
                                pst.setInt(5, temID);

                                pst.execute();
                                Dialogs.create()
                                    .owner(primaryStageMdfyTem)
                                    .title("Information")
                                    .masthead("Good news!")
                                    .message("Successfully Modified")
                                    .showInformation();
                                pst.close();
                                //refreshing page
                                mdfTeam.launchMdfTem(primaryStageMdfyTem, prjID, userID, temID);
                            }
                        }catch (SQLException ex) {
                            Dialogs.create()
                            .owner(primaryStageMdfyTem)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry there was a Server Error, Please restart program or contact Admin")
                            .showError();
                        }   
                    }    
                }
        });
        
        addBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    Optional<String> response = Dialogs.create()
                        .owner(primaryStageMdfyTem)
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
        
        saveBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    Action confirm = Dialogs.create()
                        .owner(primaryStageMdfyTem)
                        .title("Confirm Dialog")
                        .masthead("Are you sure?")
                        .message("Are you Sure you want to save the following changes?")
                        .showConfirm();                   
                     
                    if (confirm == Dialog.ACTION_YES) { 
                    try{
                        //creating a variable to hold all new members 
                        String allNewMembrs = "";
                        //creating a varibale to hold old memebers
                        String allOldMembrs = "";
                        //looping through the observableList to identify if any new team members are added
                        for(String str : observableList)
                        {
                           //if the observableList holding the team members is not in the observableListCopy
                           //then a new member have been added
                           if(!observableListCopy.contains(str))
                           {
                               //add the new members to the allMembrs variable
                               allNewMembrs = allNewMembrs + str + ",";
                           }    
                        }
                        //looping through the observableListCopy to identify any team members that are deleted 
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
                            //reusing existing method to add team members to DB
                            createTeam.addingTeamMembers(arrayOfNewMembers, prjID, temID, userID);
                            System.out.println("Added a new team member");
                        }
                        //if old team members are deleted
                        if(!allOldMembrs.isEmpty())
                        {
                            
                                Statement stateMnt=conn.createStatement();
                                String[] arrayOfOldtemMember = allOldMembrs.split(",");
                                for (int i = 0; i<arrayOfOldtemMember.length;i++)
                                {
                                    int oldtemMemberUsrID = getUsrID(arrayOfOldtemMember[i]);
                                
                                    //delete from user_info table that team leaders entire team
                                    stateMnt.execute("delete from User_Info where Project_ID = '"+prjID+"' and User_ID = '"+oldtemMemberUsrID+"' and Team_ID = '"+temID+"'");
                                }
                            
                        }
                        
                        if(allOldMembrs.isEmpty() && allNewMembrs.isEmpty())
                        {
                            Dialogs.create()
                                .owner(primaryStageMdfyTem)
                                .title("Information")
                                .masthead("No Changes!")
                                .message("Sorry No changes where made, please try again.")
                                .showInformation();
                        }else{
                            Dialogs.create()
                                .owner(primaryStageMdfyTem)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Changes are successfully saved.")
                                .showInformation();
                        }
                    }catch (SQLException ex) {
                                Dialogs.create()
                                .owner(primaryStageMdfyTem)
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
                login.launchGUI(primaryStageMdfyTem);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageMdfyTem)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {   
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageMdfyTem)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    primaryStageMdfyTem.close();
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);
        menuBar.prefWidthProperty().bind(primaryStageMdfyTem.widthProperty());
        
        root.getChildren().add(menuBar);
            
        primaryStageMdfyTem.setScene(scene);
        primaryStageMdfyTem.setResizable(false);
        primaryStageMdfyTem.show();
    }
    
    //a methods for adding team leaders to a list 
    public static void populateMtd(int prjID){
        try{
            List<Integer> usrIdOfThsPrj = new ArrayList<>();
            Statement st=conn.createStatement();
            //get every thing where project id is the current project ID and the user is a team leader 
            ResultSet rs=st.executeQuery("select * from User_Info where Project_ID = '"+prjID+"' and Role_Type = 'Team Member' and Invite_Status = 'Confirm'");

            while(rs.next()){
                //add all the userid's related to this project
                usrIdOfThsPrj.add(rs.getInt("User_ID"));
            }
            rs.close();

            //now get the users names that are in the usrIdOfThsPrj list
            ResultSet rs2=st.executeQuery("select * from login where Account_Status = 'Active'");
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
            ResultSet rs2=st.executeQuery("select * from login where Account_Status = 'Active'");
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
    
    private static Boolean check(String temName)
    {
        try{
            Statement st=conn.createStatement();
            //if a team is disabled then allow users to use the existing team names 
            ResultSet rs=st.executeQuery("select * from team where Team_Name='"+temName+"' and Team_Status = 'Active'");

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