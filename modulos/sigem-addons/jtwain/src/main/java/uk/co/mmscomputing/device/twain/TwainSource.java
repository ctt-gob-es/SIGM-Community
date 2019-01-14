package uk.co.mmscomputing.device.twain;

import java.io.*;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;

public class TwainSource extends TwainIdentity implements TwainConstants,ScannerDevice{

  private boolean busy;
  private int     state;
  private int     hWnd;

  private int     showUI     = 1;               // show DS GUI
  private int     modalUI    = 0;               // is modeless GUI

  private int     iff        = TWFF_BMP;        // image file format

  private TwainITransferFactory transferFactory;
  private boolean userCancelled;

  TwainSource(TwainSourceManager manager,int hwnd,boolean bus){
    super(manager);
    this.hWnd=hwnd;
    this.busy=bus;
    this.state=3;

    this.userCancelled=false;
    this.transferFactory=new TwainDefaultTransferFactory();
  }

  byte[] getIdentity(){return identity;}

  public boolean isBusy(){             return busy;}
         void    setBusy(boolean b){   busy=b; jtwain.signalStateChange(this);}
  public int     getState(){           return state;}
         void    setState(int s){      state=s;jtwain.signalStateChange(this);}

  public void    setCancel(boolean c){ userCancelled=c;}
  public boolean getCancel(){          return userCancelled;}


  void checkState(int state)throws TwainIOException{
    if(this.state==state){return;}
    throw new TwainIOException(getClass().getName()+".checkState:\n\tSource not in state "+state+" but in state "+this.state+".");
  }

  int getConditionCode()throws TwainIOException{
    byte[] status=new byte[4];                     // TW_STATUS
    int    rc    =jtwain.callSource(identity,DG_CONTROL,DAT_STATUS,MSG_GET,status);
    if(rc!=TWRC_SUCCESS){
      throw new TwainResultException("Cannot retrieve twain source's status.",rc);
    }
    return jtwain.getINT16(status,0);
  }

  public void call(int dg,int dat,int msg,byte[] data)throws TwainIOException{
    int rc=jtwain.callSource(identity,dg,dat,msg,data);
    switch(rc){
    case TWRC_SUCCESS:          return;
    case TWRC_FAILURE:          throw TwainFailureException.create(getConditionCode());
    case TWRC_CHECKSTATUS:      throw new TwainResultException.CheckStatus();
    case TWRC_CANCEL:           throw new TwainResultException.Cancel();
    case TWRC_DSEVENT:          return;
    case TWRC_NOTDSEVENT:       throw new TwainResultException.NotDSEvent();
    case TWRC_XFERDONE:         throw new TwainResultException.TransferDone();
//    case TWRC_ENDOFLIST:        throw new TwainResultException.EndOfList();
//    case TWRC_INFONOTSUPPORTED: throw new TwainResultException.InfoNotSupported();
//    case TWRC_DATANOTAVAILABLE: throw new TwainResultException.DataNotAvailable();
    default:                    
      System.err.println(getClass().getName()+".call\n\trc="+rc);
      throw new TwainResultException("Failed to call source.",rc);
    }
  }

  public TwainCapability[] getCapabilities()throws TwainIOException{
    return TwainCapability.getCapabilities(this);
  }

  public TwainCapability getCapability(int cap)throws TwainIOException{              // use only in state 4
    return new TwainCapability(this,cap);
  }

  public TwainCapability getCapability(int cap,int mode)throws TwainIOException{     // use only in state 4
    return new TwainCapability(this,cap,mode);
  }

  public TwainITransferFactory getTransferFactory() {
    return transferFactory;
  }

  public void setTransferFactory(TwainITransferFactory transferFactory){
    if(transferFactory==null){
      throw new IllegalArgumentException(getClass().getName()+".setTransferFactory\n\tTwain transfer factory cannot be null.");
    }
    this.transferFactory = transferFactory;
  }

  public void setShowUI(boolean enable){showUI=(enable)?1:0;}
  public boolean isModalUI(){return (modalUI==1);}

  public void setCapability(int cap,boolean v)throws ScannerIOException{
    TwainCapability tc=getCapability(cap,MSG_GETCURRENT);
    if(tc.booleanValue()!=v){
      tc.setCurrentValue(v);
      if(getCapability(cap).booleanValue()!=v){
        throw new ScannerIOException(getClass().getName()+".setCapability:\n\tCannot set capability "+TwainCapability.getCapName(cap)+" to "+v);
      }
    }
  }

