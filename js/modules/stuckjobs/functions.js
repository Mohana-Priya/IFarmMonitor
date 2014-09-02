Ext.onReady(function () {
	Ext.QuickTips.init();
});

function showStuckJobs(){
	stuckjobsStore.load();
	Ext.create('Ext.grid.Panel', {
	    title: 'Standalone Jobs',
	    margin: '0 0 0 0',
	    store: stuckjobsStore,
	    columns: [	        
	        { header: 'Label', dataIndex: 'labelId', flex: 4},
	        { header: 'Name', dataIndex: 'name', flex: 2},	        
	        { header: 'Waiting Time', dataIndex: 'stuckTime', flex: 2,renderer:function(value){return parseFloat(value).toFixed(5)}},
	        { header: 'Owner', dataIndex: 'owner', flex: 2},
	        { header: 'ID', dataIndex: 'Id', flex: 2 },
	        {
				xtype:'actioncolumn',
				header: '',
				flex : 1,
				align : 'center',				
				items:[{
					icon : 'images/information.png',
					tooltip : 'gcoutput Log',
					handler: function(grid, rowIndex, colIndex) {
                    	showLog(grid.getStore().getAt(rowIndex).data.Id,'gcoutput');                    	
                	}					
				},{
					icon : 'images/book_open.png',
					tooltip : 'checkmyjob Log',
					handler: function(grid, rowIndex, colIndex) {
                    	showLog(grid.getStore().getAt(rowIndex).data.Id,'checkmyjob');                    	
                	}					
				}]
			}
	    ],
	    height: "40%",
	    width: "100%",
	    renderTo: Ext.get('contentPane')
	});
	
	workflowJobsStore.load();
	Ext.create('Ext.grid.Panel', {
	    title: 'Workflow Jobs',
	    margin: '0 0 0 0',
	    store: workflowJobsStore,
	    columns: [	        
	        { header: 'Label', dataIndex: 'labelId', flex: 4},
	        { header: 'Name', dataIndex: 'name', flex: 2},	        
	        { header: 'Waiting Time', dataIndex: 'stuckTime', flex: 2,renderer:function(value){return parseFloat(value).toFixed(5)}},
	        { header: 'Owner', dataIndex: 'owner', flex: 2},
	        { header: 'ID', dataIndex: 'Id', flex: 2 },
	        { header: 'Workflow ID', dataIndex: 'workflowId', flex: 2 },
	        {
				xtype:'actioncolumn',
				header: '',
				flex : 1,
				align : 'center',				
				items:[{
					icon : 'images/information.png',
					tooltip : 'gcoutput Log',
					handler: function(grid, rowIndex, colIndex) {
                    	showLog(grid.getStore().getAt(rowIndex).data.Id,'gcoutput');                    	
                	}					
				},{
					icon : 'images/book_open.png',
					tooltip : 'checkmyjob Log',
					handler: function(grid, rowIndex, colIndex) {
                    	showLog(grid.getStore().getAt(rowIndex).data.Id,'checkmyjob');                    	
                	}					
				}]
			}
	    ],
	    height: "50%",
	    width: "100%",
	    renderTo: Ext.get('contentPane')
	});
}

function showLog(Id,type){
	Ext.Ajax.request({
	    url: 'getStuckJobInfo.htm',
	    params : {
	    	jobId : Id,
	    	type : type
		},
	    success: function(response){    		    		
			var stuckJobInfoWindow = Ext.create('Ext.window.Window', {
				    title: 'Output of command '+type+' for the job:'+Id,
				    id : 'stuckJobInfoWindow',
				    height: 631,
				    width: 925,
				    modal:true,
				    bodyPadding: 3,
				    closable:false,	
			        renderTo : Ext.get('contentPane'),
					layout : {
						type : 'vbox',
						pack : 'start',
						align : 'stretch'
					},					
					items : [{
				        xtype     : 'textareafield',
				        grow      : true,				        			        
				        anchor    : '100%',
				        id		  : 'jobInfo',
				        growMax   : 560,
				        growMin   : 560
				    }],
					fbar : ['->', {
						xtype : 'button',
						width : 75,
						text : 'Ok',
						handler : function(event, toolEl, panel) {
							stuckJobInfoWindow.destroy();
						}
					}]
				}).show();

			Ext.getCmp('jobInfo').setValue(response.responseText);
	    },
	    failure: function(){
			alert('Failed to get the information of the job'+data.Id);
		}
	});
}