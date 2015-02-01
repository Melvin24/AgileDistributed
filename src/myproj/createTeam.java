package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


public class createTeam  {
    private static Connection conn = mySQL.ConnectDb();
    private static final ObservableList<String> observableList = FXCollections.observableArrayList();
    private static final ObservableList<String> observableListFrAdd = FXCollections.observableArrayList();
    private static String usrFullName  = null;
    public static void launchacceptInvite (Stage Input, int userID, int passdPrjID) { 
        //need to clear everything in the observable list for a fresh 
        //start when users go back to this class
        observableList.removeAll(observableList);
        observableListFrAdd.removeAll(observableListFrAdd);
        usrFullName = null;
        startAcceptInvite(Input, userID, passdPrjID);
    }

    public static void startAcceptInvite(Stage primaryStageAcceptInvite, int userID, int passdPrjID) {
        primaryStageAcceptInvite.setTitle("Create a Team");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(40,100,100,100));
        
        //observable list to store countries 
        ObservableList<String> obListCntry = FXCollections.observableArrayList();
        
        //observable list to store states 
        ObservableList<String> obListState = FXCollections.observableArrayList();
        HashMap hm = new HashMap();
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login");
            while(rs.next()){
                hm.put(rs.getString("state"), rs.getString("country"));
                if(!obListCntry.contains(rs.getString("country"))){
                   obListCntry.add(rs.getString("country")); 
                }
                
            }
            st.close();
            rs.close();
            
        }catch (SQLException ex) {
            Dialogs.create()
            .owner(primaryStageAcceptInvite)
            .title("Error")
            .masthead("Oops there was an Error!")
            .message("Sorry there was a Server Error, Please restart program or contact Admin")
            .showError();
        }
        
        

        Text scenetitle = new Text("Create a Team");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 1, 0);
        
        Label teamName = new Label("Team Name:");
        grid.add(teamName,1, 1);
        TextField tamNameTextField = new TextField();
        grid.add(tamNameTextField, 2, 1); 
        
        Label teamCountry = new Label("Country:");
        grid.add(teamCountry,1, 2);
        ComboBox temCntryCboBox = new ComboBox(obListCntry);
        grid.add(temCntryCboBox, 2, 2);
        
        Label teamState = new Label("State:");
        grid.add(teamState,1, 3);
        TextField tamStateTextField = new TextField();
        tamStateTextField.setEditable(false);
        grid.add(tamStateTextField, 2, 3);
        
        Button addStateBtn = new Button("           Add State           ");
        HBox hbAddStateBtn = new HBox(10);
        hbAddStateBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbAddStateBtn.getChildren().add(addStateBtn);
        grid.add(hbAddStateBtn, 3, 3);
        
        Label teamPostCode = new Label("Post Code:");
        grid.add(teamPostCode,1, 4);
        TextField tamPostCodeTextField = new TextField();
        grid.add(tamPostCodeTextField, 2, 4);
        
        Label temMemeber = new Label("Team Memeber:");
        grid.add(temMemeber, 1, 5);
        
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(10, 120); 
        grid.add(listView,2,5); 
        
        //button to add team members
        Button addBtn = new Button("   Add Team Member   ");
        HBox hbaddBtn = new HBox(10);
        hbaddBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbaddBtn.getChildren().add(addBtn);
        grid.add(hbaddBtn, 3, 5);
        
        //button to remove team members
        Button removeBtn = new Button("Remove Team Member");
        HBox hbRemoveBtn = new HBox(10);
        hbRemoveBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbRemoveBtn.getChildren().add(removeBtn);
        grid.add(hbRemoveBtn, 3, 6);
        
        //button to create the team
        Button backBtn = new Button("Go Back");
        HBox hbBackBtn = new HBox(10);
        hbBackBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBackBtn.getChildren().add(backBtn);
        grid.add(hbBackBtn, 1, 7);
        
        //button to create the team
        Button createBtn = new Button("Create Team");
        HBox hbcreateBtn = new HBox(10);
        hbcreateBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbcreateBtn.getChildren().add(createBtn);
        grid.add(hbcreateBtn, 2, 7);
        
        listPrepare(userID, passdPrjID);
        
        addStateBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    //remover everything if the obListState contain any data
                    obListState.removeAll(obListState);
                    //if no country selected first then throw error
                    if(temCntryCboBox.getSelectionModel().getSelectedIndex() < 0)
                    {
                        Dialogs.create()
                            .owner(primaryStageAcceptInvite)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry Please select a Country Before selecting a State")
                            .showError();
                    }else{
                        //iterate through hashMap and identify all the states associated with the selected country
                        Iterator<String> keySetIterator = hm.keySet().iterator();

                        while(keySetIterator.hasNext()){
                            String key = keySetIterator.next();
                            if(hm.get(key).equals(obListCntry.get(temCntryCboBox.getSelectionModel().getSelectedIndex()))){
                                obListState.add(key);
                            }
                        }
                        //show them in a dialogue box
                        Optional<String> response = Dialogs.create()
                            .owner(primaryStageAcceptInvite)
                            .title("Team Names")
                            .masthead("Please Select a State from List")
                            .message("Choose one at a time:")
                            .showChoices(obListState);
                        if (response.isPresent()) {
                            tamStateTextField.setText(response.get());
                        }
                    }
                }
        });
        
        addBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    Optional<String> response = Dialogs.create()
                        .owner(primaryStageAcceptInvite)
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
        
        backBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    invite.launchInvite(primaryStageAcceptInvite, userID);        
                }
        });
        
        createBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    String temName = tamNameTextField.getText();
                    String temCountry = obListCntry.get(temCntryCboBox.getSelectionModel().getSelectedIndex());
                    String temState = tamStateTextField.getText();
                    String temPostCode = tamPostCodeTextField.getText();
                    if(!temName.equals("") && !temCountry.equals("") && !temState.equals("") && !temPostCode.equals(""))
                    {
                        String allTemMmbrs = usrFullName + ",";
                        //converting observablelist to array for future iteration
                        for(String str : observableList)
                        {
                            allTemMmbrs = allTemMmbrs + str + ",";
                        }
                        String[] arrayOfTeamMembers = allTemMmbrs.split(",");
                        
                        //cheking if team name already taken
                        //if it does them show error
                        if (check(temName).equals(false))
                        {
                            Dialogs.create()
                                .owner(primaryStageAcceptInvite)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry the Team Name is already taken, Please use another")
                                .showError();
                        }else{
                            try {
                                String sql = "Insert into team (Project_ID, Team_Name, Team_Country, Team_State, Team_Post_Code) values (?, ?, ?, ?, ?) ";
                                PreparedStatement pst = conn.prepareStatement(sql);
                                pst.setInt(1, passdPrjID);
                                pst.setString(2, temName);
                                pst.setString(3, temCountry);
                                pst.setString(4, temState);
                                pst.setString(5, temPostCode);
                                pst.execute();  
                            
                                pst.close();
                            
                                //getting the team ID
                                Statement st=conn.createStatement();
                                ResultSet rs=st.executeQuery("select * from team where Project_ID='"+passdPrjID+"' and Team_Name='"+temName+"'");
                                int temID = 0;
                                while(rs.next()){
                                    temID = rs.getInt("Team_ID");
                                }
                                st.close();
                                rs.close();
                                
                                //now calling another method to recieve user ids of the selected team memebers and then add to the user_info table
                                addingTeamMembers(arrayOfTeamMembers, passdPrjID, temID, userID);
                            
                                Dialogs.create()
                                    .owner(primaryStageAcceptInvite)
                                    .title("Information")
                                    .masthead("Good news!")
                                    .message("Successfully Create a new Team.")
                                    .showInformation();
                                //go back to Invites
                                invite.launchInvite(primaryStageAcceptInvite, userID);
                            
                            }catch (SQLException ex) {
                                Dialogs.create()
                                    .owner(primaryStageAcceptInvite)
                                    .title("Error")
                                    .masthead("Oops there was an Error!")
                                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                                    .showError();
                            }
                        }
                    }else{
                        Dialogs.create()
                            .owner(primaryStageAcceptInvite)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message(" Sorry all the fields must be populated, Please try again.")
                            .showError();
                    }
                }
        });
        
        root.getChildren().add(grid);
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageAcceptInvite);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageAcceptInvite)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {   
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageAcceptInvite)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    primaryStageAcceptInvite.close();
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);
        menuBar.prefWidthProperty().bind(primaryStageAcceptInvite.widthProperty());
        
        root.getChildren().add(menuBar);
            
        primaryStageAcceptInvite.setScene(scene);
        primaryStageAcceptInvite.setResizable(false);
        primaryStageAcceptInvite.show();
    }
    
    private static void listPrepare(int userID, int passdPrjID)
    {
        //getting items for the list ready before user select 'add'
        //the pourpose of this method is to get the individuals that are not already in this project 
        //we dont want the same people involved in this project to be displayed
        try{
            List<Integer> usrIdOfThsPrj = new ArrayList<>();
            Statement st=conn.createStatement();
            //get every thing where project id is the current project ID
            ResultSet rs=st.executeQuery("select * from User_Info where Project_ID = '"+passdPrjID+"'");

            while(rs.next()){
                //add all the userid's related to this project
                usrIdOfThsPrj.add(rs.getInt("User_ID"));
            }
            rs.close();
            
            //usrIdOfThsPrj.co
            //now get the users that are not in the usrIdOfThsPrj list
            ResultSet rs2=st.executeQuery("select * from login");
            while(rs2.next()){
                if(!usrIdOfThsPrj.contains(rs2.getInt("User_ID")))
                {
                  observableListFrAdd.add(rs2.getString("name") + " " + rs2.getString("surname"));  
                }else if(userID == rs2.getInt("User_ID"))
                {
                    usrFullName = rs2.getString("name") + " " + rs2.getString("surname");
                }
            }
            rs2.close();
            st.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        } 
    }
    
    private static Boolean check(String temName)
    {
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from team where Team_Name='"+temName+"'");

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
    
    static void addingTeamMembers(String[] arrayOfTeamMembers, int passdPrjID, int temID, int userID)
    {
        try{
            for (int i = 0; i<arrayOfTeamMembers.length; i++)
            {
                String[] splitSpace = arrayOfTeamMembers[i].split(" ");
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
                
                //got the user ID, time to add to user_info table
                // if it is a team leader i.e the passed userID from invite class is same as the generated user id 
                //then update otherwise insert
                if(getUserID == userID )
                {
                    String sql = "update User_Info set Team_ID = ?, Invite_Status =? where User_ID = ? and Project_ID = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, temID);
                    pst.setString(2, "Confirm");
                    pst.setInt(3, getUserID);
                    pst.setInt(4, passdPrjID);
                    pst.execute();
                    
                    pst.close();
                }else{
                    //else insert new
                    String sql2 = "Insert into User_Info (User_ID, Project_ID, Team_ID, Role_Type, Invite_Status) values (?, ?, ?, ?, ?) ";
                    PreparedStatement pst2 = conn.prepareStatement(sql2);
                    pst2.setInt(1, getUserID);
                    pst2.setInt(2, passdPrjID);
                    pst2.setInt(3, temID);
                    pst2.setString(4, "Team Member");
                    pst2.setString(5, "In-Process");
                    
                    pst2.execute();
                    
                    pst2.close();
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        } 
    }
        
}
