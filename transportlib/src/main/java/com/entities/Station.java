package com.entities;

import java.util.List;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class Station {

    private String tag;
    private List<Transport> transportList;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Transport> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<Transport> transportList) {
        this.transportList = transportList;
    }
}
