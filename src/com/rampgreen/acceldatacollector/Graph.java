package com.rampgreen.acceldatacollector;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class Graph {
    private Context context;
    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer renderer;
    int greater;
    int mChartColor;
	private int mAxisColor;
    public static boolean ClickEnabled = true;
    public Graph(Context context) {
        this.context = context;
        mChartColor = context.getResources().getColor(R.color.bg_chart);
        mAxisColor= context.getResources().getColor(R.color.gray_heavy);
    }

    public void initData(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z){      
        XYSeries seriesX = new XYSeries("X");
        for(int i =0 ; i< x.size();i++){
            seriesX.add (i,x.get(i));
        }
        XYSeries seriesY = new XYSeries("Y");
        for(int i =0 ; i< y.size();i++){
            seriesY.add( i,y.get(i));
        }
        XYSeries seriesZ = new XYSeries("Z");
        for(int i =0 ; i< z.size();i++){
            seriesZ.add( i,z.get(i));
        }
        greater = x.size();
        if(y.size() > greater){
            greater = y.size();
        }else if(z.size() > greater){
            greater = z.size();
        }
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesX);
        dataset.addSeries(seriesY);
        dataset.addSeries(seriesZ);
        renderer = new XYMultipleSeriesRenderer();
    }
    public void setProperties(){

        //renderer.setClickEnabled(ClickEnabled);
        renderer.setBackgroundColor(mChartColor);
        renderer.setMarginsColor(mChartColor);
//        renderer.setFitLegend(true);

        renderer.setApplyBackgroundColor(true);
        if(greater < 100){
            renderer.setXAxisMax(100);
        }else{
            renderer.setXAxisMin(greater-100);
            renderer.setXAxisMax(greater);
        }
        
        renderer.setAxesColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setChartTitle("Accelerometer Data");
		
        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        renderer1.setColor(Color.RED);
        renderer.addSeriesRenderer(renderer1);
        XYSeriesRenderer renderer2 = new XYSeriesRenderer();
        renderer2.setColor(Color.GREEN);
        renderer.addSeriesRenderer(renderer2);
        XYSeriesRenderer renderer3 = new XYSeriesRenderer();
        renderer3.setColor(Color.BLUE);
        renderer.addSeriesRenderer(renderer3);
    }


    public GraphicalView getGraph(){    
        return ChartFactory.getLineChartView(context, dataset, renderer);
    }
}
