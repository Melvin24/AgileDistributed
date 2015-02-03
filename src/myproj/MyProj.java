/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproj;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Melvin
 */
public class MyProj extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println(args);
        launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //String hello = hashing.getMD5("password");
        //String[] args;
        //MD5.main(args);
        //login.launchGUI(primaryStage);
        //register.launchRgst(primaryStage);
        mainMenu.launchMainMenu(primaryStage, 13);
        //mdfAccount.launchMdfAcc(primaryStage, 10);
        //String[] array = null;
        //createNewProject.userInfo(array, 2, 2);
        //invite.launchInvite(primaryStage, 13);
        //createTeam.launchacceptInvite(primaryStage, 8, 31);
        //mdfProject.launchMdfPrj(primaryStage, 32, 10);
        //mdfTeam.launchMdfTem(primaryStage, 31, 8, 4);
        //createNewProject.launchCreateProject(primaryStage, 15);
//        int userPass = hashing.hashIt("admin");
//        System.out.println(userPass);
//        String userPass2 = hashing.retrieveIt(userPass);
//        System.out.println(userPass2);
    }
    
}
