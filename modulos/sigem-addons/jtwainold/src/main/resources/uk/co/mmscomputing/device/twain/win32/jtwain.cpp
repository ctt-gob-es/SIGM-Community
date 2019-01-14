#include <windows.h>

#include "twain.h"
#include "bmp.h"
#include "..\uk_co_mmscomputing_device_twain_jtwain.h"

// cc	@cdk\bin\bcc32 -w-par -tWD -I"@cdk\include" -L"@cdk\lib;@cdk\lib\psdk" -n"@d"  -e"jtwain.dll" "@d\*"

bool                  open      =false;
HWND                  hWnd      =NULL;
DSMENTRYPROC          DSM_ENTRY =NULL;   // entry point to the SM
TW_IDENTITY           appID     ={0};    // storage for App states

JavaVM*               gjvm      =NULL;   // cache virtual machine we are running in.
jclass                jtwain    =NULL;
jmethodID             jexecute  =NULL;

bool loadTwainLib(JNIEnv* env){                  
  int err;
  __try{
    HINSTANCE  hLibDLL=LoadLibrary("TWAIN_32.DLL");
    if(hLibDLL==NULL){
      while(env->ExceptionCheck()){env->ExceptionClear();}
      JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException","Cannot load twain_32.dll");
      return false;
    }
    DSM_ENTRY=(DSMENTRYPROC)GetProcAddress(hLibDLL,"DSM_Entry");
    if(DSM_ENTRY==NULL){
      while(env->ExceptionCheck()){env->ExceptionClear();}
      JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException","Cannot find twain's DSM_Entry function.");
      return false;
    }
    return true;                                        // twain state 1 -> 2
  }__except(err=GetExceptionCode(),EXCEPTION_EXECUTE_HANDLER){
          // source or source manager throws an exception somewhere but does not catch it before it returns.
          // Very bad: We cannot recover from this. Hence disable jtwain and ask user to restart application.
          // If we do not catch these exceptions the jvm would (crash) exit anyway.

    char str[512]={0};
    sprintf(str,"jtwain: EXCEPTION 0x%X thrown while loading twain subsystem.",err);
          //      MessageBox(NULL,str,"jtwain.callDSMEntry:",MB_OK);
    while(env->ExceptionCheck()){env->ExceptionClear();}
    JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException",str);
    return false;
  }
}

bool openDataSourceManager(JNIEnv* env){

  if(!loadTwainLib(env)){return false;}

  memset(&appID, 0, sizeof(TW_IDENTITY));
  appID.Id = 0;                                       // init to 0, but Source Manager will assign real value
  appID.Version.MajorNum = TWON_PROTOCOLMAJOR;
  appID.Version.MinorNum = TWON_PROTOCOLMINOR;
  appID.Version.Language = TWLG_USA;
  appID.Version.Country  = TWCY_USA;
  lstrcpy (appID.Version.Info, "2007-07-07");
  lstrcpy (appID.ProductName, "jtwain");

  appID.ProtocolMajor    = TWON_PROTOCOLMAJOR;
  appID.ProtocolMinor    = TWON_PROTOCOLMINOR;
  appID.SupportedGroups  = DG_IMAGE | DG_CONTROL;
  lstrcpy (appID.Manufacturer,  "mm's computing");
  lstrcpy (appID.ProductFamily, "java twain wrapper");

  TW_UINT16 rc=(*DSM_ENTRY)(&appID,NULL,DG_CONTROL,DAT_PARENT,MSG_OPENDSM,(TW_MEMREF)&hWnd);

  open = (rc==TWRC_SUCCESS);                          // twain state 2 -> 3

  return open;
}

