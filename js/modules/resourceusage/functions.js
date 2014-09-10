if(typeof Ext != 'undefined'){
  Ext.core.Element.prototype.unselectable = function(){return this;};
  Ext.view.TableChunker.metaRowTpl = [
   '<tr class="' + Ext.baseCSSPrefix + 'grid-row {addlSelector} {[this.embedRowCls()]}" {[this.embedRowAttr()]}>',
    '<tpl for="columns">',
     '<td class="{cls} ' + Ext.baseCSSPrefix + 'grid-cell ' + Ext.baseCSSPrefix + 'grid-cell-{columnId} {{id}-modified} {{id}-tdCls} {[this.firstOrLastCls(xindex, xcount)]}" {{id}-tdAttr}><div class="' + Ext.baseCSSPrefix + 'grid-cell-inner ' + Ext.baseCSSPrefix + 'unselectable" style="{{id}-style}; text-align: {align};">{{id}}</div></td>',
    '</tpl>',
   '</tr>'
  ];
 } 


function showResourcesUsage(){
	resourcesStore.removeAll();	
	Ext.create('Ext.grid.Panel', {
		id:'ResourcesUsagePanel',
		cls: 'i-header',    
	    tbar: [
        	{ 
        		xtype:'label',
        		id:'resourceUsageLabel',
         		text:'IFarm Resources'
         	},'->',{
         		xtype: 'combobox',         		        		
         		fieldLabel: 'Choose Platform',
    			store: platformsStore,
			    queryMode: 'local',
			    displayField: 'platform',
			    valueField: 'platform',
			    id:'platformCombo',
			    labelAlign:'right',
			    emptyText:'-- select platform --'
         	},{
		        xtype: 'numberfield',		            
		        fieldLabel: 'No Of Days',
		        value: 2,
		        maxValue: 365,
		        minValue: 0,
		        id:'noOfDays',
		        labelAlign:'right'	        
		    },{
         		xtype:'button',
         		width:50,
         		text:'Go',
         		handler:function(){
         			loadResourceStore(Ext.getCmp('noOfDays').getValue(),Ext.getCmp('platformCombo').getValue());
         		}
         	}/*,{
				xtype: 'exportbutton',
				store: resourcesStore
		    }*/
    	],
	    margin: '0 0 0 0',
	    features:[groupingFeature],
		store: resourcesStore,	   	
	    columns: [	        
	        { header: 'Name', dataIndex: 'resourceName', flex: 2},	        
	        { header: 'Usage(Hrs)', dataIndex: 'usageTime', flex: 2},
	        { header: 'Jobs Count', dataIndex: 'noOfJobs', flex: 2},
	        { header: 'Status', dataIndex: 'resourceStaus', flex: 2},
	        { header: 'VCPUs Count', dataIndex: 'noOfVCPUs', flex: 2},
	        { header: 'Architecture', dataIndex: 'architecture', flex: 2}        
	    ],
	    height: "100%",
	    width: "100%",
	    renderTo: Ext.get('contentPane')
	});
	Ext.getCmp('platformCombo').setValue('RHEL4.0');
	loadResourceStore(2,'RHEL4.0');
}

var groupingFeature = Ext.create('Ext.grid.feature.Grouping',
	{
		groupHeaderTpl:'Zone: {name}'
	}
);

function loadResourceStore(noOfDays,platform){
	var newProxy = new Ext.data.proxy.Ajax({
		url: 'getResources.htm',
		model: 'resourcesModel',
	    timeout: 120000,
		reader: {
		   type: 'json'		   
		},
		extraParams:{
			noOfDays:noOfDays,
			platform:platform
		},
		afterRequest:function(request,success){
       		if(request.method = 'PUT'){
       			Ext.getCmp('resourceUsageLabel').setText('IFarm Resources for '+platform); 
       		}
       	}
	});
	resourcesStore.setProxy(newProxy);
	resourcesStore.load();
}