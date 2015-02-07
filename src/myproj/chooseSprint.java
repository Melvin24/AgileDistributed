/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproj;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Melvin
 */
public class chooseSprint {
    private static final Connection conn = mySQL.ConnectDb();
    public static void launchGUISprint (Stage input) { 
        startSprint(input);
    }
    
    public static void startSprint(Stage primaryStageSprint) {
        primaryStageSprint.setTitle("Choose Sprint");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 20));

        
        
        //adding title
        Text scenetitle = new Text("Choose Sprints");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 1, 3);

        ObservableList<String> observableList = FXCollections.observableArrayList();
        ObservableList<Integer> observableListSprintID = FXCollections.observableArrayList();
        ObservableList<Date> observableListSprtStrtDate = FXCollections.observableArrayList();
        ObservableList<Integer> observableListSprtDuration = FXCollections.observableArrayList();
        //obList to store Story IDs associated with sprints
        ObservableList<Integer> observableListStoryID = FXCollections.observableArrayList();
        //obList to store all the time_left for tasks that are completed
        ObservableList<Integer> observableListTimeLeft = FXCollections.observableArrayList();
        //obList to store all the date of starting for tasks that are Started
        ObservableList<Date> observableListDateStart = FXCollections.observableArrayList();
        //obList to store all the date of completing for tasks that are completed
        ObservableList<Date> observableListDateComplete = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(200, 350); 
        grid.add(listView,1, 4,1, 7);
        
        Button btn = new Button("View Burndown Chart");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 2, 4);
        
        try{
            Statement st=conn.createStatement();
            //sql to get user id from the user info table but only get the once that have confirmed to a project
            ResultSet rs = st.executeQuery("select * from Sprints");
            //an arraylist to store all projectid's that this user is involved in  
            while(rs.next())
            {
                observableList.add(rs.getString("Sprint_Name"));
                observableListSprintID.add(rs.getInt("Sprint_ID"));
                observableListSprtStrtDate.add(rs.getDate("Sprint_StartDate")); 
                observableListSprtDuration.add(rs.getInt("Sprint_Duration"));
                
            }
            rs.close();           
        }catch(Exception e){
            Dialogs.create()
                .owner(primaryStageSprint)
                .title("Error")
                .masthead("Oops there was an Error!")
                .message("Sorry there was a Server Error, Please restart program or contact Admin")
                .showError();
        } 

         btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try{
                    int sprntID = observableListSprintID.get(listView.getSelectionModel().getSelectedIndex());
                    int sprintDuration = observableListSprtDuration.get(listView.getSelectionModel().getSelectedIndex());
                    Date startDate = observableListSprtStrtDate.get(listView.getSelectionModel().getSelectedIndex());
                    //get the sprint id's associated with the selected story
                    Statement st=conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from Stories where Sprint_ID = '"+sprntID+"'");
                    while(rs.next())
                    {
                        //only add if its not already in the OBLIST
                        if(!observableListStoryID.contains(rs.getInt("Story_ID"))){
                           observableListStoryID.add(rs.getInt("Story_ID")); 
                           //System.out.println("Story IDs: " + rs.getInt("Story_ID"));
                        }
                        
                    }
                    rs.close();
                    //an int variable to count the total time allocated for this sprint
                    int totalTime = 0;
                    int totalNumOfTask = 0;
                    
                    for(int s: observableListStoryID){
                        ResultSet rs2 = st.executeQuery("select * from Tasks where Story_ID = '"+s+"'");
                        while(rs2.next())
                        {
                            if(rs2.getString("Status").equals("Complete")){
                                observableListTimeLeft.add(rs2.getInt("Time_Left"));
                                observableListDateStart.add(rs2.getDate("Start_Date"));
                                observableListDateComplete.add(rs2.getDate("Completed_Date"));
                                //System.out.println("Story IDs: " + s + " Time Left: " + rs2.getInt("Time_Left") + " Completed Date: " + rs2.getDate("Completed_Date"));
                            }
                            totalTime = totalTime + rs2.getInt("Time_Left");
                            totalNumOfTask = totalNumOfTask + 1;
                        }
                        rs2.close();
                    }
                    System.out.println("total num of task: " + totalNumOfTask);
                    st.close();
                    burnDown.launchBurnDown(primaryStageSprint, observableListTimeLeft, observableListDateStart, observableListDateComplete, totalTime, sprintDuration, totalNumOfTask);

                }catch(Exception ex){
                    Dialogs.create()
                .owner(primaryStageSprint)
                .title("Error")
                .masthead("Oops there was an Error!")
                .message("Sorry there was a Server Error, Please restart program or contact Admin")
                .showError();
        } 
            }
         });
            
        
        root.getChildren().add(grid);
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Register");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            //System.out.println("Item A Clicked");
            register.launchRgst(primaryStageSprint);
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageSprint)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {
                    primaryStageSprint.close();
                }         
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);       
        menuBar.prefWidthProperty().bind(primaryStageSprint.widthProperty());

        root.getChildren().add(menuBar);
           
        primaryStageSprint.setScene(scene);
        
        primaryStageSprint.setResizable(false);
        primaryStageSprint.show();
    }
}


