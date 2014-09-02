Ext.define('killedJobsModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name:'taskName'},	
		{name:'labelName'},
		{name:'taskReportDate'},
		{name:'taskRunningTime'},
		{name:'taskTimeout'}
    ]
});