var visualizr = (function () {
// Good: the name is local to this module
    var visualizr = {};
    var chartInstances = [];
    var chartDescriptors = [];
    var from = '';
    var to = '';
    var datsourceId = '';
    var autoupdate = false;
    var chartheight = 500;
    var updated = 0;
    var updateIntervall = 30000;

    visualizr.initialize = function () {


        var autoupdateParam = getQueryStringParams("autoupdate");
        var datasourceParam = getQueryStringParams("datasource");
        var chartheightParam = getQueryStringParams("chartheight");

        $('a.datasource').each(
            function (index) {
                var element = $(this);
                var ds = element.data('datasource');
                if (ds) {
                    if (datasourceParam && ds == datasourceParam) {
                        visualizr.selectDatasource(ds, element.text());
                    }

                    element.click(function () {
                        visualizr.selectDatasource(ds, element.text())
                    });
                }
            }
        );

        if (chartheightParam && chartheightParam != '') {
            chartheight = chartheightParam;
        }

        if (autoupdateParam && autoupdateParam == 'true') {
            autoupdate = true;
            $("#autoupdate").prop({checked: true})
        }

        $("#fromTimestamp").change(function () {
            visualizr.timestampsChanged();
        });

        $("#toTimestamp").change(function () {
            visualizr.timestampsChanged();
        });

        $("a.dateselection").click(function () {
            visualizr.timestampsChanged();
        });

        $("#autoupdate").click(function () {
            visualizr.autoupdateChanged();
        });

        $("#refresh").click(function () {
            visualizr.updateData();
        });

        visualizr.timestampsChanged();

        window.setInterval(function () {
            if (autoupdate) {
                if (new Date().getTime() >= updated + updateIntervall) {
                    visualizr.updateData();
                }
            }
        }, 1000)
    }


    visualizr.selectDatasource = function (id, text) {
        $("#datasource").val(id);
        $("#datasourceText").val(text);
        datsourceId = id;

        for (var i = 0; i < chartInstances.length; i++) {
            if (chartInstances[i]) {
                chartInstances[i].clear();
            }
        }

        $(".chartdiv").remove();

        chartInstances = [];
        chartDescriptors = [];

        $.getJSON("../api/" + id + "/charts", function (data) {

            for (var i = 0; i < data.length; i++) {
                var chartDescriptor = data[i];
                chartDescriptors[i] = chartDescriptor;
                console.log("Loading chart " + chartDescriptor.id);
                chartInstances[i] = loadChart(chartDescriptor);
            }

            visualizr.updateData();
        });

        function loadChart(chartDescriptor) {


            if ($("#" + chartDescriptor.id).length == 0) {
                $("#chartContainer").append('<div id="' + chartDescriptor.id + '" class="col-lg-6 chart chartdiv" style="height: ' + chartheight + 'px"></div>');
            }

            // SERIAL CHART
            chart = new AmCharts.AmSerialChart();
            chart.pathToImages = "amcharts/images/";
            chart.dataProvider = [];
            chart.dataDateFormat = "YYYY-MM-DD JJ:NN";
            chart.precision = chartDescriptor.precision;
            chart.balloon.cornerRadius = 6;
            chart.categoryField = chartDescriptor.categoryField;
            if (chartDescriptor.titleField) {
                chart.titleField = chartDescriptor.titleField;
            }

            chart.addTitle(chartDescriptor.title, 14, "#000000", 1, true);

            // AXES
            // category
            var categoryAxis = chart.categoryAxis;
            categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "mm";
            categoryAxis.gridAlpha = 0.1;
            categoryAxis.dateFormats = [
                {period: 'fff', format: 'JJ:NN:SS'},
                {period: 'ss', format: 'JJ:NN:SS'},
                {period: 'mm', format: 'JJ:NN'},
                {period: 'hh', format: 'JJ:00'},
                {period: 'DD', format: 'MMM DD HH:00'},
                {period: 'WW', format: 'MMM DD'},
                {period: 'MM', format: 'MMM'},
                {period: 'YYYY', format: 'YYYY'}
            ]
            categoryAxis.minorGridAlpha = 0.1;
            categoryAxis.axisAlpha = 0;
            categoryAxis.minorGridEnabled = true;
            categoryAxis.inside = true;
            categoryAxis.axisColor = "#DADADA";
            categoryAxis.dashLength = 1;
            categoryAxis.title = chartDescriptor.xAxisTitle;

            // value
            var valueAxis = new AmCharts.ValueAxis();
            valueAxis.axisAlpha = 0;
            valueAxis.title = chartDescriptor.yAxisTitle;
            chart.addValueAxis(valueAxis);

            for (var i = 0; i < chartDescriptor.graphs.length; i++) {
                var graphDescriptor = chartDescriptor.graphs[i];

                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.valueField = graphDescriptor.id;
                graph.title = graphDescriptor.name;
                /*graph.bullet = "none";
                 graph.bulletSize = 0;
                 graph.bulletBorderColor = "#FFFFFF";
                 graph.bulletBorderThickness = 2;
                 graph.bulletBorderAlpha = 1;*/
                graph.lineThickness = 2;
                graph.minDistance = 5;
                graph.balloonText = "";
                console.log("Adding graph " + graphDescriptor.id + " for chart " + chartDescriptor.id);
                chart.addGraph(graph);
            }


            // CURSOR
            var chartCursor = new AmCharts.ChartCursor();
            chartCursor.cursorAlpha = 0;
            chartCursor.cursorPosition = "mouse";
            chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN";
            chartCursor.graphBulletSize = 2;
            chartCursor.categoryBalloonEnabled = false;
            chart.addChartCursor(chartCursor);

            // SCROLLBAR
            var chartScrollbar = new AmCharts.ChartScrollbar();
            chart.addChartScrollbar(chartScrollbar);

            // LEGEND
            legend = new AmCharts.AmLegend();
            legend.align = "left";
            legend.markerType = "square";
            legend.markerSize = 8;
            legend.maxColumns = 1;
            legend.valueAlign = "right";
            legend.valueWidth = 200;
            legend.verticalGap = 5;
            legend.periodValueText = "avg [[value.average]]/min [[value.low]]/max [[value.high]]";
            //chart.balloonText = "[[title]]";
            chart.addLegend(legend);

            // WRITE
            chart.write(chartDescriptor.id);
            return chart;
        };
    };

    visualizr.updateData = function () {

        console.log("Updating data");
        updated = new Date().getTime();
        if (chartDescriptors.length > 0) {
            var chartIds = "";
            var timezoneId = getTimezoneId();
            for (var i = 0; i < chartDescriptors.length; i++) {
                chartIds += "&chart=" + chartDescriptors[i].id;
            }

            var time = "";

            if (from != '') {
                time += "&from=" + from;
            }

            if (to != '') {
                time += "&to=" + to;
            }

            $.getJSON("../api/" + datsourceId + "/data?tz=" + timezoneId + chartIds + time, function (data) {

                for (var i = 0; i < chartDescriptors.length; i++) {

                    chartInstances[i].dataProvider = data[chartDescriptors[i].id];
                    chartInstances[i].validateData();
                }
            });

            $("#lastupdatedtext").text("Updated: " + AmCharts.formatDate(new Date(), "YYYY-MM-DD JJ:NN:SS"));
        }
    };


    visualizr.timestampsChanged = function () {
        if ($("#fromTimestamp").length == 1) {
            from = $("#fromTimestamp").val()
        }

        if ($("#toTimestamp").length == 1) {
            to = $("#toTimestamp").val()
        }
        visualizr.updateData();
    };

    visualizr.autoupdateChanged = function () {
        autoupdate = $("#autoupdate").is(':checked');
    };


    function getTimezoneId() {
        var offset = new Date().getTimezoneOffset() * -1;
        var hours = Math.floor(offset / 60);
        var minutes = offset - (hours * 60);

        if (minutes < 10) {
            minutes = '0' + minutes;
        }

        if (Math.abs(hours) < 10) {
            hours = '0' + hours;
        }

        var result = "GMT";
        if (hours > 0) {
            result += "+" + hours;
        }
        else {
            result += hours
        }

        result += ":" + minutes;
        return result;
    }


// Good: only exporting the public interface,
// internals can be refactored without worrying
    return visualizr;


})
().initialize();

function getQueryStringParams(param) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == param) {
            return sParameterName[1];
        }
    }
}