jint callDSMEntry(JNIEnv* env,jbyte* csource,jint dg,jint dat,jint msg,jbyte* cbuf){
  if(!open && !openDataSourceManager(env)){return -1;}
  int err;
  __try{
    int rc=(*DSM_ENTRY)(&appID,(TW_IDENTITY*)csource,(TW_UINT32)dg,(TW_UINT16)dat,(TW_UINT16)msg,(TW_MEMREF)cbuf);
          // If the user reloads an applet while the select or acquire GUIs are still up
          // the applet throws ThreadDeath error. Happens only once per browser window 
          // when page has been loaded initially.
          // This is the windows thread. We do not want it to be killed!
    while(env->ExceptionCheck()){env->ExceptionClear();}
    return rc;
  }__except(err=GetExceptionCode(),EXCEPTION_EXECUTE_HANDLER){
          // source or source manager throws an exception somewhere but does not catch it before it returns.
          // Very bad: We cannot recover from this. Hence disable jtwain and ask user to restart application.
          // If we do not catch these exceptions the jvm would (crash) exit anyway.

    char str[512]={0};
    sprintf(str,"jtwain: EXCEPTION 0x%X thrown in twain source or source manager.\nThis may have left the twain subsystem in an unstable state.\nPlease restart application or web-browser.",err);
          //      MessageBox(NULL,str,"jtwain.callDSMEntry:",MB_OK);
    while(env->ExceptionCheck()){env->ExceptionClear();}
    JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException",str);
    return -1;
  }
//  JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException","jtwain: EXCEPTION thrown in twain source or source manager.\nThis may have left the twain subsystem in an unstable state.\nPlease restart application or web-browser.");
//  return -1;
}

JNIEnv* attachCurrentThread(){
  JNIEnv* env=NULL;gjvm->AttachCurrentThread((void**)&env,NULL);return env;
}

JNIEXPORT jobject JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntransferImage(JNIEnv* env, jclass clazz, 
    jint imageHandle
){
  int err;
  __try{
    jboolean hasException=JNI_FALSE;
    return BMP_transferImage(env,clazz,&hasException,(HGLOBAL)imageHandle);
  }__except(err=GetExceptionCode(),EXCEPTION_EXECUTE_HANDLER){
    char str[512]={0};
    sprintf(str,"jtwain: EXCEPTION 0x%X thrown in twain source or source manager.\nThis may have left the twain subsystem in an unstable state.\nPlease restart application or web-browser.",err);
    while(env->ExceptionCheck()){env->ExceptionClear();}
    JNU_ThrowByName(env,"uk/co/mmscomputing/device/twain/TwainException",str);
    return NULL;
  }
}

JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntrigger(JNIEnv* env, jclass clazz,
    jobject caller, jint cmd
){
  PostMessage(hWnd,WM_USER,(WPARAM)cmd,(LPARAM)env->NewGlobalRef(caller));
}

LRESULT FAR PASCAL wndProc(HWND hWnd,UINT iMessage,WPARAM wParam,LPARAM lParam){
  switch(iMessage){
  case WM_CLOSE:
  case WM_ENDSESSION:
  case WM_DESTROY:
    PostQuitMessage(0);
    DestroyWindow(hWnd);
    break;
  case WM_USER:{
    JNIEnv* env=attachCurrentThread();
    jobject obj=(jobject)lParam;
    env->CallStaticVoidMethod(jtwain,jexecute,obj,wParam);
    env->DeleteGlobalRef(obj);
    while(env->ExceptionCheck()){env->ExceptionClear();} // this runs in native thread, ALWAYS DO CLEAR Exceptions
   }break;
  default:
    return DefWindowProc (hWnd, iMessage, wParam, lParam);
  }
  return 0;
}

char szAppName[256]={0};

boolean createWindow(void){
  sprintf(szAppName,"JTWAIN_%d",GetTickCount());      // Because of applets: I.e. If two different applets use this library
  HINSTANCE hInstance=(HINSTANCE)NULL;
  WNDCLASS  wc={0};                                   // Set up application's main window
  wc.style         = NULL;                            // no style bits
  wc.lpfnWndProc   = (WNDPROC)wndProc;                // name of window proc
  wc.cbClsExtra    = 0;                               // no extra bits
  wc.cbWndExtra    = 0;
  wc.hInstance     = hInstance;
  wc.hIcon         = NULL;                            // load resources
  wc.hCursor       = NULL;                            // load mouse icon
  wc.hbrBackground = NULL;                            // use white backgnd
  wc.lpszMenuName  = NULL;                            // no menu
  wc.lpszClassName = szAppName;                       // class named

  if(!RegisterClass(&wc)){
    fprintf(stderr,"Failed to register window class \"%s\"",szAppName);
    return false;
  }
  hWnd = CreateWindow( szAppName,szAppName,WS_POPUP,CW_USEDEFAULT,CW_USEDEFAULT,CW_USEDEFAULT,CW_USEDEFAULT,NULL,NULL,NULL,NULL);
  return (hWnd!=NULL);
}

// ----- executed when jvm loads library

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* jvm, void* /*reserved*/){
  gjvm=jvm;
//  we'll load this when we really need it [2007-07-07]
//  if(!loadTwainLib()){return JNI_ERR;}
  return JNI_VERSION_1_4;	                            // might work with lower versions; only tested with 1.4 though
}

