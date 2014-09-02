stuckjobsStore = Ext.create('Ext.data.Store', {
	model: 'stuckJobsModel',
	autoLoad: false,	
	proxy: {
        type: 'ajax',
        url: 'getStuckJobs.htm',
        timeout: 300000,
        reader: {                
                type: 'json'
        }
    }
});

workflowJobsStore = Ext.create('Ext.data.Store', {
	model: 'workflowJobsModel',
	autoLoad: false,	
	proxy: {
        type: 'ajax',
        url: 'getWorkflowStuckJobs.htm',
        timeout: 300000,
        reader: {                
                type: 'json'
        }
    }
});