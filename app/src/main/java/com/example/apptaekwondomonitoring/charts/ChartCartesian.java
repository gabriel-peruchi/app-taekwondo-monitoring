package com.example.apptaekwondomonitoring.charts;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

public class ChartCartesian {

    private Cartesian cartesian;

    public ChartCartesian() {
    }

    public ChartCartesian(Cartesian cartesian) {
        this.cartesian = cartesian;
    }

    public void createDefaultSettings(String title) {
        cartesian = AnyChart.line();

        cartesian.title(title);
        cartesian.animation(true);
        //cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.xAxis(0).labels().padding(0d, 0d, 0d, 0d);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
    }

    public void setLegends(String legendY, String legendX) {
        cartesian.yAxis(0).title(legendY);
        cartesian.xAxis(0).title(legendX);
    }

    public void setData(List<ValueDataEntry> valueDataEntries) {

        if (valueDataEntries.size() == 0) {
            valueDataEntries.add(new AccelerationData(0, 0, 0, 0));
        }

        List<DataEntry> dataEntries = new ArrayList<DataEntry>(valueDataEntries);

        cartesian.removeAllSeries();

        Set set = Set.instantiate();
        set.data(dataEntries);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }"); // Line X
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }"); // Line Y
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }"); // Line Z

        Line series1 = cartesian.line(series1Mapping);
        series1.color("#008000");
        series1.name("X");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.color("#003c80");
        series2.name("Y");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.color("#ff4500");
        series3.name("Z");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
    }

    public void setView(AnyChartView anyChartView) {
        anyChartView.setChart(cartesian);
    }

    public void clearData() {
        setData(new ArrayList<ValueDataEntry>());
    }
}
