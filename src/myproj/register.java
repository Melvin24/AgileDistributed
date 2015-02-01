
package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class register  {
    private static Connection conn = mySQL.ConnectDb();
    public static void launchRgst (Stage Input) { 
        startRegister(Input);
    }

    public static void startRegister(Stage primaryStageRgst) {
        primaryStageRgst.setTitle("Register");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        //observable list to store countries 
        ObservableList<String> obListCntry = FXCollections.observableArrayList();

        
        String[] locales = Locale.getISOCountries();
 
	for (String countryCode : locales) {
		Locale obj = new Locale("", countryCode);
                obListCntry.add(obj.getDisplayCountry());
	}
        
//        try{
//            Statement st=conn.createStatement();
//            ResultSet rs=st.executeQuery("select * from team");
//
//            while(rs.next()){
//                obListExistingCntry.add(rs.getString("Team_Country"));
//                obListExistingState.add(rs.getString("Team_State"));
//            }
//            st.close();
//            rs.close();
//            
//        }catch (SQLException ex) {
//            Dialogs.create()
//            .owner(primaryStageRgst)
//            .title("Error")
//            .masthead("Oops there was an Error!")
//            .message("Sorry there was a Server Error, Please restart program or contact Admin")
//            .showError();
//        }



        
        Text scenetitle = new Text("Register");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label name = new Label("Name:");
        grid.add(name, 0, 1);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label surName = new Label("Surname:");
        grid.add(surName, 0, 2);
        TextField surNameTextField = new TextField();
        grid.add(surNameTextField, 1, 2);  
        
        //email, username, password, repass
        Label eMail = new Label("E-Mail:");
        grid.add(eMail, 0, 3);
        TextField eMailNameTextField = new TextField();
        grid.add(eMailNameTextField, 1, 3); 
        
        Label country = new Label("Country:");
        grid.add(country,0, 4);
        ComboBox statusCboBox = new ComboBox(obListCntry);
        grid.add(statusCboBox, 1, 4); 
        
        Label state = new Label("State:");
        grid.add(state, 0, 5);
        TextField stateTextField = new TextField();
        grid.add(stateTextField, 1, 5); 
        
        Label userName = new Label("User Name:");
        grid.add(userName, 0, 6);
        TextField usrNameTextField = new TextField();
        grid.add(usrNameTextField, 1, 6);       
          
        Label pw = new Label("Password:");
        grid.add(pw, 0, 7);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 7);
        
        Label rePw = new Label("Re-Password:");
        grid.add(rePw, 0, 8);
        PasswordField rePwBox = new PasswordField();
        grid.add(rePwBox, 1, 8);

        Button btn = new Button("      Register     ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 9);      
        
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String getName = nameTextField.getText();
                String getSurname = surNameTextField.getText();
                String getEmail = eMailNameTextField.getText();
                String getState = stateTextField.getText();
                String getUserName = usrNameTextField.getText();
                String getPass = hashing.getMD5(pwBox.getText());//securing the passwords using hashing
                String getPass2 = hashing.getMD5(rePwBox.getText());
                System.out.println(statusCboBox.getSelectionModel().getSelectedIndex());
                
                if (!getUserName.equals("") && getPass.equals(getPass2) && !getPass.equals("") && !getPass2.equals("") && !getName.equals("") && !getSurname.equals("") && !getEmail.equals("") && !getState.equals("") && statusCboBox.getSelectionModel().getSelectedIndex() >= 0)
                {
                    try {
                        Boolean checkMethods = check(getUserName);

                        if (checkMethods.equals(false))
                        {
                            Dialogs.create()
                                .owner(primaryStageRgst)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry the User Name is already taken, Please use another")
                                .showError();
                        }else{
                            String sql = "Insert into login (name, surname, emailid, country, state, username, password) values (?, ?, ?, ?, ?, ?, ?) ";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setString(1, getName);
                            pst.setString(2, getSurname);
                            pst.setString(3, getEmail);
                            pst.setString(4, obListCntry.get(statusCboBox.getSelectionModel().getSelectedIndex()));
                            pst.setString(5, getState);
                            pst.setString(6, getUserName);
                            pst.setString(7, getPass);

                            pst.execute();  
                            
                            Dialogs.create()
                                .owner(primaryStageRgst)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully Registered")
                                .showInformation();
                            
                            login.launchGUI(primaryStageRgst);
                            pst.close();
                        } 
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageRgst)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    
                }else{
                    Dialogs.create()
                        .owner(primaryStageRgst)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry all the fields must be populated accordingly, Please try again")
                        .showError();
                }  
            }
        });
        root.getChildren().add(grid);
        
        MenuBar menuBar = new MenuBar();
        Menu menuPhase = new Menu("Tools");        
        
        MenuItem menuItemA = new MenuItem("Login");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            login.launchGUI(primaryStageRgst);
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageRgst)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {                   
                    try {
                        conn.close();                   
                    } catch (SQLException ex) {
                        Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    primaryStageRgst.close();                   
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);       
        menuBar.prefWidthProperty().bind(primaryStageRgst.widthProperty());

        root.getChildren().add(menuBar);
            
        primaryStageRgst.setScene(scene);
        primaryStageRgst.setResizable(false);
        primaryStageRgst.show();
    }
    
    private static Boolean check(String value1)//you might not want to chek if password is uniques just only username
    {
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where username='"+value1+"'");

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