  public void setCapability(int cap,int v)throws ScannerIOException{
    TwainCapability tc=getCapability(cap,MSG_GETCURRENT);
    if(tc.intValue()!=v){
      tc.setCurrentValue(v);
      if(getCapability(cap).intValue()!=v){
        throw new ScannerIOException(getClass().getName()+".setCapability:\n\tCannot set capability "+TwainCapability.getCapName(cap)+" to "+v);
      }
    }
  }

  public void setCapability(int cap,double v)throws ScannerIOException{
    TwainCapability tc=getCapability(cap,MSG_GETCURRENT);
    if(tc.doubleValue()!=v){
      tc.setCurrentValue(v);
      if(getCapability(cap).doubleValue()!=v){
        throw new ScannerIOException(getClass().getName()+".setCapability:\n\tCannot set capability "+TwainCapability.getCapName(cap)+" to "+v);
      }
    }
  }

  // negotiation options common to Twain & Sane

  public boolean isUIControllable(){
    try{                                                    // TW_ONEVALUE/TW_BOOL if >=1.6 TWAIN compliant
      return getCapability(CAP_UICONTROLLABLE).booleanValue(); 
    }catch(Exception e){                                    // if some error assume source does not support ShowUI=false
      jtwain.signalException(getClass().getName()+".isUIControllable:\n\t"+e);return false;                                         
    }
  }

  public boolean isDeviceOnline(){                          // [1] 9-369 CAP_DEVICEONLINE
    try{                                                    // TW_ONEVALUE/TW_BOOL
      return getCapability(CAP_DEVICEONLINE).booleanValue();
    }catch(Exception e){                                    // if some error assume source is on
      jtwain.signalException(getClass().getName()+".isOnline:\n\t"+e);return true;                                          
    }
  }

  public void setShowUserInterface(boolean show)throws ScannerIOException{
    setShowUI(show);
  }

  public void setShowProgressBar(boolean show)throws ScannerIOException{
    setCapability(CAP_INDICATORS,show);                         // works only if user interface is inactive
  }

  public void setResolution(double dpi)throws ScannerIOException{
    setCapability(ICAP_UNITS,TWUN_INCHES);                      // setResolution expects dots per inch
    setCapability(ICAP_XRESOLUTION,dpi);
    setCapability(ICAP_YRESOLUTION,dpi);
  }

  public void setRegionOfInterest(int x, int y, int w, int h)throws ScannerIOException{
    if((x==-1)&&(y==-1)&&(w==-1)&&(h==-1)){                     // int version of setRegionOfInterest expects pixels
      new TwainImageLayout(this).reset();
    }else{
      setCapability(ICAP_UNITS,TWUN_PIXELS);
      TwainImageLayout til=new TwainImageLayout(this);
      til.get();                                                // System.out.println(til.toString());
      til.setLeft(x);til.setTop(y);                             // scan from topLeft(x,y) to rightBottom(x+width,y+height)
      til.setRight(x+w);til.setBottom(y+h);
      til.set();
    }
  }

  public void setRegionOfInterest(double x, double y, double w, double h)throws ScannerIOException{
    if((x==-1)&&(y==-1)&&(w==-1)&&(h==-1)){                     // double version of setRegionOfInterest expects millimeters
      new TwainImageLayout(this).reset();
    }else{
      setCapability(ICAP_UNITS,TWUN_CENTIMETERS);
      TwainImageLayout til=new TwainImageLayout(this);
      til.get();                                                // System.out.println(til.toString());
      til.setLeft(x/10.0);til.setTop(y/10.0);                   // scan from topLeft(x,y) to rightBottom(x+width,y+height)
      til.setRight((x+w)/10.0);til.setBottom((y+h)/10.0);
      til.set();
    }
//    til.get();                                                // System.out.println(til.toString());
  }

  public void select(String name)throws ScannerIOException{
    checkState(3);
    TwainSourceManager manager=jtwain.getSourceManager();
    try{
      TwainIdentity device=new TwainIdentity(manager);
      device.getFirst();                                        // get first identity
      while(true){                                              // while(not EndOfList Exception thrown)
        if(device.getProductName().equals(name)){
          System.arraycopy(device.identity,0,identity,0,identity.length);break;
        }
        device.getNext();                                       // get next identity
      }
    }catch(TwainResultException.EndOfList treeol){
      throw new TwainIOException(getClass().getName()+".select(String name)\n\tCannot find twain data source: '"+name+"'");
    }
  }

