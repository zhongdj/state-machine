package demo;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.core.event.ILifeCycleEventListener;
import net.madz.core.event.LifeCycleEvent;
import net.madz.core.lifecycle.impl.TransitionInvocationHandler;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.lifecycle.meta.impl.StateMachineMetaDataBuilderImpl;
import net.madz.core.verification.VerificationFailureSet;
import demo.IDownloadProcess.StateEnum;
import demo.IDownloadProcess.TransitionEnum;

public class RecoverMaster implements ILifeCycleEventListener {

    public static final Logger LOGGER = Logger.getLogger(RecoverMaster.class.getName());
    final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData = buildStateMachineMetaData();

    @Override
    public void onLifeCycleEvent(LifeCycleEvent event) {
        LOGGER.log(Level.INFO, event.toString());
        switch (event) {
        case INIT_EVENT:
            break;
        case STARTUP_EVENT:
            corrupt();
            break;
        case READY:
            recover();
            break;
        case SHUTDOWN_EVENT:
            break;
        case TERMINATION_EVENT:
            break;
        }

    }

    private void recover() {
        final DownloadProcessRecoverableIterator iterator = new DownloadProcessRecoverableIterator(machineMetaData);
        final ArrayList<IDownloadProcess> allDownloadProcess = new ArrayList<IDownloadProcess>();
        IDownloadProcess downloadProcess = null;
        while (iterator.hasNext()) {
            downloadProcess = iterator.next();
            allDownloadProcess.add(downloadProcess);
            downloadProcess = (IDownloadProcess) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { IDownloadProcess.class },
                    new TransitionInvocationHandler<IDownloadProcess, IDownloadProcess.StateEnum, IDownloadProcess.TransitionEnum>(downloadProcess));
            StateEnum state = downloadProcess.getState();
            LOGGER.info("recovering download process " + downloadProcess + ".state=" + state);
            StateMetaData<IDownloadProcess, StateEnum> stateMetaData = machineMetaData.getStateMetaData(state);
            TransitionMetaData recoverTransitionMetaData = stateMetaData.getRecoverTransitionMetaData();
            if (null != recoverTransitionMetaData && null != recoverTransitionMetaData.getTransitionMethod()) {
                try {
                    recoverTransitionMetaData.getTransitionMethod().invoke(downloadProcess);
                    state = downloadProcess.getState();
                    LOGGER.info("recovered download process " + downloadProcess + ".state=" + state);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Recover Process Failed");
                }
            }
        }
        StoreHelper.save(allDownloadProcess);
    }

    void corrupt() {

        final DownloadProcessRecoverableIterator iterator = new DownloadProcessRecoverableIterator(machineMetaData);
        IDownloadProcess downloadProcess = null;
        final ArrayList<IDownloadProcess> allDownloadProcess = new ArrayList<IDownloadProcess>();
        while (iterator.hasNext()) {
            downloadProcess = iterator.next();
            allDownloadProcess.add(downloadProcess);
            downloadProcess = (IDownloadProcess) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { IDownloadProcess.class },
                    new TransitionInvocationHandler<IDownloadProcess, IDownloadProcess.StateEnum, IDownloadProcess.TransitionEnum>(downloadProcess));
            StateEnum state = downloadProcess.getState();
            LOGGER.info("corrupting download process " + downloadProcess + " from " + state);
            StateMetaData<IDownloadProcess, StateEnum> stateMetaData = machineMetaData.getStateMetaData(state);
            TransitionMetaData corruptTransitionMetaData = stateMetaData.getCorruptTransitionMetaData();
            if (null != corruptTransitionMetaData && null != corruptTransitionMetaData.getTransitionMethod()) {
                try {
                    corruptTransitionMetaData.getTransitionMethod().invoke(downloadProcess);
                    state = downloadProcess.getState();
                    LOGGER.info("Corrupted download process " + downloadProcess + " to " + state);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Corrupt Process Failed");
                }
            }
        }
        StoreHelper.save(allDownloadProcess);

    }

    private static StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> buildStateMachineMetaData() {
        final StateMachineMetaDataBuilderImpl builder = new StateMachineMetaDataBuilderImpl(null, "StateMachine");
        @SuppressWarnings("unchecked")
        final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData = (StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum>) builder
                .build(null, IDownloadProcess.class);
        VerificationFailureSet verificationSet = new VerificationFailureSet();
        machineMetaData.verifyMetaData(verificationSet);
        if (verificationSet.hasVerificationFailures()) {
            throw new IllegalStateException("StateMachineMetaData has verifiation failures");
        }
        return machineMetaData;
    }
}
