
package myproj;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.Locale;

/**
 *
 * @author Melvin
 */
public class login  {
    private static final Connection conn = mySQL.ConnectDb();
    private static int userID = 0;
    public static void launchGUI (Stage input) { 
        userID = 0;
        //System.out.println("loginForm");
        startLogin(input);
    }
      
    public static void startLogin(Stage primaryStage) {
        primaryStage.setTitle("Login");
        Group root = new Group();
        
        Scene scene = new Scene(root, 550, 500);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(150, 250, 250, 150));
        grid.setId("grid");
        
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        //GridPane.
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 3);      

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String usrName = userTextField.getText();
                String usrPass = hashing.getMD5(pwBox.getText());
                //System.out.println("This is the hashcode: "+ usrPass);
                
                try{
                        Statement st=conn.createStatement();
                        //System.out.println("This is the non-hash: "+ hashing.retrieveIt(usrPass));
                        ResultSet rs=st.executeQuery("select * from login where username='"+usrName+"' and password='"+usrPass+"' and Account_Status = 'Active'");
                        
                        int count=0;
                        while(rs.next()){
                            userID = rs.getInt("User_ID");
                            count++;
                        }                      

                        if(count>0){
                            Dialogs.create()
                                .owner(primaryStage)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully logged in.")
                                .showInformation();
                            st.close();
                            mainMenu.launchMainMenu(primaryStage, userID);
                         }
                        else{
                            Dialogs.create()
                                .owner(primaryStage)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry incorrect User Name or Password, Please try again")
                                .showError();
                        }
                        
                    }catch(Exception ex){
                        Dialogs.create()
                                .owner(primaryStage)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry server Error, Please restart program")
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
            register.launchRgst(primaryStage);
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStage)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {
                    primaryStage.close();
                }         
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);       
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        root.getChildren().add(menuBar);
           
        scene.getStylesheets().add
 (login.class.getResource("Login.css").toExternalForm());
        
        primaryStage.setScene(scene);
        
//        scene.getStylesheets().add
// (login.class.getResource("Login.css").toExternalForm());
//        
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