  // END: negotiation options common to Twain & Sane

  // image acquisition

  void enable()throws TwainIOException{                         // state 4 -> 5
    checkState(4);
    jtwain.negotiateCapabilities(this);                         // still in state 4 tell application to negotiate capabilities and defaults               
    if(getState()<4){return;}                                   // application <=> source negotiation failed => application closed source

    int xfer=new TwainCapability.XferMech(this).intValue();     // what transfer mode do we use
    if(xfer==TWSX_NATIVE){                                      // if native transfer (dib)
    }else if(xfer==TWSX_FILE){                                  // if file transfer mode
      try{                                                      // if source supports different file formats
        iff=getCapability(ICAP_IMAGEFILEFORMAT).intValue();     // cache file format
      }catch(Exception e){                                      // else
        iff=TWFF_BMP;                                           // use default
      }
    }
    modalUI=0;
    byte[] gui=new byte[8];                                     // TW_USERINTERFACE
    jtwain.setINT16(gui,0,showUI);                              // 1: show gui; 0: do not show gui
    jtwain.setINT16(gui,2,modalUI);
    jtwain.setINT32(gui,4,hWnd);                                // set parent window
    try{
      call(DG_CONTROL,DAT_USERINTERFACE,MSG_ENABLEDS,gui);      // enable source; pop up gui if ShowGUI=true
      modalUI=jtwain.getINT16(gui,2);
      setState(5);
    }catch(TwainResultException.CheckStatus trecs){             // ShowGUI=false not supported
      setState(5);                                              // continue with GUI
                                    // to do: check status; don't have a source that does this.
    }catch(TwainResultException.Cancel trec){
      close();        
    }
  }

