var service = new Object({
    initChart : function (element, title, categories, series) {
        $(element).highcharts({
            chart: {
                type: 'areaspline'
            },
            title: {
                text: title
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                verticalAlign: 'top',
                x: 150,
                y: 100,
                floating: true,
                borderWidth: 1,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                title: {
                    text: 'Count Numbers'
                }
            },
            tooltip: {
                shared: true,
                stickyTracking: false,
                style: {
                    fontSize: "15px"
                },
                pointFormatter: function() {
                    return '<span style="color: '+ this.series.color +'">'+ this.series.name +': </span>' +
                        '<span style="text-align: right"><b>'+ this.y + '</b></span><br>' +
                        '<span style="color: '+this.series.color+'">Max: </span>' +
                        '<span style="text-align: right"><b>'+this.series.userOptions.extra[this.index]+'</b></span><br>';
                }
            },
            credits: {
                text: 'vica',
                href: 'https://golveo.cn'
            },
            plotOptions: {
                areaspline: {
                    fillOpacity: 0.5
                }
            },
            series: series
        });
    },

    request : function (url, title) {
        $.ajax({
            url: "/summary/"+url,
            async: false,
            type: "POST",
            dataType: "json",
            success: function (data) {
                if(data.total > 0){
                    var series = [];
                    data.msg.forEach(function (value, index, array) {
                        var da = [];
                        value.data.map(function (val, i, ay) {
                            if(val != null && val != "null")
                                da.push(val);
                            else
                                da.push(0);
                        });
                        var ex = [];
                        value.extra.map(function (val, i, ay) {
                            if(val != null && val != "null")
                                ex.push(val);
                            else
                                ex.push("-");
                        })
                        var obj = new Object({
                            name : value.name,
                            data : da,
                            extra : ex
                        });
                        series.push(obj);
                    });
                    service.initChart($("#container"), title, data.msg[0].categories, series);
                }else {
                    alert("暂无数据！");
                }
            }
        })
    }
});
(function(){
    var dateHolder = new Object({
        getYear : function (start, end){
            var y = [];
            for(var i = start; i <= end; i++){
                y.push(i);
            }
            return y;
        },
        getMonth : function (){
            var m = [];
            for(var i = 1; i < 13; i++){
                m.push(i);
            }
            return m;
        }
        ,
        getDay : function (year, month){
            var max;
            switch (month){
                case 1:
                case 3:
                case 5:
                case 9:
                case 8:
                case 10:
                case 12:
                    max = 31;
                    break;
                case 2:
                    if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
                        max = 29;
                    }else {
                        max = 28;
                    }
                    break;
                default:
                    max = 30;
                    break;
            }
            var d = [];
            for(var i = 1; i <= max ; i++){
                d.push(i);
            }
            return d;
        }
    });

    $(document).ready(function(){
        var select_year = $("#year");
        var select_month = $("#month");
        var select_day = $("#day");
        dateHolder.getYear(2016, new Date().getFullYear()).map(function (value, index, array) {
            select_year.append("<option value='"+value+"'>"+value+"</option>");
        });
        dateHolder.getMonth().map(function (value, index, array) {
            select_month.append("<option value='"+value+"'>"+value+"</option>");
        });
        select_year.children("option").last().attr("selected","selected");
        select_month.children("option").last().attr("selected","selected");

        var dayGenerator = function () {
            $("#day").empty();
            dateHolder.getDay(parseInt($("#year").children("option:selected").get(0).value),
                parseInt($("#month").children("option:selected").get(0).value)).map(function (value, index, array) {
                $("#day").append("<option value='"+value+"'>"+value+"</option>");
            });
        };
        dayGenerator();
        select_month.change(dayGenerator);
        select_year.change(dayGenerator);
    });
})();