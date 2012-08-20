package demo;

import com.zuora.core.state.IReactiveObject;

import demo.IDownloadProcess.StateEnum;

public class DownloadProcess implements IDownloadProcess, IReactiveObject<StateEnum> {

   private StateEnum state;

   @Override
   public StateEnum getState() {
      return this.state;
   }

   void setState(StateEnum state) {
      this.state = state;
   }

   @Override
   public void activate() {
      // TODO Auto-generated method stub

   }

   @Override
   public void inactivate() {
      // TODO Auto-generated method stub

   }

   @Override
   public void start() {
      // TODO Auto-generated method stub

   }

   @Override
   public void resume() {
      // TODO Auto-generated method stub

   }

   @Override
   public void pause() {
      // TODO Auto-generated method stub

   }

   @Override
   public void finish() {
      // TODO Auto-generated method stub

   }

   @Override
   public void err() {
      // TODO Auto-generated method stub

   }

   @Override
   public void receive() {
      // TODO Auto-generated method stub

   }

   @Override
   public void restart() {
      // TODO Auto-generated method stub

   }

   @Override
   public void remove() {
      // TODO Auto-generated method stub

   }

   private int id;
   private String url;
   private String referenceUrl;
   private String localFileName;
   private long contentLength;
   

   @Override
   public int getId() {
      return id;
   }

   @Override
   public String getUrl() {
      return url;
   }

   @Override
   public String getReferenceUrl() {
      return referenceUrl;
   }

   @Override
   public String getLocalFileName() {
      return localFileName;
   }

   @Override
   public long getContentLength() {
      return contentLength;
   }

   @Override
   public String toString() {
      return "DownloadProcess [state=" + state + ", id=" + id + ", url=" + url + ", referenceUrl=" + referenceUrl + ", localFileName=" + localFileName + ", contentLength=" + contentLength + "]";
   }
   
   

}
