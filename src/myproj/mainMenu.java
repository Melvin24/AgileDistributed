
package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class mainMenu  {
    private static Connection conn = mySQL.ConnectDb();
    public static void launchMainMenu (Stage Input, int userID) { 
        startMainMenu(Input, userID);
    }
    
    public static void startMainMenu(Stage primaryStageMainMenu, int userID) {
        primaryStageMainMenu.setTitle("Main Menu");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 20));
        root.getChildren().add(grid);
        
        //adding title
        Text scenetitle = new Text("Projects");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 1, 3);
        
        Text scenetitle2 = new Text("Details");
        scenetitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle2, 5, 3);
                   
        //adding a listview
        ObservableList<String> observableList = FXCollections.observableArrayList();
        ObservableList<String> observableListOwner = FXCollections.observableArrayList();
        ObservableList<String> observableListSprintsDuration = FXCollections.observableArrayList();
        ObservableList<String> observableListStatus = FXCollections.observableArrayList();
        ObservableList<String> observableListYurRole = FXCollections.observableArrayList();
        ObservableList<String> observableListBrief = FXCollections.observableArrayList();
        ObservableList<String> observableListCreated = FXCollections.observableArrayList();
        
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(200, 350); 
        grid.add(listView,1, 4,1, 7);
        
        ObservableList<Integer> alPrjOwnrUsrID = FXCollections.observableArrayList();
        ObservableList<Integer> alPrjID = FXCollections.observableArrayList();
        ObservableList<Integer> alTemID = FXCollections.observableArrayList();
        try{
            Statement st=conn.createStatement();
            //sql to get user id from the user info table but only get the once that have confirmed to a project
            ResultSet rs2 = st.executeQuery("select * from User_Info where User_ID='"+userID+"' and Invite_Status='Confirm'");
            //an arraylist to store all projectid's that this user is involved in  
            while(rs2.next())
            {
                //observableList.add(rs2.getString("projectName"));
                alPrjID.add(rs2.getInt("Project_ID"));
                alTemID.add(rs2.getInt("Team_ID"));
                observableListYurRole.add(rs2.getString("Role_Type"));
            }
            rs2.close();
            int count = 0;
            //an array list to store the index of the top observablelist's that will not be added to sub Observable lists
            List<Integer> alUnusedIndex = new ArrayList<>();
            //looping through alPrjID obList and adding to subObservable list to be displayed
            for (int s : alPrjID) {
                
                //querying database and adding project details to observable list
                ResultSet rs3 = st.executeQuery("select * from project where Project_ID='"+s+"'");
                while(rs3.next())
                {
                    if(rs3.getString("Project_Status").equals("Incomplete") || rs3.getString("Project_Status").equals("Complete")){
                        observableList.add(rs3.getString("Project_Name"));
                        observableListOwner.add(getName(rs3.getInt("PrjOwnrUser_ID")));
                        if(userID == rs3.getInt("PrjOwnrUser_ID"))
                        {
                            alPrjOwnrUsrID.add(s);
                            //System.out.println(s);
                        }
                        observableListSprintsDuration.add(rs3.getString("Project_Sprint_Duration"));
                        observableListStatus.add(rs3.getString("Project_Status"));
                        observableListBrief.add(rs3.getString("Project_Brief"));
                        observableListCreated.add(rs3.getString("Project_Created"));
                    }else{
                        alUnusedIndex.add(count);
                    }

                    
                }
                count++;
                rs3.close(); 
            }
            //removing details of projects that are disabled from the obList's
            for (int i = 0; i < alUnusedIndex.size(); i++) {
                //System.out.println("removing prjID: " + alPrjID.get(i));
                alPrjID.remove(i);
                //System.out.println("removing TemID: " + alTemID.get(i));
                alTemID.remove(i);
                //System.out.println("removing YourRole Type: " + observableListYurRole.get(i));
                observableListYurRole.remove(i);
            }
            
        }catch(Exception e){
            Dialogs.create()
                .owner(primaryStageMainMenu)
                .title("Error")
                .masthead("Oops there was an Error!")
                .message("Sorry there was a Server Error, Please restart program or contact Admin")
                .showError();
        } 

        //adding details labels and textareas
        Label prjctName = new Label("Project Name:");
        grid.add(prjctName,5, 4);
        TextField prjctNameTextField = new TextField();
        prjctNameTextField.setEditable(false);
        grid.add(prjctNameTextField, 6, 4); 
        
        Label prjctOwnr = new Label("Project Owner:");
        grid.add(prjctOwnr, 5, 5);
        TextField prjctOwnrTextField = new TextField();
        prjctOwnrTextField.setEditable(false);
        grid.add(prjctOwnrTextField, 6, 5); 
        
        Label prjCreated = new Label("Project Created:");
        grid.add(prjCreated, 5, 6);
        TextField prjctCreatedTextField = new TextField();
        prjctCreatedTextField.setEditable(false);
        grid.add(prjctCreatedTextField, 6, 6);
        
        
        Label prjctSprint = new Label("Sprint Duration:");
        grid.add(prjctSprint, 5, 7);
        TextField prjctSprintTextField = new TextField();
        prjctSprintTextField.setEditable(false);
        grid.add(prjctSprintTextField, 6, 7);
        
        Label prjctStatus = new Label("Project Status:");
        grid.add(prjctStatus, 5, 8);
        TextField prjctStatusTextField = new TextField();
        prjctStatusTextField.setEditable(false);
        grid.add(prjctStatusTextField, 6, 8);
        
        Label prjBrief = new Label("Brief:");
        grid.add(prjBrief, 5, 9);
        TextArea briefTextArea = TextAreaBuilder.create()
                .prefWidth(10)
                .wrapText(true)
                .build();
        briefTextArea.setEditable(false);
        ScrollPane briefScrollPane = new ScrollPane();
        briefScrollPane.setContent(briefTextArea);
        briefScrollPane.setFitToWidth(true);
        briefScrollPane.setPrefWidth(10);
        briefScrollPane.setPrefHeight(150);
        grid.add(briefScrollPane,6, 9);
        
        //adding open button
        Button openBtn = new Button("Open");
        openBtn.setDisable(true);
        HBox hbOpenBtn = new HBox(10);
        hbOpenBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbOpenBtn.getChildren().add(openBtn);
        grid.add(hbOpenBtn, 6, 10);
        
        Button closeBtn = new Button("Close Project");
        closeBtn.setDisable(true);
        HBox hbCloseBtn = new HBox(10);
        hbCloseBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbCloseBtn.getChildren().add(closeBtn);
        grid.add(hbCloseBtn, 5, 10);
        
        closeBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                     Action confirm = Dialogs.create()
                        .owner(primaryStageMainMenu)
                        .title("Confirm Dialog")
                        .masthead("Are you sure?")
                        .message("Are you Sure you want to Close the Project? All details Associated with this project will be permanently Deleted")
                        .showConfirm(); 
                     
                    if (confirm == Dialog.ACTION_YES) { 
                        try{
                            //get the project ID of the project that is to be deleted
                            int slctdPrjID = alPrjID.get(listView.getSelectionModel().getSelectedIndex());
                            String sql = "update project set Project_Status = 'Disabled' where Project_ID = ?";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setInt(1, slctdPrjID);
                            pst.execute();
                            pst.close();
                            int selectedIdx = listView.getSelectionModel().getSelectedIndex();
                            if (selectedIdx != -1) {
                                int newSelectedIdx = (selectedIdx == listView.getItems().size() - 1)
                                    ? selectedIdx - 1
                                    : selectedIdx;
                                listView.getItems().remove(selectedIdx);
                                listView.getSelectionModel().select(newSelectedIdx);                       
                            } 
                            
                            Dialogs.create()
                                .owner(primaryStageMainMenu)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully Deleted This Project.")
                                .showInformation();
                        }catch (SQLException ex) {
                        Dialogs.create()
                            .owner(primaryStageMainMenu)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry there was a Server Error Couldn't Close, Please restart program or contact Admin")
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
                login.launchGUI(primaryStageMainMenu);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageMainMenu)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {
                    try {
                        conn.close();
                        primaryStageMainMenu.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                            .owner(primaryStageMainMenu)
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
        
//*******************************************The second topMenu****************************************\\
        Menu menuPhase2 = new Menu("Account");
        MenuItem menu2ItemA = new MenuItem("Modify Account");
        menu2ItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            mdfAccount.launchMdfAcc(primaryStageMainMenu, userID);
        }
        });
        
        MenuItem menu2ItemB = new MenuItem("Close Account");
        menu2ItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageMainMenu)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Close your Account?, All the details Associated with your Project will be permanently deleted")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {
                    try {
                            //loop through the alPrjOwnrUsrID arraylist and delete the project from the project table, team table and User_Info table 
                            //this loop deletes all data associated with the project whose project ownere is the current user 
                            for(int i: alPrjOwnrUsrID)
                            {
                                //check if a team exist for the projectID  = i
                                if(check(i).equals(false)){
                                   //then disable those teams
                                   String sql = "update team set Team_Status = 'Disabled' where Project_ID = ?";
                                   PreparedStatement pst = conn.prepareStatement(sql);
                                   pst.setInt(1, i);
                                   pst.execute();
                                   pst.close();
                                }
                                //then disable the project also
                                String sql2 = "update project set Project_Status = 'Disabled' where Project_ID = ?";
                                PreparedStatement pst2 = conn.prepareStatement(sql2);
                                pst2.setInt(1, i);
                                pst2.execute();
                                pst2.close();
                            }
                            //Now disable the user also
                            String sql3 = "update login set Account_Status = 'Disabled' where User_ID = ?";
                            PreparedStatement pst3 = conn.prepareStatement(sql3);
                            pst3.setInt(1, userID);
                            pst3.execute();
                            pst3.close();
                            
                            Dialogs.create()
                                .owner(primaryStageMainMenu)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully Deleted.")
                                .showInformation();
                            login.launchGUI(primaryStageMainMenu);
                            
                        } catch (SQLException ex) {
                            Dialogs.create()
                                .owner(primaryStageMainMenu)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                .showError();
                        }
                }   
        }
        });
        
        menuPhase2.getItems().add(menu2ItemA);
        menuPhase2.getItems().add(menu2ItemB);
        menuBar.getMenus().add(menuPhase2);

