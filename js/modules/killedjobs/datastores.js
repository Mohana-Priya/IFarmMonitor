killedJobsStore = Ext.create('Ext.data.Store', {
	model: 'killedJobsModel',
	autoLoad: false,	
	proxy: {
        type: 'ajax',
        url: 'getKilledJobs.htm',
        timeout: 300000,
        reader: {                
                type: 'json'
        }
    }
});

runningJobsStore = Ext.create('Ext.data.Store', {
	model: 'killedJobsModel',
	autoLoad: false,	
	proxy: {
        type: 'ajax',
        url: 'getRunningJobs.htm',
        timeout: 300000,
        reader: {                
                type: 'json'
        }
    }
});