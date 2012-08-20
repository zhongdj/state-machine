package demo;

import com.zuora.core.state.meta.impl.builder.StateMachineMetaDataBuilder;

import demo.DownloadProcess.DownloadRequest;

public class Client {

   public static void main(String[] args) {
      DownloadRequest r = new DownloadRequest("", "", null);
      final DownloadProcess process = new DownloadProcess(r,3);

//      InvocationHandler handler = new InvocationHandler() {
//
//         @Override
//         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            IState state = process.getState();
//            StateContext context = new StateContext(process);
//            state.doStateChange(context);
//            return proxy;
//         }
//      };
//
//      IDownloadProcess proxyInstance = (IDownloadProcess) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class[] { IDownloadProcess.class }, handler);
//      proxyInstance.activate();

      StateMachineMetaDataBuilder builder = new StateMachineMetaDataBuilder();
      builder.build(IDownloadProcess.class);
      
      
      
   }

}