JNIEXPORT void JNICALL JNI_OnUnLoad(JavaVM* pjvm, void* /*reserved*/){
  if(callDSMEntry(attachCurrentThread(),NULL,DG_CONTROL,DAT_PARENT,MSG_CLOSEDSM,(jbyte*)&hWnd)!=TWRC_SUCCESS){
//    fprintf(stderr,"jtwain.cpp: [DG_CONTROL/DAT_PARENT/MSG_CLOSEDSM]: TWRC_FAILURE\n");
  }
}

JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ninitLib(JNIEnv* env, jclass clazz){
  jtwain   = (jclass)env->NewGlobalRef(clazz);
  jexecute = env->GetStaticMethodID(jtwain,"cbexecute","(Ljava/lang/Object;I)V");
  if((jexecute!=NULL) 
    && createWindow() 
//    && openDataSourceManager()
  ){
    return (int)hWnd;
  }
  return NULL;  
}

JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nstart(JNIEnv* env,jclass clazz){
  MSG       msg ={0};

  jmethodID cbhandleGetMessage =env->GetStaticMethodID(jtwain,"cbhandleGetMessage","(I)I");
  if(cbhandleGetMessage!=NULL){
    while(GetMessage((LPMSG)&msg, NULL, 0, 0)){
      jint rc=env->CallStaticIntMethod(jtwain,cbhandleGetMessage,(int)&msg);
      while(env->ExceptionCheck()){env->ExceptionClear();} // this runs in native thread, ALWAYS DO CLEAR Exceptions
      if(rc!=TWRC_DSEVENT){                                // if event wasn't handled by source let windows handle it
        TranslateMessage((LPMSG)&msg);
        DispatchMessage((LPMSG)&msg);
      }
    }
  }
}

// java virtual machine -> native: call only within native thread !!

JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSourceManager(JNIEnv* env,jclass clazz, 
    jint dg, jint dat, jint msg, jbyteArray jbuf
){
  int rc=TWRC_FAILURE;
  if(jbuf==NULL){
    rc=callDSMEntry(env,NULL,dg,dat,msg,NULL);
  }else{
    jbyte* cbuf=env->GetByteArrayElements(jbuf,NULL);
    if(cbuf!=NULL){
      if((dg==DG_CONTROL)&&(dat==DAT_IDENTITY)&&(msg==MSG_USERSELECT)){  // show user select dialog
        SetForegroundWindow(hWnd);
      }
      rc=callDSMEntry(env,NULL,dg,dat,msg,cbuf);
      env->ReleaseByteArrayElements(jbuf,cbuf,0);
    }
  }
  return rc;
}

JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSource(JNIEnv* env, jclass clazz, 
    jbyteArray jsource, jint dg, jint dat, jint msg, jbyteArray jbuf
){
  if(jsource==NULL){
    return Java_uk_co_mmscomputing_device_twain_jtwain_ncallSourceManager(env,clazz,dg,dat,msg,jbuf);
  }

  int      rc     =TWRC_FAILURE;
  jbyte*   csource=env->GetByteArrayElements(jsource,NULL);
  if(csource!=NULL){
    if(jbuf==NULL){
      rc=callDSMEntry(env,csource,dg,dat,msg,NULL);
    }else{
      if((dg==DG_CONTROL)&&(dat==DAT_USERINTERFACE)&&(msg==MSG_ENABLEDS)){  // show scan dialog
        SetForegroundWindow(hWnd);
      }
      jbyte* cbuf=env->GetByteArrayElements(jbuf,NULL);
      if(cbuf!=NULL){
        rc=callDSMEntry(env,csource,dg,dat,msg,cbuf);
        env->ReleaseByteArrayElements(jbuf,cbuf,0);
      }
    }
    env->ReleaseByteArrayElements(jsource,csource,JNI_ABORT);  // JNI_ABORT: don't copy back, it's not necessary
  }
  return rc;
}

// capabilities: get/set container

static DCItemSize[]={
  sizeof(TW_INT8),sizeof(TW_INT16),sizeof(TW_INT32),
  sizeof(TW_UINT8),sizeof(TW_UINT16),sizeof(TW_UINT32),
  sizeof(TW_BOOL),sizeof(TW_FIX32),sizeof(TW_FRAME),
  sizeof(TW_STR32),sizeof(TW_STR64),sizeof(TW_STR128),sizeof(TW_STR255),
  sizeof(TW_STR1024),sizeof(TW_UNI512),
};

