package exceptions;

import fragments.BusStopFormDialogFragment;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class NoInterfaceImplementation extends RuntimeException {

    public NoInterfaceImplementation(){
        super(BusStopFormDialogFragment.BusStopFormListener.class.getSimpleName() + " not implemented!");
    }

}
