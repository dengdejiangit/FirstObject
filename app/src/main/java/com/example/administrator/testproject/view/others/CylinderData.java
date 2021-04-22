package com.example.administrator.testproject.view.others;


/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/7/19
 */

public class CylinderData {
    private String parameter;
    private int value;

    public CylinderData() {
    }

    public CylinderData( int value,String parameter) {
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
