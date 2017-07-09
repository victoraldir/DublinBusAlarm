package exceptions;

import fragments.AlarmRegisterDialogFragment;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class NoInterfaceImplementation extends RuntimeException {

    public NoInterfaceImplementation(){
        super(AlarmRegisterDialogFragment.AlarmRegisterListener.class.getSimpleName() + " not implemented!");
    }

}
