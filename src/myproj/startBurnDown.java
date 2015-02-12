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
    
    public static LineChart<Number, Number> createGraph( String burnDownChartTitle, int sumOfStoryPoints, int sumOfStoryHours, ObservableList<Integer> obListStoryID, ObservableList<Integer> obListStoryPoint,  ObservableList<Integer> obListStoryHour, ObservableList<Integer> obListTaskTimeLeft, int totalNumOfTasks){
        
       
        
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hours"); 
        yAxis.setLabel("Story Points"); 
        //xAxis.set
        
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(burnDownChartTitle + " Burndown");
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Ideal Burndown");
        
        series.getData().add(new XYChart.Data(sumOfStoryHours, 0));
        series.getData().add(new XYChart.Data(0, sumOfStoryPoints));
        
        //a new series for story points projection
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Actual Burndown  ");
        series2.getData().add(new XYChart.Data(0, sumOfStoryPoints));
        
        //a new series for num of tasks projection
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Completed Tasks");
        series3.getData().add(new XYChart.Data(0, totalNumOfTasks));
        
        int count = 0;
        int initialStoryPoint = sumOfStoryPoints;
        int finalStoryPoint = 0;
        
        int initialStoryHours = 0;
        int finalStoryHours = 0;
              
                
        
        for(int storyID: obListStoryID){
            finalStoryHours = obListStoryHour.get(count);
            initialStoryHours = initialStoryHours + finalStoryHours;
            series2.getData().add(new XYChart.Data(initialStoryHours, initialStoryPoint));
            
            finalStoryPoint = obListStoryPoint.get(count);
            initialStoryPoint = initialStoryPoint - finalStoryPoint;
            series2.getData().add(new XYChart.Data(initialStoryHours, initialStoryPoint));
            
            count++;
        }
        
        int taskTimeCount = 0;
        for (int taskTime: obListTaskTimeLeft){
            taskTimeCount = taskTimeCount + taskTime;
            totalNumOfTasks--;
            series3.getData().add(new XYChart.Data(taskTimeCount, totalNumOfTasks));
            
        }
        
        
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        
        return lineChart;
    }
}
