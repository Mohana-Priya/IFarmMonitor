resourcesStore = Ext.create('Ext.data.Store', {
	model: 'resourcesModel',
	autoLoad: false,
	groupField:'zoneName'
});

platformsStore = Ext.create('Ext.data.Store', {
	fields:['platform'],
	autoLoad: true,		
	proxy: {
        type: 'ajax',
        url: 'getPlatforms.htm',
        timeout: 300000,
        reader: {                
                type: 'json'
        }        
    }
});