//*******************************************The Third topMenu****************************************\\
        Menu menuPhase3 = new Menu("Project");
        MenuItem menu3ItemA = new MenuItem("Create Project");
        menu3ItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            createNewProject.launchCreateProject(primaryStageMainMenu, userID);
            
        }
        });
        
        MenuItem menu3ItemB = new MenuItem("Modify Project");
        menu3ItemB.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            
            int prjID = alPrjID.get(listView.getSelectionModel().getSelectedIndex());
            mdfProject.launchMdfPrj(primaryStageMainMenu, prjID, userID);
        }
        });
        menu3ItemB.setDisable(true);
        
        MenuItem menu3ItemC = new MenuItem("Modify Team");
        menu3ItemC.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            
            int prjID = alPrjID.get(listView.getSelectionModel().getSelectedIndex());
            int temID = alTemID.get(listView.getSelectionModel().getSelectedIndex());
            mdfTeam.launchMdfTem(primaryStageMainMenu, prjID, userID, temID);
        }
        });
        menu3ItemC.setDisable(true);
        
        MenuItem menu3ItemD = new MenuItem("View Burndown");
        menu3ItemD.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            int prjID = alPrjID.get(listView.getSelectionModel().getSelectedIndex());
            chooseSprint.launchGUISprint(primaryStageMainMenu, prjID, userID);
        }
        });
        
        MenuItem menu3ItemE = new MenuItem("Invites");
        menu3ItemE.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            invite.launchInvite(primaryStageMainMenu, userID);
        }
        });
        
        menuPhase3.getItems().add(menu3ItemA);
        menuPhase3.getItems().add(menu3ItemB);
        menuPhase3.getItems().add(menu3ItemC);
        menuPhase3.getItems().add(menu3ItemD);
        menuPhase3.getItems().add(menu3ItemE);
        menuBar.getMenus().add(menuPhase3);
        
        menuBar.prefWidthProperty().bind(primaryStageMainMenu.widthProperty());
        
        //listener for list selection, when selected add the appropriate data to fields
        listView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, 
                String old_val, String new_val) {
                    openBtn.setDisable(false);
                    
                    prjctNameTextField.setText(new_val);
                    prjctSprintTextField.setText(observableListSprintsDuration.get(observableList.indexOf(new_val)));
                    prjctStatusTextField.setText(observableListStatus.get(observableList.indexOf(new_val)));
                    briefTextArea.setText(observableListBrief.get(observableList.indexOf(new_val)));
                    prjctOwnrTextField.setText(observableListOwner.get(observableList.indexOf(new_val))); 
                    prjctCreatedTextField.setText(observableListCreated.get(observableList.indexOf(new_val))); 
                    //if the selected project's owner is the logged-in user then allow them to modify a project
                    //if the selected project's team leader is the logged in user then allow them to modify their team details
                    if(observableListYurRole.get(observableList.indexOf(new_val)).equals("Project Owner"))
                    {
                        closeBtn.setDisable(false);
                        menu3ItemB.setDisable(false);
                        menu3ItemC.setDisable(true);
                    }else if (observableListYurRole.get(observableList.indexOf(new_val)).equals("Team Leader")){
                        menu3ItemC.setDisable(false);
                        menu3ItemB.setDisable(true);
                        closeBtn.setDisable(true);
                    }else{
                        menu3ItemB.setDisable(true);
                        closeBtn.setDisable(true);
                        menu3ItemC.setDisable(true);
                    }
            } 
        });

        root.getChildren().add(menuBar);
            
        primaryStageMainMenu.setScene(scene);
        primaryStageMainMenu.setResizable(false);
        primaryStageMainMenu.show();
        
        
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
    
    //cheking method
    private static Boolean check(int value)
    {
        try{

            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from team where Project_ID='"+value+"'");

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
