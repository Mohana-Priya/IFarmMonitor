var filerArray=[];

function clearContentPane(){
	var contentPane = document.getElementById("contentPane");
	contentPane.innerHTML = "";
}

function startup() {
	loadFilerList();
	loadCategoryList();	
}

function init(url) {
	var req;
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	req.open("POST", url, true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
	return req;
}

function loadFilerList() {	
	var req = init("/IFarmMonitor/getFilerList.htm");
	req.onreadystatechange = function(){populateFilerArray(req);}
	req.send();
}

function populateFilerArray(req){
	if (req.readyState == 4) {				
		if (req.status == 200) {
			for (var i=0; i<req.responseXML.getElementsByTagName("list").length;i++) {
				var listObj = req.responseXML.getElementsByTagName("list")[i];
				filerArray[i]=listObj.childNodes[0].nodeValue;
			}
			
			submitForm();
		}
	}
}

function loadCategoryList() {
	var req = init("/IFarmMonitor/getCategoryList.htm");	
	req.onreadystatechange = function(){listHandler("categoryDropDown",document.getElementById("categoryList"),req);}
	req.send();	
}

function listHandler(name,list,req) {	
	
	if (req.readyState == 4) {				
		if (req.status == 200) {
			list.innerHTML = "";
									
			var temp = "<select name=\""+name+"\">";
			for (var i=0; i<req.responseXML.getElementsByTagName("list").length;i++) {
				var listObj = req.responseXML.getElementsByTagName("list")[i];
				temp = temp + "<option value=\"" + listObj.childNodes[0].nodeValue +"\">" + listObj.childNodes[0].nodeValue + "</option>";
			}

			temp = temp + "</select>";			
			list.innerHTML = temp;			
		}
	}
}

function submitForm(){
	clearContentPane();
	if(document.forms[0].categoryDropDown.value=="Space"){
		var contentPane = document.getElementById("contentPane");				
		var temp = "<div id=\"";
		for(var i=0;i<filerArray.length;i++){			
			temp += filerArray[i]+"\" style=\"min-width: 310px; min-height: 400px; margin: 0 auto\"><center><b>Loading ...</b></center></div>";
			if(i != filerArray.length-1){
				temp += "<div id=\"";
			}
		}
		contentPane.innerHTML = temp;		
		
		for(var i=0;i<filerArray.length;i++){			
			var req = init("/IFarmMonitor/getSpaceUtilisations.htm");			
			req.onreadystatechange = displayGraph;
			req.send("filer="+filerArray[i]);
		}			
	} else if(document.forms[0].categoryDropDown.value=="StuckJobs"){
		showStuckJobs();
	} else if(document.forms[0].categoryDropDown.value=="Terminated Jobs"){
		showKilledJobs();	
	} else if(document.forms[0].categoryDropDown.value=="Resources Usage"){
		showResourcesUsage();	
	}
}

function displayGraph(){
	var req = this;
	if (req.readyState == 4) {				
		if (req.status == 200) {			
			var data = [];
			var filer = req.responseXML.getElementsByTagName("filer")[0].childNodes[0].nodeValue;			
			var length = req.responseXML.getElementsByTagName("list").length;
			for (var i=0; i<length; i++) {
				data[i] = parseFloat(req.responseXML.getElementsByTagName("list")[i].childNodes[0].nodeValue);
			}					
			drawGraph(data,filer,req.responseXML.getElementsByTagName("startTime")[0].childNodes[0].nodeValue);
		}
	}
}

function drawGraph(data,filer,startTime) {
     var startTimeArray = startTime.split(',');
     $(''+'#'+filer).highcharts({
	    chart: {
	        zoomType: 'x'
	    },
	    title: {
	        text: 'Space utilisations for '+filer
	    },
	    subtitle: {
	        text: document.ontouchstart === undefined ?
	            'Click and drag in the plot area to zoom in' :
	            'Pinch the chart to zoom in'
	    },
	    xAxis: {
	        type: 'datetime',
	        minRange: 1 * 24 * 3600000 // 1 day
	    },
	    yAxis: {
	        title: {
	            text: 'Space usage percentage'
	        }
	    },
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	        area: {
	            fillColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                stops: [
	                    [0, Highcharts.getOptions().colors[0]],
	                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                ]
	            },
	            marker: {
	                radius: 2
	            },
	            lineWidth: 1,
	            states: {
	                hover: {
	                    lineWidth: 1
	                }
	            },
	            threshold: null
	        }
	    },
	
	    series: [{
	        type: 'line',
	        name: 'space usage %',
	        pointInterval:  1800 * 1000,
	        pointStart: Date.UTC(startTimeArray[0],startTimeArray[1],startTimeArray[2],startTimeArray[3],startTimeArray[4],startTimeArray[5]),
	        data: data
	    }]
	});
}