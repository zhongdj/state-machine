package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zuora.core.recovery.IRecoverableIterator;

public class DownloadProcessRecoverableIterator implements IRecoverableIterator<IDownloadProcess> {

   private final Iterator<IDownloadProcess> iterator;

   private DownloadProcessRecoverableIterator() {
      final List<IDownloadProcess> downloads = getSampleProcesses();
      iterator = downloads.iterator();
   }

   public List<IDownloadProcess> getSampleProcesses() {
      final ArrayList<IDownloadProcess> result = new ArrayList<IDownloadProcess>();
      ObjectInputStream ois = null;
      try {
         final File persistent = new File("dataStore");
         System.out.println(persistent.getAbsolutePath());
         ois = new ObjectInputStream(new FileInputStream(persistent));
      }
      catch (Exception e) {
         throw new IllegalStateException(e);
      }
      finally {
         if (null != ois) {
            try {
               ois.close();
            }
            catch (IOException e) {
               throw new IllegalStateException(e);
            }
         }
      }
      return result;
   }

   @Override
   public boolean hasNext() {
      return iterator.hasNext();
   }

   @Override
   public IDownloadProcess next() {
      return iterator.next();
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }

}
