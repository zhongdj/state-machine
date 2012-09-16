package demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.madz.core.common.Dumper;
import net.madz.core.lifecycle.impl.TransitionInvocationHandler;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.lifecycle.meta.impl.StateMachineMetaDataBuilderImpl;
import net.madz.core.verification.VerificationFailureSet;
import demo.DownloadProcess.DownloadRequest;
import demo.IDownloadProcess.StateEnum;
import demo.IDownloadProcess.TransitionEnum;

public class Client {

    public static void main(String[] args) {
	Dumper dumper = new Dumper(System.out);
	dumper.println("");
	dumper.println("Dumping State Machine Meta Data");
	dumper.println("");
	DownloadRequest r = new DownloadRequest("", "", null);
	final DownloadProcess process = new DownloadProcess(r, 3);
	final List<IDownloadProcess> list = new ArrayList<IDownloadProcess>();
	list.add(process);
	StoreHelper.save(list);

	final StateMachineMetaDataBuilderImpl builder = new StateMachineMetaDataBuilderImpl(
		null, "StateMachine");
	@SuppressWarnings("unchecked")
	final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData = (StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum>) builder
	.build(null, IDownloadProcess.class);
	machineMetaData.dump(dumper);
	VerificationFailureSet verificationSet = new VerificationFailureSet();
	machineMetaData.verifyMetaData(verificationSet);
	verificationSet.dump(dumper);
	
	dumper.println("");
	dumper.println("Test Transition");
	dumper.println("");
	
	@SuppressWarnings("rawtypes")
	IDownloadProcess iProcess = (IDownloadProcess) Proxy.newProxyInstance(
		Client.class.getClassLoader(),
		new Class[] { IDownloadProcess.class },
		new TransitionInvocationHandler(process));
	
	dumper.print("From = ");
	machineMetaData.getStateMetaData(iProcess.getState()).dump(dumper);
	iProcess.prepare();
	dumper.print("To   = ");
	machineMetaData.getStateMetaData(iProcess.getState()).dump(dumper);

//	testRecover(machineMetaData, iProcess);
    }

    private static void testRecover(
	    final StateMachineMetaData<IDownloadProcess, StateEnum, TransitionEnum> machineMetaData,
	    IDownloadProcess iProcess) {
	final DownloadProcessRecoverableIterator iterator = new DownloadProcessRecoverableIterator(
		machineMetaData);
	IDownloadProcess downloadProcess = null;
	while (iterator.hasNext()) {
	    downloadProcess = iterator.next();
	    StateEnum state = downloadProcess.getState();
	    StateMetaData<IDownloadProcess, StateEnum> stateMetaData = machineMetaData
		    .getStateMetaData(state);
	    TransitionMetaData corruptTransitionMetaData = stateMetaData
		    .getCorruptTransitionMetaData();
	    if (null != corruptTransitionMetaData
		    && null != corruptTransitionMetaData.getTransitionMethod()) {
		try {
		    corruptTransitionMetaData.getTransitionMethod().invoke(iProcess);
		    // StateEnum nextState = stateMetaData
		    // .nextState(corruptTransitionMetaData
		    // .getTransition());
		    // System.out.println(nextState);
		} catch (IllegalArgumentException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }

}
