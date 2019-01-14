package uk.co.mmscomputing.device.twain;

import uk.co.mmscomputing.device.scanner.ScannerIOException;

public class TwainSourceManager implements TwainConstants{

  private TwainSource   source;

  TwainSourceManager(int hwnd){
    source=new TwainSource(this,hwnd,false);
    source.getDefault();
  }

  int getConditionCode()throws TwainIOException{           // [1] 7-219
    byte[] status=new byte[4];                             // TW_STATUS
    int    rc    =jtwain.callSourceManager(DG_CONTROL,DAT_STATUS,MSG_GET,status);
    if(rc!=TWRC_SUCCESS){
      throw new TwainResultException("Cannot retrieve twain source manager's status.",rc);
    }
    return jtwain.getINT16(status,0);
  }

  public void call(int dg,int dat,int msg,byte[] data)throws TwainIOException{
    int rc=jtwain.callSourceManager(dg,dat,msg,data);
    switch(rc){
    case TWRC_SUCCESS:      return;
    case TWRC_FAILURE:      throw new TwainFailureException(getConditionCode());
    case TWRC_CANCEL:       throw new TwainResultException.Cancel();
    case TWRC_CHECKSTATUS:  throw new TwainResultException.CheckStatus();
    case TWRC_XFERDONE:     throw new TwainResultException.TransferDone();
    case TWRC_ENDOFLIST:    throw new TwainResultException.EndOfList();
    default:                throw new TwainResultException("Failed to call source.",rc);
    }
  }

  TwainSource getSource(){return source;}

  TwainSource selectSource()throws TwainIOException{       
    source.checkState(3);
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
    try{
      source.userSelect();                                 // new source in state 3
      return source;
    }catch(TwainResultException.Cancel trec){
      return source;
//  }catch(ThreadDeath e){
//    Applet: Select dialog enabled while user reloads webpage. 
//    Happens only first time.
    }finally{
      source.setBusy(false);                               // tell TwainPanel to enable GUI
    }
  }

  void getIdentities(java.util.Vector identities)throws ScannerIOException{
    source.checkState(3);
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
    try{
      TwainIdentity identity=new TwainIdentity(this);
      identity.getFirst();                                 // get first identity
      identities.add(identity);
      while(true){                                         // while(not EndOfList Exception thrown)
        identity=new TwainIdentity(this);
        identity.getNext();                                // get next identity
        identities.add(identity);        
      }      
    }catch(TwainResultException.EndOfList treeol){
    }catch(TwainIOException tioe){
      System.out.println(getClass().getName()+".getIdentities:\n\t"+tioe);
    }finally{
      source.setBusy(false);                               // tell TwainPanel to enable GUI
    }
  }

  TwainSource selectSource(String name)throws ScannerIOException{       
    source.checkState(3);
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
    try{
      source.select(name);                                 // new source in state 3
      return source;
    }finally{
      source.setBusy(false);                               // tell TwainPanel to enable GUI
    }
  }

  TwainSource openSource()throws TwainIOException{         
    source.checkState(3);                                  // old source not enabled
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
    try{
      source.open();
      if(!source.isDeviceOnline()){
        source.close();
        throw new TwainIOException("Selected twain source is not online.");
      }
      source.setState(4);
      return source;
    }catch(TwainResultException.Cancel trec){
      source.setBusy(false);                               // tell TwainPanel to enable GUI
      return source;
    }catch(TwainIOException tioe){  
      source.setBusy(false);                               // tell TwainPanel to enable GUI
      throw tioe;
    }
  }
}
