package com.api.dublinbus;

import java.util.List;

/**
 * Created by victoraldir on 09/07/2017.
 */

class BusStop {

    private String description;
    private List<Bus> busList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Bus> getBusList() {
        return busList;
    }

    public void setBusList(List<Bus> busList) {
        this.busList = busList;
    }
}
