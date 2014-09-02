Ext.define('stuckJobsModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name:'owner'},	
		{name:'name'},
		{name:'labelId'},
		{name:'stuckTime'},
		{name:'Id'},
		{name:'workflowId'}
    ]
});

Ext.define('workflowJobsModel',{
	extend: 'Ext.data.Model',
    fields: [
		{name:'owner'},	
		{name:'name'},
		{name:'labelId'},
		{name:'stuckTime'},
		{name:'Id'},
		{name:'workflowId'}
    ]
});