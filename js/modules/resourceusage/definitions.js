Ext.define('resourcesModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name:'platform'},	
		{name:'resourceName'},
		{name:'usageTime'},
		{name:'noOfJobs'},
		{name:'resourceStaus'},
		{name:'noOfVCPUs'},
		{name:'architecture'},
		{name:'zoneName'}
    ]
});