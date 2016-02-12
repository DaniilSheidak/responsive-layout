Ejst.CustomWidget = CQ.Ext.extend(CQ.form.CompositeField, {
 

    hiddenField: null,

    titleField: null,

    targetField: null,

    pathField: null,

    constructor: function(config) {
        config = config || { };
        var defaults = {
            "border": false,
            "layout": "table",
            "columns":3
        };
        config = CQ.Util.applyDefaults(config, defaults);
        Ejst.CustomWidget.superclass.constructor.call(this, config);
    },
 
    // overriding CQ.Ext.Component#initComponent
    initComponent: function() {
        Ejst.CustomWidget.superclass.initComponent.call(this);
 
        this.hiddenField = new CQ.Ext.form.Hidden({
            name: this.name
        });
        this.add(this.hiddenField);
 
        this.titleField = new CQ.form.TextField({
            type:"textfield",
            cls:"ejst-customwidget-1",
            listeners: {
                selectionchanged: {
                    scope:this,
                    fn: this.updateHidden
                }
            },
            optionsProvider: this.optionsProvider
        });
        this.add(this.titleField);
 
        this.targetField = new CQ.Ext.form.Selection({
            type:"select",
            cls:"ejst-customwidget-2",
            listeners: {
                change: {
                    scope:this,
                    fn:this.updateHidden
                }
            }
        });
        this.add(this.targetField);

        this.pathField = new CQ.Ext.form.TextField({
            type:"textfield",
            cls:"ejst-customwidget-2",
            listeners: {
                change: {
                    scope:this,
                    fn:this.updateHidden
                }
            }
        });
        this.add(this.pathField);
 
    },
 
    // overriding CQ.form.CompositeField#processPath
    processPath: function(path) {
        console.log("CustomWidget#processPath", path);
        this.targetField.processPath(path);
    },
 
    // overriding CQ.form.CompositeField#processRecord
    processRecord: function(record, path) {
        console.log("CustomWidget#processRecord", path, record);
        this.targetField.processRecord(record, path);
    },
 
    // overriding CQ.form.CompositeField#setValue
    setValue: function(value) {

        //var parts = JSON.parse(value);
        //this.titleField.setValue(parts.titleField);
        //this.targetField.setValue(parts.targetField);
        //this.pathField.setValue(parts.pathField);
        //this.hiddenField.setValue(value);

        var parts = value.split("/");
        this.titleField.setValue(parts[0]);
        this.targetField.setValue(parts[1]);
        this.pathField.setValue(parts[2]);
        this.hiddenField.setValue(value);
    },
 
    // overriding CQ.form.CompositeField#getValue
    getValue: function() {
        return this.getRawValue();
    },
 
    // overriding CQ.form.CompositeField#getRawValue
    getRawValue: function() {
        if (!this.titleField) {
            return null;
        }
        return this.titleField.getValue() + "/" +
               this.targetField.getValue() + "/" +
            this.pathField.getValue();

        //return JSON.stringify({"titleField": titleField.getValue(), "targetField": targetField.getValue(), "pathField": pathField.getValue()});
    },
 
    // private
    updateHidden: function() {
        this.hiddenField.setValue(this.getValue());
    }
 
});
 
// register xtype
CQ.Ext.reg('ejstcustom', Ejst.CustomWidget);