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
import javafx.scene.chart.LineChart;
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
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
    private static  int sumOfSprintPoints = 0;
    private static  int sumOfSprintHours = 0;
    public static void launchGUISprint (Stage input, int prjID, int userID) { 
        startSprint(input, prjID, userID);
    }
    
    public static void startSprint(Stage primaryStageSprint, int prjID, int userID) {
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
        ObservableList<Integer> observableListAllSprintID = FXCollections.observableArrayList();
        ObservableList<Integer> obListCompletedSprintID = FXCollections.observableArrayList();
        ObservableList<Integer> obListCompletedSprintPoints = FXCollections.observableArrayList();
        ObservableList<Integer> obListCompletedSprintHours = FXCollections.observableArrayList();

        
        ListView<String> listView = new ListView<String>(observableList);
        listView.setPrefSize(200, 400); 
        grid.add(listView,1, 4,1, 7);
        
        Button btn = new Button("View Sprint Burndown ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 2, 4);
        
        Button prjBurnDownBtn = new Button("View Project Burndown");
        HBox prjBurnDownhbBtn = new HBox(10);
        prjBurnDownhbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        prjBurnDownhbBtn.getChildren().add(prjBurnDownBtn);
        grid.add(prjBurnDownhbBtn, 2, 5);
        
        Button goBackBtn = new Button("            Go Back             ");
        HBox goBackhbBtn = new HBox(10);
        goBackhbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        goBackhbBtn.getChildren().add(goBackBtn);
        grid.add(goBackhbBtn, 2, 6);
        
        
        
        try{
            Statement st=conn.createStatement();
            //sql to get user id from the user info table but only get the once that have confirmed to a project
            ResultSet rs = st.executeQuery("select * from Sprints where Project_ID = '"+prjID+"'");
            //an arraylist to store all projectid's that this user is involved in  
            while(rs.next())
            {
                sumOfSprintPoints = sumOfSprintPoints + rs.getInt("Sprint_Point");
                sumOfSprintHours = sumOfSprintHours + rs.getInt("Sprint_Duration");
                observableList.add(rs.getString("Sprint_Name"));
                observableListAllSprintID.add(rs.getInt("Sprint_ID"));
                
                if(rs.getString("Sprint_Status").equals("Complete")){
                    obListCompletedSprintID.add(rs.getInt("Sprint_ID"));
                    obListCompletedSprintPoints.add(rs.getInt("Sprint_Point"));
                    obListCompletedSprintHours.add(rs.getInt("Sprint_Duration"));
                }
                
            }
            rs.close(); 
            st.close();          
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
                    String burnDownChartTitle = observableList.get(listView.getSelectionModel().getSelectedIndex());
                    int selectedSprintID = observableListAllSprintID.get(listView.getSelectionModel().getSelectedIndex());
                    int sumOfStoryPoints = 0;
                    int sumOfStoryHours = 0;
                    
                    ObservableList<Integer> obListStoryID = FXCollections.observableArrayList();
                    ObservableList<Integer> obListStoryPoint = FXCollections.observableArrayList();
                    ObservableList<Integer> obListStoryHour = FXCollections.observableArrayList();

                
                    Statement st=conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from Stories where Sprint_ID = '"+selectedSprintID+"'");
                    while(rs.next())
                    {
                        sumOfStoryPoints = sumOfStoryPoints + rs.getInt("Story_Point");
                        sumOfStoryHours = sumOfStoryHours + rs.getInt("Story_Duration");
                        
                        if(rs.getString("Story_Status").equals("Complete")){
                            obListStoryID.add(rs.getInt("Story_ID"));
                            obListStoryPoint.add(rs.getInt("Story_Point"));
                            obListStoryHour.add(rs.getInt("Story_Duration"));
                        }
                    }
                    rs.close();
                    //System.out.println("jgsdsad");
                    
                    int totalNumOfTasks = 0;
                    ObservableList<Integer> obListTaskTimeLeft = FXCollections.observableArrayList();
                   
                    
                    for (int storyID: obListStoryID){
                        ResultSet rs2 = st.executeQuery("select * from Tasks where Story_ID = '"+storyID+"'");
                        while(rs2.next())
                        {
                            obListTaskTimeLeft.add(rs2.getInt("Time_Left"));
                            totalNumOfTasks++;
                        }
                        rs2.close();
                        
                    }
                    
                    LineChart<Number, Number> createGraph = startBurnDown.createSprintBurnDown(burnDownChartTitle, sumOfStoryPoints, sumOfStoryHours, obListStoryID, obListStoryPoint, obListStoryHour, obListTaskTimeLeft, totalNumOfTasks);
                    final Stage myDialog = new Stage();
                    myDialog.initModality(Modality.WINDOW_MODAL);
                    
                    Button backBtn = new Button("Go Back");
                    backBtn.setOnAction(new EventHandler<ActionEvent>(){
 
                    @Override
                    public void handle(ActionEvent arg0) {
                        myDialog.close();
                    }
               
                    });
                    
                    Scene myDialogScene = new Scene(VBoxBuilder.create()
                     .children(createGraph, backBtn)
                     .alignment(Pos.CENTER)
                     .padding(new Insets(10))
                     .build());
           
                    myDialog.setScene(myDialogScene);
                    myDialog.show();
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
        
        prjBurnDownBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //int sumOfSprintPoints = sumOfSprintPoints;
                LineChart<Number, Number> createGraphs = startBurnDown.createPrjBurnDown(sumOfSprintPoints, sumOfSprintHours, obListCompletedSprintID, obListCompletedSprintPoints,  obListCompletedSprintHours);
                
                        
                final Stage myDialog = new Stage();
                myDialog.initModality(Modality.WINDOW_MODAL);

                Button backBtn = new Button("Go Back");
                backBtn.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent arg0) {
                    myDialog.close();
                }

                });

                Scene myDialogScene = new Scene(VBoxBuilder.create()
                 .children(createGraphs, backBtn)
                 .alignment(Pos.CENTER)
                 .padding(new Insets(10))
                 .build());

                myDialog.setScene(myDialogScene);
                myDialog.show();
            }
        });
         
         
        goBackBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                mainMenu.launchMainMenu(primaryStageSprint, userID);
            }
        });
        
        root.getChildren().add(grid);
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageSprint);          
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

