package demo;

import java.util.ArrayList;
import java.util.List;

import net.madz.core.common.Dumper;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.impl.StateMachineMetaDataBuilderImpl;
import net.madz.core.verification.VerificationFailureSet;
import demo.DownloadProcess.DownloadRequest;
import demo.IDownloadProcess.StateEnum;
import demo.IDownloadProcess.TransitionEnum;

public class Client {

    public static void main(String[] args) {
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
	Dumper dumper = new Dumper(System.out);
	machineMetaData.dump(dumper);
	VerificationFailureSet verificationSet = new VerificationFailureSet();
	machineMetaData.verifyMetaData(verificationSet);
	verificationSet.dump(dumper);

//	final DownloadProcessRecoverableIterator iterator = new DownloadProcessRecoverableIterator(
//		machineMetaData);
//	IDownloadProcess downloadProcess = null;
//	while (iterator.hasNext()) {
//	    downloadProcess = iterator.next();
//	    StateEnum state = downloadProcess.getState();
//	    StateMetaData<IDownloadProcess, StateEnum> stateMetaData = machineMetaData
//		    .getStateMetaData(state);
//	    TransitionMetaData corruptTransitionMetaData = stateMetaData
//		    .getCorruptTransitionMetaData();
//	    if (null != corruptTransitionMetaData
//		    && null != corruptTransitionMetaData.getTransitionMethod()) {
//		try {
//		    corruptTransitionMetaData.getTransitionMethod().invoke(
//			    process, null);
//		    StateEnum nextState = stateMetaData
//			    .nextState(corruptTransitionMetaData
//				    .getTransition());
//		    System.out.println(nextState);
//		} catch (IllegalArgumentException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		} catch (IllegalAccessException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		} catch (InvocationTargetException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//	    }
//	}
    }

}
