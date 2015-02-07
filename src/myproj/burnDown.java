/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproj;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Melvin
 */
public class burnDown {
//    private static final ObservableList<String> observableListTimeLeft = FXCollections.observableArrayList();
//    private static final ObservableList<String> observableListDateComplete = FXCollections.observableArrayList();
    //int totalTime = 0;
    public static void launchBurnDown (Stage Input, ObservableList<Integer> observableListTimeLeft, ObservableList<Date> observableListDateStart, ObservableList<Date> observableListDateComplete, int totalTime, int sprintDuration, int totalNumOfTask ) {
        startBurnDown(Input, observableListTimeLeft, observableListDateStart, observableListDateComplete, totalTime, sprintDuration, totalNumOfTask);
    }
    
     private static void startBurnDown(Stage primaryStageBurnDown, ObservableList<Integer> observableListTimeLeft, ObservableList<Date> observableListDateStart, ObservableList<Date> observableListDateComplete, int totalTime, int sprintDuration, int totalNumOfTask ) {
        primaryStageBurnDown.setTitle("Burndown Chart");
        Group root = new Group();
        //Scene scene = new Scene(root, 550, 500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(150, 150, 150, 150));
        
        
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days"); 
        yAxis.setLabel("Story Points"); 
        //xAxis.set
        
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Sprint Burndown, 2010");
                                
        XYChart.Series series = new XYChart.Series();
        series.setName("Ideal Burndown");
        
        series.getData().add(new XYChart.Data(sprintDuration, 0));
        series.getData().add(new XYChart.Data(0, totalTime));
        
        //a new series for story points projection
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Actual Burndown  ");
        series2.getData().add(new XYChart.Data(0, totalTime));
        
        //a new series for num of tasks projection
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Completed Tasks");
        series3.getData().add(new XYChart.Data(0, totalNumOfTask));

        int count = 0;
        int curntDiffInDate = 0;
        int totalDays = 0;
        int ttlNumOfTask = 0;

        int ttlStryPnt = totalTime;

        //1: For each items in the observableListDateComplete calculate the number of days between startDate and the i^th dateCompleted = A
        for(Date sqlDate: observableListDateComplete){
            //getting the start Date from observableListDateStart and converting startDate to util date
            java.util.Date startDate = new java.util.Date(observableListDateStart.get(count).getTime());

            //converting completed date to util date
            java.util.Date completedDate = new java.util.Date(sqlDate.getTime());

            //converting to localDate
            //LocalDate dates = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Start Date: " + startDate);
            System.out.println("Completed Date: " + completedDate);

            //get the differance in date
            curntDiffInDate = diffInDate.DateDiff(startDate, completedDate);
            //limit so that it only view up to less or equal to the sprint limit
            if(totalDays <= sprintDuration){

                totalDays = totalDays + curntDiffInDate;
                totalNumOfTask--;
                System.out.println("totalDays So FAR: " + totalDays);
                System.out.println(" ");
                ttlStryPnt = ttlStryPnt - (observableListTimeLeft.get(count));
                
                series2.getData().add(new XYChart.Data(totalDays, ttlStryPnt));
                
                series3.getData().add(new XYChart.Data(totalDays, totalNumOfTask));
                
            }
            count++;
        }
            
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        Scene scene  = new Scene(lineChart,800,600);
        
       
        primaryStageBurnDown.setScene(scene);
        primaryStageBurnDown.show();
     }
    
}

//        int count = 0;
//        int prvDiffInDate = 0;
//        int curntDiffInDate = 0;
//        int diffDate = 0;
//        int changeInStry = 0;
//        int ttlStryPnt = totalTime;
//        //1: For each items in the observableListDateComplete calculate the number of days between startDate and the i^th dateCompleted = A
//        for(Date sqlDate: observableListDateComplete){
//            //converting to util date
//            java.util.Date completedDate = new java.util.Date(sqlDate.getTime());
//            //converting to localDate
//            //LocalDate dates = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            System.out.println("Start Date: " + startDate);
//            System.out.println("Completed Date: " + completedDate);
//            //get the differance in date
//            
//            
//            curntDiffInDate = diffInDate.DateDiff(startDate, completedDate);
//            
//            if(curntDiffInDate < prvDiffInDate){
//                prvDiffInDate = prvDiffInDate + curntDiffInDate;
//            }else if( curntDiffInDate ==  prvDiffInDate){
//                prvDiffInDate = prvDiffInDate + 1;
//            }else{
//                diffDate = curntDiffInDate - prvDiffInDate;
//                prvDiffInDate = prvDiffInDate + diffDate;
//            }
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
////            curntDiffInDate = diffInDate.DateDiff(startDate, completedDate);
////            System.out.println("curntDiffInDate: " + curntDiffInDate);
////            
////            if (curntDiffInDate < prvDiffInDate){
////                //diffDate = prvDiffInDate - curntDiffInDate; 
////                prvDiffInDate = prvDiffInDate + curntDiffInDate;
////            }else{
////                diffDate = curntDiffInDate - prvDiffInDate;
////                prvDiffInDate = prvDiffInDate + diffDate;
////            }
////            System.out.println("diffDate: " + diffDate);
////                
////            prvDiffInDate = prvDiffInDate + diffDate;
////            System.out.println("Differance in Date: " + prvDiffInDate);
////            System.out.println("  ");
//            ttlStryPnt = ttlStryPnt - (observableListTimeLeft.get(count));
//            //System.out.println("Story Point: " + ttlStryPnt);
//            series2.getData().add(new XYChart.Data(prvDiffInDate, ttlStryPnt));
//            count++;
//        }
//        //2: Plot a point where Y=A and X = observableListTimeLeft at this index.
//        
        
        //grid.add(lineChart, 1, 1);