/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproj;

import java.sql.Date;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Melvin
 */
public class startBurnDown {
//    private startBurnDown(ObservableList<Integer> observableListTimeLeft, ObservableList<Date> observableListDateStart, ObservableList<Date> observableListDateComplete, int totalTime, int sprintDuration, int totalNumOfTask){
//        createGraph(observableListTimeLeft, observableListDateStart, observableListDateComplete, totalTime, sprintDuration, totalNumOfTask);
//    }
    
    public static LineChart<Number, Number> createGraph(String sprintTitle, ObservableList<Integer> observableListTimeLeft, ObservableList<Date> observableListDateStart, ObservableList<Date> observableListDateComplete, int totalTime, int sprintDuration, int totalNumOfTask){
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days"); 
        yAxis.setLabel("Story Points"); 
        //xAxis.set
        
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(sprintTitle + " Burndown");
        
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
        int ttlStryPnt = totalTime;

        //1: For each items in the observableListDateComplete calculate the number of days between startDate and the i^th dateCompleted = A
        for(Date sqlDate: observableListDateComplete){
            //getting the start Date from observableListDateStart and converting startDate to util date
            java.util.Date startDate = new java.util.Date(observableListDateStart.get(count).getTime());

            //converting completed date to util date
            java.util.Date completedDate = new java.util.Date(sqlDate.getTime());

            //get the differance in date
            curntDiffInDate = diffInDate.DateDiff(startDate, completedDate);
            //limit so that it only view up to less or equal to the sprint limit
            if(totalDays <= sprintDuration){

                totalDays = totalDays + curntDiffInDate;
                totalNumOfTask--;
                ttlStryPnt = ttlStryPnt - (observableListTimeLeft.get(count));         
                series2.getData().add(new XYChart.Data(totalDays, ttlStryPnt));
                
                series3.getData().add(new XYChart.Data(totalDays, totalNumOfTask));  
            }
            count++;
        }
            
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        
        return lineChart;
    }
}
