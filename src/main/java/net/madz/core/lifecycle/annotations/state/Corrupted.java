package net.madz.core.lifecycle.annotations.state;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Corrupted {
<<<<<<< HEAD
    int recoverPriority() default 0;
}
=======

    int recoverPriority();

}
>>>>>>> Remove IState.doStateChange
