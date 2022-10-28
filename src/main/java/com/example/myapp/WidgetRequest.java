package com.example.myapp;

import java.util.ArrayList;

public class WidgetRequest {
    private String key;
    private String type;
    private String requestId;
    private String widgetId;
    private String owner;
    private String label;
    private String description;
    private ArrayList<Object> otherAttributes;

    public WidgetRequest() 
    {
        key = "";
        type = "";
        requestId = "";
        widgetId = "";
        owner = "";
        label = "";
        description = "";
        otherAttributes = new ArrayList<>();
    }

    public void setKey (String _key) {
        this.key = _key;
    }
    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public String getOwner() {
        return owner.replace(' ', '-');
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Object> getOtherAttributes() {
        return otherAttributes;
    }
}