  private void transfer(TwainITransfer tt)throws TwainIOException{            // 5 -> 6
    try{
      byte[]  pendingXfers=new byte[6];                                       // TW_PENDINGXFERS
      do{
        setState(6);
        jtwain.setINT16(pendingXfers,0,0);                                    // pendingXfer.Count = 0;  
        try{
          tt.setCancel(userCancelled);
          tt.initiate();
        }catch(TwainResultException.TransferDone tretd){                      // state 7: memory allocated
          setState(7);
          tt.finish();
          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_ENDXFER,pendingXfers);         // tell source we are done with image
          if(jtwain.getINT16(pendingXfers,0)==0){                             // state 6: if pendingXfers!=0
            setState(5);                                                      // state 5: if pendingXfers==0
          }
        }catch(TwainUserCancelException tuce){                                // state 6: cancel via setCancel(true)
          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_RESET,pendingXfers);           // tell source to cancel pending images
          setState(5);
        }catch(TwainResultException.Cancel trec){                             // state 7: cancel via source gui
          tt.cancel();

          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_ENDXFER,pendingXfers);         // state 6: tell source we are done with image
          if(jtwain.getINT16(pendingXfers,0) > 0){
            call(DG_CONTROL,DAT_PENDINGXFERS,MSG_RESET,pendingXfers);         // tell source to cancel pending images
          }
          setState(5);
        }catch(TwainFailureException tfe){                                    // state 6/7: no memory allocated
          jtwain.signalException(getClass().getName()+".transfer:\n\t"+tfe);

          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_ENDXFER,pendingXfers);         // tell source we are done with image
          if(jtwain.getINT16(pendingXfers,0) > 0){
            call(DG_CONTROL,DAT_PENDINGXFERS,MSG_RESET,pendingXfers);         // tell source to cancel pending images
          }
          setState(5);
        }finally{
          tt.cleanup();
        }
      }while(jtwain.getINT16(pendingXfers,0)!=0);                             // ADF scanner: pendingXfers = -1 if not known
    }finally{
      if(userCancelled||(showUI==0)){                                         // User cannot close source
        userCancelled=false;
        disable();                                                            // hence close here
        close();
      }
    }
  }

  void transferImage()throws TwainIOException{
    switch(getXferMech()){
    case TWSX_NATIVE:   transfer(transferFactory.createNativeTransfer(this));  break;
    case TWSX_FILE:     transfer(transferFactory.createFileTransfer(this));    break;
    case TWSX_MEMORY:   transfer(transferFactory.createMemoryTransfer(this));  break;
    default:                                            // shouldn't be here
      System.out.println(getClass().getName()+".transferImage:\n\tDo not support this transfer mode: "+getXferMech()); 
      System.err.println(getClass().getName()+".transferImage:\n\tDo not support this transfer mode: "+getXferMech()); 
      break;
    }
  }

  void disable()throws TwainIOException{                    //  state 5 -> 4
    if(state<5){return;}

    byte[] gui=new byte[8];                                 // TW_USERINTERFACE
    jtwain.setINT16(gui,0,-1);                              // TWON_DONTCARE8
    jtwain.setINT16(gui,2,0);
    jtwain.setINT32(gui,4,hWnd);                            // set parent window

    call(DG_CONTROL,DAT_USERINTERFACE,MSG_DISABLEDS,gui);
    setState(4);
  }

  public void  close()throws TwainIOException{                      //  state 4 -> 3
    if(state!=4){return;}
    super.close();
    busy=false;
    setState(3);
  }

  int handleGetMessage(int msgPtr)throws TwainIOException{  // callback functions cpp -> java; windows thread;
    if(state<5){return TWRC_NOTDSEVENT;}
    try{
      byte[] event=new byte[6];                             // TW_EVENT
      jtwain.setINT32(event,0,msgPtr);                      // twEvent.pEvent=(TW_MEMREF)&msg;
      jtwain.setINT16(event,4,0);                           // twEvent.TWMessage=MSG_NULL;

      call(DG_CONTROL,DAT_EVENT,MSG_PROCESSEVENT,event);    // [1] 7 - 162

      int message=jtwain.getINT16(event,4);                 // if event was handled by source
      switch (message){                                     // any messages from source
      case MSG_XFERREADY:                                   // state 5 -> 6                   
        transferImage();                                    // source has an image for us
        break;
      case MSG_CLOSEDSOK:                                   // Do not use DG_CONTROL/DAT_USERINTERFACE/MSG_ENABLEDSUIONLY, 
                                                            // hence shouldn't get this event
      case MSG_CLOSEDSREQ:                                  // source wants us to close it
        disable();
        close();
        break;
      case MSG_DEVICEEVENT:                                 // Do not allow this event, hence don't get this event
      case MSG_NULL:                                        // Event fully processed by source
      default:
        break;
      }
      return TWRC_DSEVENT;
    }catch(TwainResultException.NotDSEvent trendse){        // if event was not handled by source
      return TWRC_NOTDSEVENT;
    }
  }

  public int getXferMech()throws TwainIOException{
    return new TwainCapability.XferMech(this).intValue();
  }

  public void setXferMech(int mech){
    try{
      switch(mech){
      case TWSX_NATIVE:
      case TWSX_FILE:
      case TWSX_MEMORY:                     break;
      default:          mech=TWSX_NATIVE;   break;
      }
      TwainCapability tc;
      tc=getCapability(ICAP_XFERMECH,MSG_GETCURRENT);       // some sources cannot use enumerations to set capabilities,
      if(tc.intValue()!=mech){                              // hence use MSG_GETCURRENT => TW_ONEVALUE/TW_UINT16
        tc.setCurrentValue(mech);
        if(getCapability(ICAP_XFERMECH).intValue()!=mech){
          jtwain.signalException(getClass().getName()+".setXferMech:\n\tCannot change transfer mechanism to mode="+mech);
        }
      }
    }catch(TwainIOException e){                             // Shouldn't happen must be supported by all sources.
      jtwain.signalException(getClass().getName()+".setXferMech:\n\t"+e);
    }
  }

  int getImageFileFormat(){return iff;}

  public void setImageFileFormat(int iff){
    try{
      TwainCapability tc;
      switch(iff){
      case TWFF_TIFF:      case TWFF_BMP:     case TWFF_JFIF:
      case TWFF_TIFFMULTI: case TWFF_PNG:
        break;
      default:
        iff=TWFF_BMP;                                       // (must be supported by all windows sources)
        break;
      }
      tc=getCapability(ICAP_IMAGEFILEFORMAT,MSG_GETCURRENT);// TW_ONEVALUE/TW_UINT16
      if(tc.intValue()!=iff){
        tc.setCurrentValue(iff);
        if(getCapability(ICAP_IMAGEFILEFORMAT).intValue()!=iff){
          jtwain.signalException(getClass().getName()+".setImageFileFormat:\n\tCannot change file format to format="+iff);
        }
      }
    }catch(Exception e){
      jtwain.signalException(getClass().getName()+".setImageFileFormat:\n\t"+e);
    }
  }

//  public TwainFileSystem getFileSystem(){return new TwainFileSystem(this);}
}


