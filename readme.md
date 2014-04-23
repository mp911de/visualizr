visualizr
=========================
visualizr is a tiny data visualization framework. It uses [amcharts](http://http://www.amcharts.com/) and an internal HTTP API to create
charts based on time data.

*Features*

* Linecharts for time data (single and multiple graphs within one chart)
* Multiple charts per data source
* Support of multiple data sources (a data source can provide multiple charts)
* Full Ajax support
* Automatic refresh
* Easy integration

Integration
--------------

There are 6 steps, you've to do in order to integrate visualizr:

1. Implement a `DataSourceProvider` (implements `biz.paluch.visualizr.spi.DataSourceProvider`)<br/>
The `DataSourceProvider` provides a list of DataSources. Every `ChartDataSource` has an id and a name.

2. Implement a `ChartProvider` (ìmplements `biz.paluch.visualizr.spi.ChartProvider`)br/>
The `ChartProvider` provides two things: The `ChartDescriptor`s and `ChartData`.

3. Implement your `ViewResource` (extnds `biz.paluch.visualizr.AbstractViewResource`)<br/>
You need to implement just the `getDataSourceProvider` method. Since I'm not in charge of dependency injection, you're free on how you get the `DataSourceProvider`

4. Implement your `ChartResource` (extnds `biz.paluch.visualizr.AbstractChartResource`)<br/>
You need to implement just the `getChartProvider` method. Since I'm not in charge of dependency injection, you're free on how you get the `ChartProvider`

5. Like 4, implement your `ChartDataResource` (extnds `biz.paluch.visualizr.AbstractChartDataResource`)<br/>
You need to implement just the `getChartProvider` method. Since I'm not in charge of dependency injection, you're free on how you get the `ChartProvider`

6. Stick all together and run it! Some RESTEasy versions try to create instances of abstract resource classes,
therefore you should disable autoscan and create an `javax.ws.rs.core.Application` that specifies the 3 resource classes.


Running a demo
--------------
Wanna try it? You need just maven for it:

    mvn clean install
    cd visualizr
    mvn jetty:run


Versions/Dependencies
--------------
This project is built against following dependencies/versions:

* Apache Velocity 1.7
* RESTEasy/RESTEasy Jackson 2.3.6
* Jackson 1.9.9 (ASL)
* Google Guava 16.0.1
* Java Servlet API 3 (will work with 2.5 as well)

HTML/Javascript Frameworks
--------------
visualizr uses JQuery 1.11.0, Bootstrap 3.1.1 and amcharts 3. These libraries are bundled within the module `local-hosted-js-frameworks`
which is
optional. In case the bundled resources are not available, it will switch to the hosted version of JQuery (via googleapis),
Bootstrap (netdna) and amcharts (amcharts.com). The compressed version of `local-hosted-js-frameworks` eats some 1MB.