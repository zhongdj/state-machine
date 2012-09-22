package demo;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.madz.core.common.Dumper;
import net.madz.core.event.LifeCycleEvent;
import net.madz.core.event.LifeCycleEventUtils;
import net.madz.core.lifecycle.impl.TransitionInvocationHandler;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.impl.StateMachineMetaDataBuilderImpl;
import net.madz.core.verification.VerificationFailureSet;
import demo.DownloadProcess.DownloadRequest;
import demo.IDownloadProcess.StateEnum;
import demo.IDownloadProcess.TransitionEnum;

public class Client {

    public static void main(String[] args) {

        LifeCycleEventUtils.notify(LifeCycleEvent.INIT_EVENT);

        LifeCycleEventUtils.notify(LifeCycleEvent.STARTUP_EVENT);

        LifeCycleEventUtils.notify(LifeCycleEvent.READY);

        final Dumper dumper = new Dumper(System.out);

        final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData = testBuildStateMachineMetaData(dumper);

        final DownloadProcess process = createSampleProcess();

        testTransition(dumper, process, machineMetaData);

        // testRecover(machineMetaData, iProcess);

        LifeCycleEventUtils.notify(LifeCycleEvent.SHUTDOWN_EVENT);

        LifeCycleEventUtils.notify(LifeCycleEvent.TERMINATION_EVENT);
    }

    private static DownloadProcess createSampleProcess() {
        DownloadRequest r = new DownloadRequest("", "", null);
        final DownloadProcess process = new DownloadProcess(r, 3);
        final List<IDownloadProcess> list = new ArrayList<IDownloadProcess>();
        list.add(process);
        StoreHelper.save(list);
        return process;
    }

    private static StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> testBuildStateMachineMetaData(Dumper dumper) {
        dumper.println("");
        dumper.println("Dumping State Machine Meta Data");
        dumper.println("");
        final StateMachineMetaDataBuilderImpl builder = new StateMachineMetaDataBuilderImpl(null, "StateMachine");
        @SuppressWarnings("unchecked")
        final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData = (StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum>) builder
                .build(null, IDownloadProcess.class);
        machineMetaData.dump(dumper);
        VerificationFailureSet verificationSet = new VerificationFailureSet();
        machineMetaData.verifyMetaData(verificationSet);
        verificationSet.dump(dumper);
        return machineMetaData;
    }

    private static void testTransition(Dumper dumper, final DownloadProcess process,
            final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData) {
        dumper.println("");
        dumper.println("Test Transition");
        dumper.println("");

        @SuppressWarnings("rawtypes")
        IDownloadProcess iProcess = (IDownloadProcess) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class[] { IDownloadProcess.class },
                new TransitionInvocationHandler(process));

        dumper.print("From = ");
        machineMetaData.getStateMetaData(iProcess.getState()).dump(dumper);
        iProcess.prepare();
        dumper.print("To   = ");
        machineMetaData.getStateMetaData(iProcess.getState()).dump(dumper);
        // final List<IDownloadProcess> list = StoreHelper.list();
        // list.add(process);
        // StoreHelper.save(list);
    }

}
