package com.alxan.noteefy.common.worker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StateKeeperUnitTest {
    @Test
    public void shouldChangeStateToProcessing() {
        StateKeeper stateKeeper = new StateKeeper();
        stateKeeper.startedProcessing();

        Assertions.assertFalse(stateKeeper.isProcessingStopped());
    }

    @Test
    public void shouldChangeStateToStoppedProcessing() {
        StateKeeper stateKeeper = new StateKeeper();
        stateKeeper.stoppedProcessing();

        Assertions.assertTrue(stateKeeper.isProcessingStopped());
    }
}