JNIEXPORT jbyteArray JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ngetContainer(JNIEnv* env, jclass clazz, 
    jint type, jint cbuf
){
  if(cbuf==0){return NULL;}

  jbyteArray jbuf=NULL;
  switch(type){
  case TWON_ARRAY:{
    TW_ARRAY* array=(TW_ARRAY*)GlobalLock((void*)cbuf);
    if(array!=NULL){
      int size;
      if(array->ItemType<=TWTY_UNI512){
        size=6+array->NumItems*DCItemSize[array->ItemType];
      }else{                                         // unknown item type
        size=6;array->NumItems=0;
      }
      jbuf=env->NewByteArray(size);
      if(jbuf!=NULL){
        env->SetByteArrayRegion(jbuf,0,size,(jbyte*)array);
      }
      GlobalUnlock((void*)cbuf);
    }
   }break;
  case TWON_ENUMERATION:{
    TW_ENUMERATION* enumeration=(TW_ENUMERATION*)GlobalLock((void*)cbuf);
    if(enumeration!=NULL){
      int size;
      if(enumeration->ItemType<=TWTY_UNI512){
        size=14+enumeration->NumItems*DCItemSize[enumeration->ItemType];
      }else{                                         // unknown item type
        size=14;enumeration->NumItems=0;
      }
      jbuf=env->NewByteArray(size);
      if(jbuf!=NULL){
        env->SetByteArrayRegion(jbuf,0,size,(jbyte*)enumeration);
      }
      GlobalUnlock((void*)cbuf);
    }
   }break;
  case TWON_ONEVALUE:{                            // TW_ONEVALUE 6 bytes
    TW_ONEVALUE* onevalue=(TW_ONEVALUE*)GlobalLock((void*)cbuf);
    if(onevalue!=NULL){
      jbuf=env->NewByteArray(sizeof(TW_ONEVALUE));
      if(jbuf!=NULL){
        env->SetByteArrayRegion(jbuf,0,sizeof(TW_ONEVALUE),(jbyte*)onevalue);
      }
      GlobalUnlock((void*)cbuf);
    }
   }break;
  case TWON_RANGE:{                               // TW_RANGE   22 bytes
    TW_RANGE* range=(TW_RANGE*)GlobalLock((void*)cbuf);
    if(range!=NULL){
      jbuf=env->NewByteArray(sizeof(TW_RANGE));
      if(jbuf!=NULL){
        env->SetByteArrayRegion(jbuf,0,sizeof(TW_RANGE),(jbyte*)range);
      }
      GlobalUnlock((void*)cbuf);
    }
   }break;
  }
  GlobalFree((void*)cbuf);
  return jbuf;
}

JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nsetContainer(JNIEnv* env, jclass clazz, 
  jint type, jbyteArray jbuf
){
  if(jbuf==NULL){return 0;}
  int    len        = env->GetArrayLength(jbuf);
  if(len==0){return 0;}
  HANDLE container  = GlobalAlloc(GHND,len);
  if(container!=NULL){
    jbyte* nbuf     = (jbyte*)GlobalLock(container);
    if(nbuf!=NULL){
      jbyte* cbuf   = env->GetByteArrayElements(jbuf,NULL);
      if(cbuf!=NULL){
        memcpy(nbuf,cbuf,len);
      }
      GlobalUnlock(container);
    }
  }
  return (jint)container;
}

JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nfree(JNIEnv* env, jclass clazz, 
    jint handle
){
  if(handle!=0){GlobalFree((void*)handle);}
}

JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nnew(JNIEnv* env, jclass clazz,
    jbyteArray jbuf,jint len){

  if((jbuf!=NULL)&&(env->GetArrayLength(jbuf)==(jint)sizeof(TW_IMAGEMEMXFER))){    
    jbyte* cbuf=env->GetByteArrayElements(jbuf,NULL);
    if(cbuf!=NULL){
      HANDLE buffer = GlobalAlloc(GHND,len);
      if(buffer!=NULL){
        TW_IMAGEMEMXFER* imx=(TW_IMAGEMEMXFER*)cbuf;
        imx->Memory.Flags  = TWMF_APPOWNS | TWMF_HANDLE;
        imx->Memory.Length = len;
        imx->Memory.TheMem = buffer;
      }else{
        JNU_ThrowByName(env,"java/lang/OutOfMemoryError","jtwain.cpp [new] : Out of memory.");
      }
      env->ReleaseByteArrayElements(jbuf,cbuf,0);
    }
  }
}

