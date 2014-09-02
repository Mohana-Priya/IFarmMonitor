function showKilledJobs(){
	runningJobsStore.load();
	Ext.create('Ext.grid.Panel', {
	    title: 'Currently Running Jobs in IFarm',
	    margin: '0 0 0 0',
	    store: runningJobsStore,
	    columns: [	        
	        { header: 'Job', dataIndex: 'taskName', flex: 2},
	        { header: 'Label', dataIndex: 'labelName', flex: 2},	        
	        { header: 'Job Report Date', dataIndex: 'taskReportDate', flex: 2},
	        { header: 'Job Running Time(Hrs)', dataIndex: 'taskRunningTime', flex: 2},
	        { header: 'Time out(Hrs)', dataIndex:'taskTimeout',flex:1},
	        {
				xtype:'actioncolumn',
				header: 'notify',
				flex : 1,
				align : 'center',				
				items:[{
					icon : 'images/picker.png',
					tooltip : 'Send mail',
					handler: function(grid, rowIndex, colIndex) {
                    	showMailWindow(grid.getStore().getAt(rowIndex).data);                    	
                	}					
				}]
			}
	    ],
	    height: "50%",
	    width: "100%",
	    renderTo: Ext.get('contentPane')
	});
	
	killedJobsStore.load();
	Ext.create('Ext.grid.Panel', {
	    title: 'Killed Jobs due to timeout',
	    margin: '0 0 0 0',
	    store: killedJobsStore,
	    columns: [	        
	        { header: 'Job', dataIndex: 'taskName', flex: 4},
	        { header: 'Label', dataIndex: 'labelName', flex: 2},	        
	        { header: 'Job Report Date', dataIndex: 'taskReportDate', flex: 2},
	        { header: 'Time out(Hrs)', dataIndex:'taskTimeout',flex:1}               
	    ],
	    height: "50%",
	    width: "100%",
	    renderTo: Ext.get('contentPane')
	});
}

function showMailWindow (rec){
	Ext.getBody().mask("loading..");
	Ext.Ajax.request({
	    url: 'getJobOwner.htm',
	    params : {
	    	labelName : rec.labelName	    	
		},
	    success: function(response){
	    	Ext.getBody().unmask();
			var mailWindow = Ext.create('Ext.window.Window', {
		    title: 'Send Mail',
		    id : 'mailWindow',
		    height: 600,
		    width: 600,
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
		        id		  : 'fromAddress',
		        fieldLabel: 'From',        
		        height	  :30,
		        value	  :'mohana.attuluri@oracle.com'
		    },{
		        xtype     : 'textareafield',
		        id		  : 'toAddress',
		        fieldLabel: 'To', 	        
		        height    : 30,
		        value	  : response.responseText
		    },{
		        xtype     : 'textareafield',
		        id		  : 'subject',
		        fieldLabel: 'Subject', 	        
		        height    : 30,
		        value     : 'Alert Mail: The job '+rec.taskName+' for label '+rec.labelName+' is running for '+rec.taskRunningTime+' Hrs.'
		    },{
		        xtype     : 'textareafield',
		        grow      : true,				        			        
		        anchor    : '100%',
		        id		  : 'message',
		        fieldLabel: 'Message', 
		        growMax   : 560,
		        growMin   : 470,
		        value     :'The job '+rec.taskName+' for label '+rec.labelName+' is running for '+rec.taskRunningTime+' Hrs.\nIts time out is '+rec.taskTimeout+" hrs."
		    }],
			fbar : ['->', {
				xtype : 'button',
				width : 75,
				text : 'Send',
				handler : function(event, toolEl, panel) {
					sendMail();
				}
			},
			{
				xtype : 'button',
				width : 75,
				text : 'Cancel',
				handler : function(event, toolEl, panel) {
					mailWindow.destroy();
				}
			}]
			}).show();
		},
	    failure: function(){
	    	Ext.getBody().unmask();
			alert('Failed to send the mail');			
		}
	});
	
}

function sendMail(){
	Ext.Ajax.request({
	    url: 'sendMail.htm',
	    params : {
	    	fromAddress : Ext.getCmp('fromAddress').getValue(),
	    	toAddress : Ext.getCmp('toAddress').getValue(),
	    	subject : Ext.getCmp('subject').getValue(),
	    	message : Ext.getCmp('message').getValue()
		},
	    success: function(response){
			alert(response.responseText);
			if(response.responseText == "sent mail successfully"){
				Ext.getCmp('mailWindow').destroy();
			}
	    },
	    failure: function(){
			alert('Failed to send the mail');			
		}
	});
}