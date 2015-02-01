
package myproj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.JOptionPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;


public class mdfAccount  {
    private static Connection conn = mySQL.ConnectDb();
    public static void launchMdfAcc (Stage Input, int userID) { 
        startModify(Input, userID);
    }

    public static void startModify(Stage primaryStageMdfy, int userID) {
        primaryStageMdfy.setTitle("Modify Account");
        Group root = new Group();
        Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,100,100,100));

        Text scenetitle = new Text("Modify Account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 2);

        Label name = new Label("Name:");
        grid.add(name, 0, 3);
        TextField nameTextField = new TextField();
        //nameTextField.setEditable(false);
        grid.add(nameTextField, 1, 3);

        Label surName = new Label("Surname:");
        grid.add(surName, 0, 4);
        TextField surNameTextField = new TextField();
        //surNameTextField.setEditable(false);
        grid.add(surNameTextField, 1, 4);  

        Label eMail = new Label("E-Mail:");
        grid.add(eMail, 0, 5);
        TextField eMailNameTextField = new TextField();
        grid.add(eMailNameTextField, 1, 5); 
        
        Button mdfyPDBtn = new Button("  Modify Personal Details  ");
        HBox hbMdfyBtn = new HBox(10);
        hbMdfyBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbMdfyBtn.getChildren().add(mdfyPDBtn);
        grid.add(hbMdfyBtn, 1, 6); 
        
        Label userName = new Label("User Name:");
        grid.add(userName, 0, 8);
        TextField usrNameTextField = new TextField();
        usrNameTextField.setEditable(false);
        grid.add(usrNameTextField, 1, 8);   
        
        Label oldPw = new Label("Old-Password:");
        grid.add(oldPw, 0, 9);
        PasswordField oldPwBox = new PasswordField();
        grid.add(oldPwBox, 1, 9);
          
        Label pw = new Label("New-Password:");
        grid.add(pw, 0, 10);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 10);
        
        Label rePw = new Label("Re-Password:");
        grid.add(rePw, 0, 11);
        PasswordField rePwBox = new PasswordField();
        grid.add(rePwBox, 1, 11);

        Button cancelBtn = new Button("Go Back");
        HBox hbCancelBtn = new HBox(10);
        hbCancelBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbCancelBtn.getChildren().add(cancelBtn);
        grid.add(hbCancelBtn, 1, 13);  
        
        Button mdfyBtn = new Button("    Modify Login Details    ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(mdfyBtn);
        grid.add(hbBtn, 1, 12);      
        
        
        //Populating required fields
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where User_ID='"+userID+"'");

            while(rs.next()){
                nameTextField.setText(rs.getString("name"));
                surNameTextField.setText(rs.getString("surname"));
                eMailNameTextField.setText(rs.getString("emailid"));
                usrNameTextField.setText(rs.getString("username"));
            }
            st.close();
            rs.close();
        }catch(Exception e){
            Dialogs.create()
                    .owner(primaryStageMdfy)
                    .title("Error")
                    .masthead("Oops there was an Error!")
                    .message("Sorry there was a Server Error, Please restart program or contact Admin")
                    .showError();
        } 
        
        //when cancel button is executed-->Cancel it
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                    mainMenu.launchMainMenu(primaryStageMdfy, userID);               
            }
        });
        
        mdfyPDBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try{
                    String name = nameTextField.getText();
                    String surName = surNameTextField.getText();
                    String eMail = eMailNameTextField.getText();
                
                    if(name.isEmpty() || surName.isEmpty() || eMail.isEmpty())
                    {
                        Dialogs.create()
                            .owner(primaryStageMdfy)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry some of the Details are not Populated, Please try again.")
                            .showError();
                    }else{
                        String sql = "update login set name =?, surname =?, emailid =? where User_ID = ?";
                        PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1, name);
                        pst.setString(2, surName);
                        pst.setString(3, eMail);
                        pst.setInt(4, userID);

                        pst.execute();
                        Dialogs.create()
                            .owner(primaryStageMdfy)
                            .title("Information")
                            .masthead("Good news!")
                            .message("Successfully Modified your Details")
                            .showInformation();
                        pst.close();
                    }
                }catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageMdfy)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                             
            }
        });
        
        
        //Modify users password
        mdfyBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try { 
                    String txtUsrName = usrNameTextField.getText();
                    String cfrOldPassword = hashing.getMD5(oldPwBox.getText());
                    String newPassword = hashing.getMD5(pwBox.getText());
                    String rePassword = hashing.getMD5(rePwBox.getText());
                
                    if(!cfrOldPassword.isEmpty() && newPassword.equals(rePassword) && !newPassword.isEmpty() && !rePassword.isEmpty()){ 
                        //check if old password is correct 
                        Boolean checkMethods = check(txtUsrName, cfrOldPassword);
                        //if it is not show error
                        if (checkMethods.equals(true))
                        {
                            Dialogs.create()
                                .owner(primaryStageMdfy)
                                .title("Error")
                                .masthead("Oops there was an Error!")
                                .message("Sorry your old password is Incorrect, Please try again.")
                                .showError();
                        }else{
                            
                            String sql = "update login set password =? where User_ID = ?";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setString(1, newPassword);
                            pst.setInt(2, userID);

                            pst.execute();
                            Dialogs.create()
                                .owner(primaryStageMdfy)
                                .title("Information")
                                .masthead("Good news!")
                                .message("Successfully Changed your Password")
                                .showInformation();
              
                            pst.close();
                        } 
                    }else{
                        Dialogs.create()
                            .owner(primaryStageMdfy)
                            .title("Error")
                            .masthead("Oops there was an Error!")
                            .message("Sorry all the fields must be populated accordingly, Please try again")
                            .showError();
                    }
                } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageMdfy)
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
        
        MenuItem menuItemA = new MenuItem("Log-Out");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                login.launchGUI(primaryStageMdfy);          
        }
        });
        
        MenuItem menuItemB = new MenuItem("Exit");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {     
        @Override public void handle(ActionEvent e) {
            Action response = Dialogs.create()
                .owner(primaryStageMdfy)
                .title("Confirm")
                .masthead("Are you sure?")
                .message("Do you want to Exit?")
                .showConfirm();

                if (response == Dialog.ACTION_YES) {   
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Dialogs.create()
                        .owner(primaryStageMdfy)
                        .title("Error")
                        .masthead("Oops there was an Error!")
                        .message("Sorry there was a Server Error, Please restart program or contact Admin")
                        .showError();
                    }
                    primaryStageMdfy.close();
                }   
        }
        });
        
        menuPhase.getItems().add(menuItemA);
        menuPhase.getItems().add(menuItemB);
        menuBar.getMenus().add(menuPhase);
        menuBar.prefWidthProperty().bind(primaryStageMdfy.widthProperty());
        
        root.getChildren().add(menuBar);
            
        primaryStageMdfy.setScene(scene);
        primaryStageMdfy.setResizable(false);
        primaryStageMdfy.show();
    }
    
    //cheking method
    private static Boolean check(String value1, String value2)
        {
            try{
               
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery("select * from login where username='"+value1+"' and password='"+value2+"'");

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