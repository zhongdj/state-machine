package demo;

import static demo.IDownloadProcess.TransitionEnum.Activate;
import static demo.IDownloadProcess.TransitionEnum.Err;
import static demo.IDownloadProcess.TransitionEnum.Finish;
import static demo.IDownloadProcess.TransitionEnum.Inactivate;
import static demo.IDownloadProcess.TransitionEnum.Pause;
import static demo.IDownloadProcess.TransitionEnum.Prepare;
import static demo.IDownloadProcess.TransitionEnum.Receive;
import static demo.IDownloadProcess.TransitionEnum.Remove;
import static demo.IDownloadProcess.TransitionEnum.Restart;
import static demo.IDownloadProcess.TransitionEnum.Resume;
import static demo.IDownloadProcess.TransitionEnum.Start;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;
import com.zuora.core.state.IllegalStateChangeException;
import com.zuora.core.state.StateContext;
import com.zuora.core.state.annotations.StateMachine;
import com.zuora.core.state.annotations.StateSet;
import com.zuora.core.state.annotations.Transition;
import com.zuora.core.state.annotations.TransitionSet;
import com.zuora.core.state.annotations.action.Corrupt;
import com.zuora.core.state.annotations.action.End;
import com.zuora.core.state.annotations.action.Fail;
import com.zuora.core.state.annotations.action.Recover;
import com.zuora.core.state.annotations.action.Redo;
import com.zuora.core.state.annotations.action.Timeout;
import com.zuora.core.state.annotations.state.Corrupted;
import com.zuora.core.state.annotations.state.Initial;
import com.zuora.core.state.annotations.state.Running;
import com.zuora.core.state.annotations.state.Stopped;

import demo.IDownloadProcess.StateEnum;
import demo.IDownloadProcess.TransitionEnum;

@StateMachine(states = @StateSet(StateEnum.class), transitions = @TransitionSet(TransitionEnum.class))
public interface IDownloadProcess extends Serializable, IReactiveObject {

   public static enum TransitionEnum implements ITransition {
      @Recover
      @Timeout(3000L)
      Activate,
      @Corrupt
      @Timeout(3000L)
      Inactivate,
      @Fail
      @Timeout(3000L)
      Err, Prepare, Start, Resume, Pause, Finish, Receive,
      @Redo
      @Timeout(3000L)
      Restart, Remove;
   }

   public static enum StateEnum implements IState<IDownloadProcess> {
      @Initial
      New(0, false, true),
      @Running
      Queued(1),
      @Running
      Started(2),
      @Corrupted
      InactiveQueued(3),
      @Corrupted
      InactiveStarted(4),
      @Stopped
      Paused(5),
      @Stopped
      Failed(6),
      @Stopped
      Finished(7),
      @End
      Removed(8, true);

      static {
         New.transitionFunction.put(Prepare, Queued);
         New.transitionFunction.put(Remove, Removed);

         Queued.transitionFunction.put(Pause, Paused);
         Queued.transitionFunction.put(Start, Started);
         Queued.transitionFunction.put(Remove, Removed);
         Queued.transitionFunction.put(Inactivate, InactiveQueued);

         InactiveQueued.transitionFunction.put(Activate, Queued);
         InactiveQueued.transitionFunction.put(Remove, Removed);

         Started.transitionFunction.put(Pause, Paused);
         Started.transitionFunction.put(Receive, Started);
         Started.transitionFunction.put(Inactivate, InactiveStarted);
         Started.transitionFunction.put(Err, Failed);
         Started.transitionFunction.put(Finish, Finished);
         Started.transitionFunction.put(Remove, Removed);

         InactiveStarted.transitionFunction.put(Activate, Queued);
         InactiveStarted.transitionFunction.put(Remove, Removed);

         Paused.transitionFunction.put(Resume, New);
         Paused.transitionFunction.put(Restart, New);
         Paused.transitionFunction.put(Remove, Removed);

         Failed.transitionFunction.put(Resume, New);
         Failed.transitionFunction.put(Restart, New);
         Failed.transitionFunction.put(Remove, Removed);

         Finished.transitionFunction.put(Remove, Removed);
         Finished.transitionFunction.put(Restart, New);
      }

      final int seq;
      final boolean end;
      final boolean initial;
      final HashMap<TransitionEnum, StateEnum> transitionFunction = new HashMap<TransitionEnum, StateEnum>();

      private StateEnum(final int seq) {
         this(seq, false, false);
      }

      private StateEnum(final int seq, final boolean end) {
         this(seq, end, false);
      }

      private StateEnum(final int seq, final boolean end,
         final boolean initial) {
         this.seq = seq;
         this.end = end;
         this.initial = initial;
      }

      @Override
      public int seq() {
         return seq;
      }

      @Override
      public Map<TransitionEnum, StateEnum> getTransitionFunction() {
         return Collections.unmodifiableMap(transitionFunction);
      }

      @Override
      public StateEnum doStateChange(StateContext<IDownloadProcess, IState<IDownloadProcess>> context) {
         if (!transitionFunction.containsKey(context.getCurrentState())) {
            throw new IllegalStateChangeException(context);
         }

         return transitionFunction.get(context.getCurrentState());
      }

   }

   /**
    * Rebuild lost states from incorrect persisted state and Enqueue
    */
   @Transition
   void activate();

   /**
    * Expected Precondition: No resource enlisted
    * Any enlisted resource should be delisted
    */
   @Transition
   void inactivate();

   /**
    * Initialize states and Enqueue
    */
   @Transition
   void prepare();

   /**
    * Living thread allocated
    */
   @Transition
   void start();

   /**
    * Rebuild states from correct persisted or in-memory state and Enqueue
    */
   @Transition
   void resume();

   /**
    * Deallocate Thread resource, Persist correct states
    */
   @Transition
   void pause();

   /**
    * Thread die naturally, persist correct states and recycle all resources enlisted.
    */
   @Transition
   void finish();

   /**
    * Process aborted unexpected, persist current states and recycle all resources enlisted
    */
   @Transition
   void err();

   /**
    * While processing, update working progress.
    * 
    * @param bytes
    *           received
    */
   @Transition
   void receive(long bytes);

   /**
    * Roll back all information change after create, Re-initialize states and Enqueue
    */
   @Transition
   void restart();

   /**
    * Make sure enlisted resource has been delisted if there is, such as
    * thread, connection, memory, and persisted information and files.
    * 
    * @param both
    *           downloaded file and the download request/task
    */
   @Transition
   void remove(boolean both);

   StateEnum getState();

   int getId();

   String getUrl();

   String getReferenceUrl();

   String getLocalFileName();

   long getContentLength();
}
