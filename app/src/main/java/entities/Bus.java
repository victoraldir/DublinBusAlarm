package entities;

import org.joda.time.LocalTime;

/**
 * Created by victor on 06/12/15.
 */
public class Bus {

    private String route;

    private String destination;

    private String time;

    private String stop;

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

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "route='" + route + '\'' +
                ", destination='" + destination + '\'' +
                ", time='" + time + '\'' +
                ", stop='" + stop + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bus bus = (Bus) o;

        if (!route.equals(bus.route)) return false;
        if (!destination.equals(bus.destination)) return false;
        if (!time.equals(bus.time)) return false;
        return stop.equals(bus.stop);

    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + stop.hashCode();
        return result;
    }
}
