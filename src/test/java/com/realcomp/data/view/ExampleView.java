package com.realcomp.data.view;

/**
 *
 * @author krenfro
 */
public class ExampleView extends BaseRecordView{


    public String getValue(){
        return (String) record.get("value");
    }
    
}
