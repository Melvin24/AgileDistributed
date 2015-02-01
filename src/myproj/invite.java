package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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


public class invite  {
    private static Connection conn = mySQL.ConnectDb();
    public static void launchInvite (Stage Input, int userID) { 
        startInvite(Input, userID);
    }

    public static void startInvite(Stage primaryStageInvite, int userID) {
        primaryStageInvite.setTitle("Invites");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 20));

        //adding title
        Text scenetitle = new Text("Invites");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 1, 3);
        
        Text scenetitle2 = new Text("Details");
        scenetitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle2, 5, 3);
        
        ObservableList<String> observableListInvites = FXCollections.observableArrayList();
        ObservableList<String> observableListOwner = FXCollections.observableArrayList();
        ObservableList<String> observableListDeadline = FXCollections.observableArrayList();
        ObservableList<String> observableListYurRole = FXCollections.observableArrayList();
        ObservableList<String> observableListBrief = FXCollections.observableArrayList();
        ObservableList<String> observableListCreated = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<String>(observableListInvites);
        listView.setPrefSize(200, 350); 
        grid.add(listView,1, 4,1, 7);
        
        List<Integer> alPrjID = new ArrayList<>();
        try{
            Statement st=conn.createStatement();
            //sql to get user id from the user info table but only get the once that have not confirmed to a project
            ResultSet rs2 = st.executeQuery("select * from User_Info where User_ID='"+userID+"' and Invite_Status='In-Process'");
            //an arraylist to store all projectid's that this user is involved in  
            
            
            while(rs2.next())
            {
                //observableList.add(rs2.getString("projectName"));
                alPrjID.add(rs2.getInt("Project_ID"));
                observableListYurRole.add(rs2.getString("Role_Type"));
            }
            rs2.close();
            
            
            //looping through arraylist and adding to observable list to be displayed
            for (int s : alPrjID) {
                //querying database and adding project details to observable list
                ResultSet rs3 = st.executeQuery("select * from project where Project_ID='"+s+"'");
                while(rs3.next())
                {
                    observableListInvites.add(rs3.getString("Project_Name"));
                    observableListOwner.add(getName(rs3.getInt("PrjOwnrUser_ID")));
                    observableListDeadline.add(rs3.getString("Project_Deadline"));
                    observableListBrief.add(rs3.getString("Project_Brief"));
                    observableListCreated.add(rs3.getString("Project_Created"));
                }
                rs3.close();
                
            }
            
        }catch(Exception e){
            Dialogs.create()
                .owner(primaryStageInvite)
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
        
        
        Label prjctDeadline = new Label("Project Deadline:");
        grid.add(prjctDeadline, 5, 7);
        TextField prjctDeadlineTextField = new TextField();
        prjctDeadlineTextField.setEditable(false);
        grid.add(prjctDeadlineTextField, 6, 7);
        
        Label yurRoleStatus = new Label("Your Role:");
        grid.add(yurRoleStatus, 5, 8);
        TextField yurRoleTextField = new TextField();
        yurRoleTextField.setEditable(false);
        grid.add(yurRoleTextField, 6, 8);
        
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
        Button acceptBtn = new Button("Accept");
        acceptBtn.setDisable(true);
        HBox hbAcceptBtn = new HBox(10);
        hbAcceptBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbAcceptBtn.getChildren().add(acceptBtn);
        grid.add(hbAcceptBtn, 6, 10);
        
        Button declineBtn = new Button("Decline");
        declineBtn.setDisable(true);
        HBox hbDeclineBtn = new HBox(10);
        hbDeclineBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbDeclineBtn.getChildren().add(declineBtn);
        grid.add(hbDeclineBtn, 5, 10);
        
        Button goBackBtn = new Button("Go-Back");
        HBox hbGoBackBtn = new HBox(10);
        hbGoBackBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbGoBackBtn.getChildren().add(goBackBtn);
        grid.add(hbGoBackBtn, 6, 11);
        
        listView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, 
                String old_val, String new_val) {                   
                    acceptBtn.setDisable(false);
                    declineBtn.setDisable(false);
                    prjctNameTextField.setText(new_val);
                    prjctDeadlineTextField.setText(observableListDeadline.get(observableListInvites.indexOf(new_val)));
                    yurRoleTextField.setText(observableListYurRole.get(observableListInvites.indexOf(new_val)));
                    briefTextArea.setText(observableListBrief.get(observableListInvites.indexOf(new_val)));
                    prjctOwnrTextField.setText(observableListOwner.get(observableListInvites.indexOf(new_val))); 
                    prjctCreatedTextField.setText(observableListCreated.get(observableListInvites.indexOf(new_val)));    
                    
            }   
        });
        
        declineBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                        try{
                            //getting the project id of the project that the user is accepting 
                            int prjID = alPrjID.get(observableListInvites.indexOf(prjctNameTextField.getText())) ;
                            Statement st=conn.createStatement();
                            st.execute("delete from User_Info where User_ID ='"+userID+"' and Project_ID ='"+prjID+"'");
                            Dialogs.create()
                                .owner(primaryStageInvite)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully Declined the Invitation.")
                                .showInformation();
                            st.close();
                            //refresh the page 
                            invite.launchInvite(primaryStageInvite, userID);
                        }catch (SQLException ex) {
                                Dialogs.create()
                                    .owner(primaryStageInvite)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                    .showError();
                            }
                              
            }
        });
        
        acceptBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                        try{
                              //getting the index of the project id that the user is accepting 
                              int prjID = alPrjID.get(observableListInvites.indexOf(prjctNameTextField.getText())) ;
                              //getting the type of roal 
                              String roleType = observableListYurRole.get(alPrjID.indexOf(prjID));
                              //if the accepting user is a team member
                              if (roleType.equals("Team Member")){
                                  //update the team member's status to confirm
                                  String sql = "update User_Info set Invite_Status =? where User_ID = ? and Project_ID = ?";
                                  PreparedStatement pst = conn.prepareStatement(sql);
                                  pst.setString(1, "Confirm");
                                  pst.setInt(2, userID);
                                  pst.setInt(3, prjID);
                                  pst.execute();
                                  Dialogs.create()
                                    .owner(primaryStageInvite)
                                    .title("Information")
                                    .masthead("Good news!")
                                    .message("Successfully Joined this Project.")
                                    .showInformation();
                                  //refresh the page 
                                  invite.launchInvite(primaryStageInvite, userID);
                              //but if a team leader
                              }else if (roleType.equals("Team Leader")){
                                  createTeam.launchacceptInvite(primaryStageInvite, userID, prjID);
                              }
                        }catch (SQLException ex) {
                                Dialogs.create()
                                    .owner(primaryStageInvite)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                    .showError();
                            }
                              
            }
        });
        
        goBackBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                    mainMenu.launchMainMenu(primaryStageInvite, userID);               
            }
        });
        
        root.getChildren().add(grid);
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageInvite);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageInvite)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {   
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageInvite)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    primaryStageInvite.close();
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);
        menuBar.prefWidthProperty().bind(primaryStageInvite.widthProperty());
        
        root.getChildren().add(menuBar);
            
        primaryStageInvite.setScene(scene);
        primaryStageInvite.setResizable(false);
        primaryStageInvite.show();
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