JNIEXPORT int JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncopy(JNIEnv* env, jclass clazz, 
    jbyteArray jbuf, jbyteArray jimx, jint size){

  if((jbuf==NULL)||(jimx==NULL)){ 
    JNU_ThrowByName(env,"java/lang/NullPointerException","jtwain.cpp: ncopy: null pointer exception.");return -1;
  }
  int len=env->GetArrayLength(jbuf);                    // assume: len does match SETUPMEMXFER size!
  if((len==0)||(len<size)||(env->GetArrayLength(jimx)!=(jint)sizeof(TW_IMAGEMEMXFER))){    
    JNU_ThrowByName(env,"java/lang/IllegalArgumentException","jtwain.cpp: ncopy: Illegal argument.");return -1;
  }
  len=-1;
  TW_IMAGEMEMXFER* cimx=(TW_IMAGEMEMXFER*)env->GetByteArrayElements(jimx,NULL);
  if(cimx!=NULL){
    HANDLE handle = (HANDLE)cimx->Memory.TheMem;
    if(handle==NULL){
      JNU_ThrowByName(env,"java/lang/IllegalArgumentException","jtwain.cpp: ncopy: Illegal argument.");
    }else{
      jbyte* cbuf=(jbyte*)GlobalLock(handle);
      if(cbuf==NULL){
        JNU_ThrowByName(env,"java/lang/OutOfMemoryError","jtwain.cpp: ncopy: Cannot lock handle.");
      }else{
        env->SetByteArrayRegion(jbuf,0,size,cbuf);
        GlobalUnlock(handle);
        len=size;
      }
    }
  }
  env->ReleaseByteArrayElements(jimx,(jbyte*)cimx,0);
  return len;
}
/*
JNIEXPORT jbyteArray JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncopy(JNIEnv* env, jclass clazz,
    jbyteArray jbuf,jint len){

  jbyteArray jmembuf=NULL;
  if((len>0)&&(jbuf!=NULL)&&(env->GetArrayLength(jbuf)==(jint)sizeof(TW_IMAGEMEMXFER))){    
    jbyte* cbuf=env->GetByteArrayElements(jbuf,NULL);
    if(cbuf!=NULL){
      TW_IMAGEMEMXFER* imx    = (TW_IMAGEMEMXFER*)cbuf;
      HANDLE           handle = (HANDLE)imx->Memory.TheMem;
      if(handle!=NULL){
        jbyte* cmembuf=(jbyte*)GlobalLock(handle);
        if(cmembuf!=NULL){
          jmembuf=env->NewByteArray(len);
          if(jmembuf!=NULL){
            env->SetByteArrayRegion(jmembuf,0,len,cmembuf);
          }
          GlobalUnlock(handle);
        }
      }
      env->ReleaseByteArrayElements(jbuf,cbuf,0);
    }    
  }
  return jmembuf;
}
*/
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ndelete(JNIEnv* env, jclass clazz,
    jbyteArray jbuf){
  
  if((jbuf!=NULL)&&(env->GetArrayLength(jbuf)==(jint)sizeof(TW_IMAGEMEMXFER))){    
    jbyte* cbuf=env->GetByteArrayElements(jbuf,NULL);
    if(cbuf!=NULL){
      TW_IMAGEMEMXFER* imx    = (TW_IMAGEMEMXFER*)cbuf;
      HANDLE           handle = (HANDLE)imx->Memory.TheMem;
      if(handle!=NULL){
        GlobalFree((void*)handle);
        imx->Memory.TheMem=NULL;
      }
      env->ReleaseByteArrayElements(jbuf,cbuf,0);
    }    
  }
}

int WINAPI DllEntryPoint(HINSTANCE hInstance, unsigned long reason, void*){
// fprintf(stderr,"%d\n",sizeof(TW_IMAGEMEMXFER));
  return 1;
}

//      if((dg==DG_IMAGE)&&(dat==DAT_IMAGEMEMXFER)&&(msg==MSG_GET)){cbuf=NULL;return cbuf[0];}
/*
  char str[256]={0};
  sprintf(str,"END MESSAGELOOP 0x%X",hWnd);
  MessageBox(NULL,str,"jtwain",MB_OK);
*/
