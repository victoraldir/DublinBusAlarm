package entities;

/**
 * Created by victor on 06/12/15.
 */
public class Bus {

    private String route;

    private String destination;

    private String time;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return route + destination + time;
    }
}
