package ieci.tecdoc.sbo.fss.core;

import ieci.tecdoc.core.centera.CenteraBasicFns;
import ieci.tecdoc.core.centera.CenteraCnnCfg;
import ieci.tecdoc.core.datetime.DateTimeUtil;
import ieci.tecdoc.core.db.DBSessionManager;
import ieci.tecdoc.core.exception.IeciTdException;
import ieci.tecdoc.core.file.FileManager;
import ieci.tecdoc.core.ftp.FtpBasicFns;
import ieci.tecdoc.core.ftp.FtpConnectionInfo;
import ieci.tecdoc.core.ftp.FtpTransferFns;
import ieci.tecdoc.core.textutil.Util;
import ieci.tecdoc.core.types.IeciTdType;
import ieci.tecdoc.sbo.config.CfgFssMdoFile;
import ieci.tecdoc.sbo.fss.core.CfgMdoFile.RepositoryConfig;
import ieci.tecdoc.sbo.util.nextid.NextId;
import ieci.tecdoc.sgm.base.collections.IeciTdLongIntegerArrayList;
import ieci.tecdoc.sgm.base.dbex.DbConnection;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public final class FssMdoFile
{

   //~ Static fields/initializers ---------------------------------------------

   private static final Logger log = Logger.getLogger("xml");

   private static final String CONFIG_FILE = "IeciTd_Fss_Config.xml";
   private static boolean configInitialized = false;
   private static Map configurations;
   
   //~ Constructors -----------------------------------------------------------

   private FssMdoFile()
   {

   }

   //~ Methods ----------------------------------------------------------------

   public static byte[] retrieveFile(DbConnection dbConn, int fileId, String entidad)
      throws Exception
   {

      byte[]         fileCont = null;
      FssDaoFileRecA fileRecA = null;
      FssDaoVolRecA  volRecA = null;
      FssDaoRepRecA  repRecA = null;
      char           sep;
      //int            os;
      String         fileFullPath;
      FssRepInfo     repInfo = null;
      FssVolInfo     volInfo = null;

      fileRecA = FssDaoFileTbl.selectRecA(dbConn, fileId);

      volRecA = FssDaoVolTbl.selectRecA(dbConn, fileRecA.m_volId);
      checkVolIsReady(volRecA.m_stat);

      repRecA = FssDaoRepTbl.selectRecA(dbConn, volRecA.m_repId);
      checkRepIsNotOffLine(repRecA.m_stat);
      
      repInfo = calculateRepInfo(repRecA);
      volInfo = FssMdoUtil.decodeVolInfo(volRecA.m_info);

      sep = FssMdoUtil.getFileNameSeparator(repInfo.m_os);

      if (repInfo.m_type != FssMdoUtil.RT_DB && repInfo.m_type != FssMdoUtil.RT_CENTERA)
      {
         fileFullPath = repInfo.m_path + sep + volInfo.m_path + sep
         					+ fileRecA.m_loc;
         fileCont = readFile(repInfo, fileFullPath);
      }
      else if (repInfo.m_type == FssMdoUtil.RT_DB)
         fileCont = getDBFile(repInfo, fileRecA.m_loc, fileRecA.m_volId, entidad);
      else
         fileCont = readCenteraFile(repInfo,fileRecA.m_loc,(int)fileRecA.m_size);

      return fileCont;

   }
   
   //Si el fichero no tiene b�squeda en contenido de documento se pasa
   //ftsInfo como null
   public static int storeFileInVolume(DbConnection dbConn, int volId, FssFileInfo info, 
                                       FssFtsInfo ftsInfo,
                                       byte[] fileCont, String entidad) throws Exception
   {

      int              fileId      = IeciTdType.NULL_LONG_INTEGER;
      FssDaoVolRecA    volRecA     = null;
      FssDaoRepRecA    repRecA     = null;
      char             sep;
      FssRepInfo       repInfo     = null;
      FssVolInfo       volInfo     = null;
      FssDaoFileRecAc  fileRecAc   = null;
      FssDaoFtsRecAc   ftsRecAc    = null;
      FssDaoActDirRecA actDirRecAc = null;
      String           dirPath     = null;
      String           fileFullPath = "";
      String           dirFullPath;
      int              fileSize;
      double           volMaxSize = 0;
      
      if ( ((info.m_flags & FssMdoUtil.FILE_FLAG_FTS) != 0) &&
           (ftsInfo == null) )
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

      volRecA = FssDaoVolTbl.selectRecA(dbConn, volId);
      volInfo = FssMdoUtil.decodeVolInfo(volRecA.m_info);

      fileSize = fileCont.length;

      
      if (volInfo.m_repType != FssMdoUtil.RT_CENTERA)
      {
         volMaxSize = Double.parseDouble(volInfo.m_maxSize);         
      }
      
      checkCanStoreInVol(volInfo.m_repType,volRecA.m_stat, volMaxSize,
          	Double.parseDouble(volRecA.m_actSize), fileSize);
      
      repRecA = FssDaoRepTbl.selectRecA(dbConn, volRecA.m_repId);

      checkCanStoreInRep(repRecA.m_stat);

      repInfo = calculateRepInfo(repRecA);
      sep = FssMdoUtil.getFileNameSeparator(repInfo.m_os);

      actDirRecAc = FssDaoActDirTbl.selectRecA(dbConn, volId);
      dirPath = Util.formatLongInteger(actDirRecAc.m_actDir);
      
      fileRecAc    = fillRecAc(repInfo.m_type, volId, dirPath, sep, info, fileSize, entidad);      
      
      if (repInfo.m_type != FssMdoUtil.RT_CENTERA)
      {
         fileFullPath = repInfo.m_path + sep + volInfo.m_path + sep
         					+ fileRecAc.m_loc;
                     
         if ( ((info.m_flags & FssMdoUtil.FILE_FLAG_FTS) != 0) || repInfo.m_type == FssMdoUtil.RT_DB)
         {  
            if (repInfo.m_type == FssMdoUtil.RT_DB)
            {
               ftsRecAc = fillDBFtsRecAc(fileRecAc, info);
            }
            else
            {
               ftsRecAc = fillFtsRecAc(fileRecAc, info, ftsInfo, repRecA.m_type,
                     						sep, fileFullPath);
            }
         }

         dirFullPath = repInfo.m_path + sep + volInfo.m_path + sep + dirPath;
         if (repInfo.m_type != FssMdoUtil.RT_DB)
         {
	         try
	         {
	
	            createDirectory(repInfo, dirFullPath);
	
	         }
	         catch (Exception e)
	         {
	         	 //[Manu Ticket #826] SIGEM La aplicaci�n deja miles de archivos abandonados en la carpeta temporary y el log est� saturado de mensajes. Apadrina un fichero
	        	 //log.warn(e.getMessage());
	        	 if(e instanceof IeciTdException)
	        		 if(((IeciTdException) e).getErrorCode().equals(FssError.EM_VOL_IS_FULL) || ((IeciTdException) e).getErrorCode().equals(FssError.EM_VOL_IS_READONLY))
	        			 log.info(e.getMessage());
	        		 else
	        			 log.error(e.getMessage());
	        	 else log.error(e.getMessage());
	         	//[Manu Ticket #826] SIGEM La aplicaci�n deja miles de archivos abandonados en la carpeta temporary y el log est� saturado de mensajes. Apadrina un fichero
	         }
	         
	         writeFile(repInfo, fileFullPath, fileCont);
         }
      }
      else if (repInfo.m_type == FssMdoUtil.RT_CENTERA)
      {
         fileRecAc.m_loc = writeCenteraFile(repInfo,fileCont);
      }         
      
      //Se pasa fileCont pues si el tipo de repositorio es DB hay que hacerlo
      //dentro de la misma transacci�n
      reflectStoreInTables(repInfo.m_type, volId, actDirRecAc.m_actDir, fileSize, volMaxSize,
                           fileRecAc, ftsRecAc, info.m_ext,fileCont, entidad);

      fileId = fileRecAc.m_id;

      return fileId;

   }
   
   public static int storeFileInList(DbConnection dbConn, int listId, FssFileInfo info,
                                     FssFtsInfo ftsInfo,
                                     byte[] fileCont, String entidad)throws Exception
   {

      IeciTdLongIntegerArrayList volIds;
      int                        i;
      int                        fileId = IeciTdType.NULL_LONG_INTEGER;
      boolean                    store = false;
      
      if ( ((info.m_flags & FssMdoUtil.FILE_FLAG_FTS) != 0) &&
           (ftsInfo == null) )
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

      volIds = FssDaoListVolTbl.selectVolIds(dbConn, listId);

      if (volIds.count() == 0)
      {
         throw new IeciTdException(FssError.EC_NO_VOLS_IN_LIST,
                                   FssError.EM_NO_VOLS_IN_LIST);
      }                 

      for (i = 0; i < volIds.count(); i++)
      {

         try
         {

            fileId = storeFileInVolume(dbConn, volIds.get(i), info, ftsInfo, fileCont, entidad);

            store = true;

            break;

         }
         catch (Exception e)
         {
        	 //[Manu Ticket #826] SIGEM La aplicaci�n deja miles de archivos abandonados en la carpeta temporary y el log est� saturado de mensajes. Apadrina un fichero
        	 //log.warn(e.getMessage());
        	 if(e instanceof IeciTdException)
        		 if(((IeciTdException) e).getErrorCode().equals(FssError.EM_VOL_IS_FULL) || ((IeciTdException) e).getErrorCode().equals(FssError.EM_VOL_IS_READONLY))
        			 log.info(e.getMessage());
        		 else
        			 log.error(e.getMessage());
        	 else log.error(e.getMessage());
         	 //[Manu Ticket #826] SIGEM La aplicaci�n deja miles de archivos abandonados en la carpeta temporary y el log est� saturado de mensajes. Apadrina un fichero
         }

      }

      if (!store)
      {

         throw new IeciTdException(FssError.EC_UNABLE_TO_STORE_IN_LIST,
                                   FssError.EM_UNABLE_TO_STORE_IN_LIST);
      }

      return fileId;

   }

   public static void deleteFile(DbConnection dbConn, int fileId, String entidad) throws Exception
   {

      //FssDaoFileRecUc rec;
      //int             volId;
      char            sep;
      String          fileFullPath;
      FssDaoFileRecA  fileRecA = null;
      FssDaoVolRecA   volRecA  = null;
      FssDaoRepRecA   repRecA  = null;
      FssRepInfo      repInfo  = null;
      FssVolInfo      volInfo  = null;
      int             fileSize;
      double          volMaxSize = 0;
      boolean         isFts;

      fileRecA = FssDaoFileTbl.selectRecA(dbConn, fileId);

      volRecA = FssDaoVolTbl.selectRecA(dbConn, fileRecA.m_volId);
      checkCanDeleteInVol(volRecA.m_stat);

      repRecA = FssDaoRepTbl.selectRecA(dbConn, volRecA.m_repId);
      checkCanDeleteInRep(repRecA.m_stat);

      repInfo = calculateRepInfo(repRecA);
      volInfo = FssMdoUtil.decodeVolInfo(volRecA.m_info);
      
      if (repInfo.m_type != FssMdoUtil.RT_CENTERA)
         volMaxSize = Double.parseDouble(volInfo.m_maxSize);

      sep = FssMdoUtil.getFileNameSeparator(repInfo.m_os);
      fileFullPath = repInfo.m_path + sep + volInfo.m_path + sep
                     + fileRecA.m_loc;

      if (repInfo.m_type != FssMdoUtil.RT_DB && repInfo.m_type != FssMdoUtil.RT_CENTERA)
         fileSize = getFileSize(repInfo, fileFullPath);
      else
         fileSize = (int)fileRecA.m_size;

      if ( (fileRecA.m_flags & FssMdoUtil.FILE_FLAG_FTS) != 0 )
         isFts = true;
      else
         isFts = false;
      
      //En esta funci�n si el tipo de repositorio es DB se elimina
      //el fichero y tiene que estar dentro de la misma transacci�n
      reflectDeleteInTables(repInfo.m_type, fileId, fileSize, isFts, fileRecA, volMaxSize, entidad);
      
      if (repInfo.m_type != FssMdoUtil.RT_DB)
         deleteFile(repInfo, fileFullPath, fileRecA.m_loc);
      
      
   }

   public static int getFileSize(DbConnection dbConn, int fileId) throws Exception
   {

      //FssDaoFileRecUc rec;
      //int             volId;
      char            sep;
      String          fileFullPath;
      FssDaoFileRecA  fileRecA = null;
      FssDaoVolRecA   volRecA  = null;
      FssDaoRepRecA   repRecA  = null;
      FssRepInfo      repInfo  = null;
      FssVolInfo      volInfo  = null;
      int             fileSize;

      fileRecA = FssDaoFileTbl.selectRecA(dbConn, fileId);

      volRecA = FssDaoVolTbl.selectRecA(dbConn, fileRecA.m_volId);

      repRecA = FssDaoRepTbl.selectRecA(dbConn, volRecA.m_repId);

      repInfo = calculateRepInfo(repRecA);
      
      sep = FssMdoUtil.getFileNameSeparator(repInfo.m_os);

      if (repInfo.m_type != FssMdoUtil.RT_DB && repInfo.m_type != FssMdoUtil.RT_CENTERA) 
      {
    	 volInfo = FssMdoUtil.decodeVolInfo(volRecA.m_info);
         fileFullPath = repInfo.m_path + sep + volInfo.m_path + sep + fileRecA.m_loc;
         fileSize = getFileSize(repInfo, fileFullPath);
      }
      else
      {
         fileSize = (int)fileRecA.m_size;
      }

      return fileSize;
   }
   
   
   // **************************************************************************
   private static void checkVolIsReady(int volStat) throws Exception
   {

      if ((volStat & FssMdoUtil.VOL_STAT_NOT_READY) != 0)
      {
         throw new IeciTdException(FssError.EC_VOL_NOT_READY,
                                   FssError.EM_VOL_NOT_READY);
      }

   }

   private static void checkVolIsNotFull(int volStat) throws Exception
   {

      if ((volStat & FssMdoUtil.VOL_STAT_FULL) != 0)
      {
         throw new IeciTdException(FssError.EC_VOL_IS_FULL,
                                   FssError.EM_VOL_IS_FULL);
      }

   }

   private static void checkVolIsNotReadOnly(int volStat)
                       throws Exception
   {

      if ((volStat & FssMdoUtil.VOL_STAT_READONLY) != 0)
      {
         throw new IeciTdException(FssError.EC_VOL_IS_READONLY,
                                   FssError.EM_VOL_IS_READONLY);
      }

   }

   private static void checkCanStoreInVol(int repType,int volStat, double maxSize,
                                          double actSize, int fileSize)
                       throws Exception
   {

      checkVolIsNotReadOnly(volStat);
      checkVolIsReady(volStat);
      checkVolIsNotFull(volStat);
      
      if ( (repType != FssMdoUtil.RT_CENTERA)&& ((actSize + fileSize) > maxSize) )
      {
         throw new IeciTdException(FssError.EC_VOL_IS_FULL,
                                   FssError.EM_VOL_IS_FULL);
      }

   }

   private static void checkCanDeleteInVol(int volStat)
                       throws Exception
   {

      checkVolIsNotReadOnly(volStat);
      checkVolIsReady(volStat);

   }

   private static void checkRepIsNotOffLine(int repStat)
                       throws Exception
   {

      if ((repStat & FssMdoUtil.REP_STAT_OFFLINE) != 0)
      {
         throw new IeciTdException(FssError.EC_REP_OFF_LINE,
                                   FssError.EM_REP_OFF_LINE);
      }
      
   }

   private static void checkRepIsNotReadOnly(int repStat)
                       throws Exception
   {

      if ((repStat & FssMdoUtil.REP_STAT_READONLY) != 0)
      {
         throw new IeciTdException(FssError.EC_REP_IS_READONLY,
                                   FssError.EM_REP_IS_READONLY);
      }

   }

   private static void checkCanStoreInRep(int repStat)
                       throws Exception
   {

      checkRepIsNotOffLine(repStat);
      checkRepIsNotReadOnly(repStat);

   }

   private static void checkCanDeleteInRep(int repStat)
                       throws Exception
   {

      checkRepIsNotOffLine(repStat);
      checkRepIsNotReadOnly(repStat);

   }
   
   private static byte[] getDBFile(FssRepInfo repInfo, String locId, int volId, String entidad)
                         throws Exception
   {
      FssDaoVolVolRecAc recAc;
      
      if (repInfo.m_type == FssMdoUtil.RT_DB)
      {
         recAc =  FssDaoVolVolTbl.selectRecAc(locId, entidad);
         return recAc.m_fileVal;
      }
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }   
      
   }

   private static byte[] readFile(FssRepInfo repInfo, String fileFullPath)
                         throws Exception
   {

      if (repInfo.m_type == FssMdoUtil.RT_PFS)
         return FileManager.retrieveFile(fileFullPath);
      else if (repInfo.m_type == FssMdoUtil.RT_FTP)
         return readFtpFile(repInfo, fileFullPath);
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

   }
   
   private static byte[] readCenteraFile(FssRepInfo repInfo, String fileId,int fileSize)
   								throws Exception
   {
      CenteraCnnCfg cfg = new CenteraCnnCfg();
      cfg.setServer(repInfo.m_srv);
      cfg.setPort(repInfo.m_port);
      cfg.setUser(repInfo.m_usr);
      cfg.setPassword(repInfo.m_pwd);
      if (repInfo.m_flags == 0)
         cfg.setAvoidCollision(false);
      else
         cfg.setAvoidCollision(true);
      
      return (CenteraBasicFns.Centera_retrieveBytes(cfg,fileId));
   }

   private static byte[] readFtpFile(FssRepInfo repInfo, String fileFullPath)
                         throws Exception
   {

      FtpConnectionInfo ftpConnInfo;

      ftpConnInfo = FssMdoUtil.createFtpConnectionInfo(repInfo);

      return FtpTransferFns.retrieveFile(ftpConnInfo, fileFullPath);

   }

   private static int getFileSize(FssRepInfo repInfo, String fileFullPath)
                      throws Exception
   {

      if (repInfo.m_type == FssMdoUtil.RT_PFS)
         return FileManager.getFileSize(fileFullPath);
      else if (repInfo.m_type == FssMdoUtil.RT_FTP)
         return getFtpFileSize(repInfo, fileFullPath);
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

   }

   private static int getFtpFileSize(FssRepInfo repInfo, String fileFullPath)
                      throws Exception
   {

      FtpConnectionInfo ftpConnInfo;
      int fileSize = 0;
      String size = null;

      ftpConnInfo = FssMdoUtil.createFtpConnectionInfo(repInfo);

      size = FtpBasicFns.getFileSize(ftpConnInfo, fileFullPath);

      fileSize = Integer.parseInt(size);

      return fileSize;

   }
   
   private static void storeFileDB(FssDaoFtsRecAc ftsRecAc, String locId, byte[] fileCont,
                                   int fileSize, String fileExt, String entidad) throws Exception
   {      
      FssDaoVolVolRecAc VolVolRecAc;
      
            
      VolVolRecAc = new FssDaoVolVolRecAc();
         
      VolVolRecAc.m_locId = locId;
      VolVolRecAc.m_extId1 = ftsRecAc.m_extId1;
      VolVolRecAc.m_extId2 = ftsRecAc.m_extId2;
      VolVolRecAc.m_extId3 = ftsRecAc.m_extId3;
      VolVolRecAc.m_extId4 = ftsRecAc.m_extId4;
      VolVolRecAc.m_fileExt = fileExt;
      VolVolRecAc.m_fileVal = fileCont;
      VolVolRecAc.m_fileSize = fileSize;
         
      FssDaoVolVolTbl.insertRow( VolVolRecAc,fileSize, entidad);
         
      
   }

   private static void writeFile(FssRepInfo repInfo, String fileFullPath,
                                 byte[] fileCont) throws Exception
   {

      if (repInfo.m_type == FssMdoUtil.RT_PFS)
         FileManager.storeFile(fileFullPath, fileCont);
      else if (repInfo.m_type == FssMdoUtil.RT_FTP)
         writeFtpFile(repInfo, fileFullPath, fileCont);
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

   }
   
   private static void deleteCenteraFile(FssRepInfo repInfo,String fileLoc) throws Exception
   {    
      CenteraCnnCfg cfg = new CenteraCnnCfg();
      
      cfg.setServer(repInfo.m_srv);
      cfg.setPort(repInfo.m_port);
      cfg.setUser(repInfo.m_usr);
      cfg.setPassword(repInfo.m_pwd);
      if (repInfo.m_flags == 0)
         cfg.setAvoidCollision(false);
      else
         cfg.setAvoidCollision(true);
      
      CenteraBasicFns.Centera_deleteFile(cfg,fileLoc);
   }
   
   private static String writeCenteraFile(FssRepInfo repInfo,byte[] fileCont)
                       throws Exception
   {
      String        fileId;
      CenteraCnnCfg cfg = new CenteraCnnCfg();
      
      cfg.setServer(repInfo.m_srv);
      cfg.setPort(repInfo.m_port);
      cfg.setUser(repInfo.m_usr);
      cfg.setPassword(repInfo.m_pwd);
      if (repInfo.m_flags == 0)
         cfg.setAvoidCollision(false);
      else
         cfg.setAvoidCollision(true);
      
      fileId = CenteraBasicFns.Centera_storeBytes(cfg,fileCont);
      
      return fileId;
   }

   private static void createDirectory(FssRepInfo repInfo, String dirFullPath)
                       throws Exception
   {

      if (repInfo.m_type == FssMdoUtil.RT_PFS)
         FileManager.createDirectory(dirFullPath);
      else if (repInfo.m_type == FssMdoUtil.RT_FTP)
         createFtpDirectory(repInfo, dirFullPath);
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

   }

   private static void createFtpDirectory(FssRepInfo repInfo, String dirFullPath)
                       throws Exception
   {

      FtpConnectionInfo ftpConnInfo;

      ftpConnInfo = FssMdoUtil.createFtpConnectionInfo(repInfo);

      FtpBasicFns.createDirectory(ftpConnInfo, dirFullPath);

   }
   
   private static void deleteDbFile(String locId, String entidad) throws Exception
   {      
      FssDaoVolVolTbl.deleteRow(locId, entidad);
   }

   private static void deleteFile(FssRepInfo repInfo, String fileFullPath,String fileLoc)
                       throws Exception
   {

      if (repInfo.m_type == FssMdoUtil.RT_PFS)
         FileManager.deleteFile(fileFullPath);
      else if (repInfo.m_type == FssMdoUtil.RT_FTP)
         deleteFtpFile(repInfo, fileFullPath);
      else if (repInfo.m_type == FssMdoUtil.RT_CENTERA)
         deleteCenteraFile(repInfo,fileLoc);
      else
      {
         throw new IeciTdException(FssError.EC_INVALID_PARAM,
                                   FssError.EM_INVALID_PARAM);
      }

   }

   private static void deleteFtpFile(FssRepInfo repInfo, String fileFullPath)
                       throws Exception
   {

      FtpConnectionInfo ftpConnInfo;

      ftpConnInfo = FssMdoUtil.createFtpConnectionInfo(repInfo);

      FtpBasicFns.deleteFile(ftpConnInfo, fileFullPath);

   }   
   

   private static void writeFtpFile(FssRepInfo repInfo, String fileFullPath,
                                    byte[] fileCont) throws Exception
   {

      FtpConnectionInfo ftpConnInfo;

      ftpConnInfo = FssMdoUtil.createFtpConnectionInfo(repInfo);

      FtpTransferFns.storeFile(ftpConnInfo, fileFullPath, fileCont);

   }

   private static String createUniqueFileName() throws Exception
   {

      String  fileName = null;
      FssGuid guid = new FssGuid();

      fileName = guid.toString();

      return fileName;

   }

   private static FssDaoFileRecAc fillRecAc(int repType, int volId, String dirPath,
                                            char sep, FssFileInfo info, double fileSize, String entidad)
                                  throws Exception
   {

      int    fileId;
      String fileName;

      FssDaoFileRecAc rec = new FssDaoFileRecAc();

      fileId = NextId.generateNextId(FssMdoUtil.NEXT_ID_TBL_NAME,
            FssMdoUtil.NEXT_ID_TYPE_FILE, entidad);

      rec.m_id    = fileId;
      rec.m_volId = volId;

      //Coger un nombre �nico;
      fileName  = createUniqueFileName();
      if (repType != FssMdoUtil.RT_DB)
      {
         fileName  = fileName + "." + info.m_ext;
         rec.m_loc = dirPath + sep + fileName;
      }
      else
         rec.m_loc = fileName;

      rec.m_extId1 = info.m_extId1;
      rec.m_extId2 = info.m_extId2;
      rec.m_extId3 = info.m_extId3;
      rec.m_flags  = info.m_flags;

      rec.m_stat   = FssMdoUtil.STAT_DEF;

      rec.m_ts     = DateTimeUtil.getCurrentDateTime();
      
      rec.m_size = fileSize;

      return rec;

   }

   private static FssDaoVolRecUc fillVolRecUcToStoreFile(int repType, int fileSize,
                                                         double volMaxSize,
                                                          FssDaoVolRecA volRecA)
                                 throws Exception
   {

      double        actSize;
      double        newActSize;
      int           stat;
      DecimalFormat decFrmt = null;
      String        aSize;

      FssDaoVolRecUc rec = new FssDaoVolRecUc();

      actSize    = Double.parseDouble(volRecA.m_actSize);
      newActSize = actSize + fileSize;

      if (repType != FssMdoUtil.RT_CENTERA)
      {
         if (newActSize >= volMaxSize)
            stat = volRecA.m_stat | FssMdoUtil.VOL_STAT_FULL;
         else
            stat = volRecA.m_stat;
      }
      else
         stat = volRecA.m_stat;

      decFrmt = new DecimalFormat("0.");
      aSize   = decFrmt.format(newActSize);
      aSize   = aSize.replace(',', '.');

      rec.m_actSize = aSize;
      rec.m_stat    = stat;

      return rec;

   }
   
   private static String getFstPath(FssFtsInfo ftsInfo, int repType, 
                                    char fileNameSep, String fileFullPath)
                         throws Exception
   {

      String ftsPath = null;
      char   ftsSep;
      String tempFtsPath;
      int    len, idx;
      
      ftsPath = fileFullPath;
      
      ftsSep = FssMdoUtil.getFileNameSeparator(ftsInfo.m_ftsPlatform);
      
      if (ftsInfo.m_ftsPlatform == FssMdoUtil.OS_UNIX)
      {
         
         if (ftsSep != fileNameSep) //El repositorio est� en plataforma Windows
         {
            /* Si el path viene de la forma unidad_red:\dir1\...\dirn � 
             * \\unidad_red\Dir1..\Dir2, se sustituye la unidad de red por ftsRoot
             * y se cambian los separadores por el que le corresponde ('\' por '/')
             */
            
            len = fileFullPath.length();
            
            tempFtsPath = fileFullPath;
            
            if (fileFullPath.startsWith("\\\\") )
            {
               idx = fileFullPath.indexOf(fileNameSep,2);
               tempFtsPath = ftsInfo.m_ftsRoot + fileFullPath.substring(idx);
            }
            else if (fileFullPath.charAt(1) == ':')
            {
               tempFtsPath = ftsInfo.m_ftsRoot + fileFullPath.substring(2,len-1);              
            }
            
            ftsPath = tempFtsPath.replace(fileNameSep,ftsSep);           
               
         }        
         
      }
      else if ( (ftsInfo.m_ftsPlatform == FssMdoUtil.OS_NT) ||
                (ftsInfo.m_ftsPlatform == FssMdoUtil.OS_WINDOWS))
      { 
         //El repositorio est� en plataforma Unix o es ftp y hay que concatenarle
         // ftsRoot
         if ( (ftsSep != fileNameSep) || 
              (repType == FssMdoUtil.RT_FTP) ) 
         {
            /*  se cambian los separadores por el que le corresponde y 
             * se concatena FtsRoot
             */ 
             
            tempFtsPath = fileFullPath;
                       
            if ( ftsSep != fileNameSep)
               tempFtsPath = fileFullPath.replace(fileNameSep,ftsSep);            
            
            if (tempFtsPath.charAt(0) == ftsSep )
            {
               ftsPath = ftsInfo.m_ftsRoot + tempFtsPath;
            }
            else
            {
               ftsPath = ftsInfo.m_ftsRoot + ftsSep + tempFtsPath;              
            }       
             
         }   
      
      }

      return ftsPath;

   }
   
   private static FssDaoFtsRecAc fillFtsRecAc(FssDaoFileRecAc fileRecAc,
                                              FssFileInfo     info,
                                              FssFtsInfo      ftsInfo,
                                              int             repType,
                                              char            fileNameSep,
                                              String          fileFullPath)
                                 throws Exception
   {

      String         ftsPath;
      FssDaoFtsRecAc rec = new FssDaoFtsRecAc();  
      
      ftsPath = getFstPath(ftsInfo, repType, fileNameSep, fileFullPath);

      rec.m_id     = fileRecAc.m_id;
      rec.m_extId1 = info.m_extId1;
      rec.m_extId2 = info.m_extId2;
      rec.m_extId3 = info.m_extId3;
      rec.m_extId4 = info.m_extId4; 
      rec.m_path   = ftsPath;
      rec.m_ts     = fileRecAc.m_ts;      

      return rec;

   }
   
   private static FssDaoFtsRecAc fillDBFtsRecAc(FssDaoFileRecAc fileRecAc,
                                                FssFileInfo     info)
                                 throws Exception
   {
      
      FssDaoFtsRecAc rec = new FssDaoFtsRecAc();  

      rec.m_id     = fileRecAc.m_id;
      rec.m_extId1 = info.m_extId1;
      rec.m_extId2 = info.m_extId2;
      rec.m_extId3 = info.m_extId3;
      rec.m_extId4 = info.m_extId4;
      rec.m_ts     = fileRecAc.m_ts;      

      return rec;

   }

   private static void reflectStoreInTables(int repType, int volId, int actDir,
                                            int fileSize, double volMaxSize,
                                            FssDaoFileRecAc fileRecAc,
                                            FssDaoFtsRecAc ftsRecAc, String fileExt,
                                            byte[] fileCont, String entidad)
                       throws Exception
   {

      FssDaoActDirRecA actDirRecA = null;
      FssDaoVolRecA    volRecA    = null;
      FssDaoVolRecUc   volRecUc   = null;
      //String           aSize;

      DbConnection dbConn=new DbConnection();
      try
      {
    	dbConn.open(DBSessionManager.getSession(entidad));
         dbConn.beginTransaction();

         if (repType == FssMdoUtil.RT_DB)
            storeFileDB(ftsRecAc, fileRecAc.m_loc, fileCont, fileSize, fileExt, entidad);
         //Se incrementa el tama�o y n�mero de ficheros del volumen
         FssDaoVolTbl.incrementNumFiles(dbConn, volId);

         volRecA = FssDaoVolTbl.selectRecA(dbConn, volId);

         volRecUc = fillVolRecUcToStoreFile(repType, fileSize, volMaxSize, volRecA);

         FssDaoVolTbl.updateRow(dbConn, volRecUc, volId);

         //Se inserta la informaci�n del fichero
         FssDaoFileTbl.insertRow(dbConn, fileRecAc);
         
         if (ftsRecAc != null && repType != FssMdoUtil.RT_DB)
            FssDaoFtsTbl.insertRow(dbConn, ftsRecAc);

         //Se actualiza la informaci�n del directorio en curso
         if (repType != FssMdoUtil.RT_DB)
         {
            FssDaoActDirTbl.incrementNumFiles(dbConn, volId, actDir);

            actDirRecA = FssDaoActDirTbl.selectRecA(dbConn, volId);

            if ((actDirRecA.m_numFiles >= FssMdoUtil.DIR_MAX_NUM_FILES)
                  && (actDirRecA.m_actDir == actDir))

               FssDaoActDirTbl.incrementActDir(dbConn, volId);
         }

         dbConn.endTransaction(true);

      }
      catch (Exception e)
      {
    	  throw (e);
      }finally{
    	  dbConn.close();
      }

   }

   private static FssDaoVolRecUc fillVolRecUcToDeleteFile(int repType,int fileSize,
                                                          double volMaxSize,
                                                          FssDaoVolRecA volRecA)
                                 throws Exception
   {

      double        actSize;
      double        newActSize;
      int           stat;
      DecimalFormat decFrmt = null;
      String        aSize;

      FssDaoVolRecUc rec = new FssDaoVolRecUc();

      actSize    = Double.parseDouble(volRecA.m_actSize);
      newActSize = actSize - fileSize;

      if (repType != FssMdoUtil.RT_CENTERA)
      {
         if ((newActSize < volMaxSize) && 
               ((volRecA.m_stat & FssMdoUtil.VOL_STAT_FULL) != 0))
            stat = volRecA.m_stat ^ FssMdoUtil.VOL_STAT_FULL;
         else
            stat = volRecA.m_stat;
      }
      stat = volRecA.m_stat;

      decFrmt = new DecimalFormat("0.");
      aSize   = decFrmt.format(newActSize);
      aSize   = aSize.replace(',', '.');

      rec.m_actSize = aSize;
      rec.m_stat    = stat;

      return rec;

   }

   private static void reflectDeleteInTables(int repType,int fileId, int fileSize,
                                             boolean isFts, FssDaoFileRecA fileRecA, 
                                             double volMaxSize, String entidad)
                       throws Exception
   {

      FssDaoVolRecA  volRecA  = null;
      FssDaoVolRecUc volRecUc = null;
      DbConnection dbConn=new DbConnection();  
      try
      {
    	dbConn.open(DBSessionManager.getSession(entidad));
         dbConn.beginTransaction();

         FssDaoFileTbl.deleteRow(dbConn, fileId);
         
         if (isFts)
         {
            //Se borra en la fts 
            FssDaoFtsTbl.deleteRow(dbConn, fileId);
         }

         // Se decrementa el tama�o y n�mero de ficheros del volumen
         FssDaoVolTbl.decrementNumFiles(dbConn, fileRecA.m_volId);

         volRecA = FssDaoVolTbl.selectRecA(dbConn, fileRecA.m_volId);

         volRecUc = fillVolRecUcToDeleteFile(repType,fileSize, volMaxSize, volRecA);

         FssDaoVolTbl.updateRow(dbConn, volRecUc, fileRecA.m_volId);
         
         if (repType == FssMdoUtil.RT_DB)
            deleteDbFile(fileRecA.m_loc, entidad);

         dbConn.endTransaction(true);

      }
      catch (Exception e)
      {
    	  throw (e);
      }

   }

   public static FssRepInfo calculateRepInfo(FssDaoRepRecA  repRecA) throws Exception                                
   {
      FssRepInfo repInfo = null;

      repInfo = FssMdoUtil.decodeRepInfo(repRecA.m_info);

      if ( (repInfo.m_type == FssMdoUtil.RT_PFS) || (repInfo.m_type == FssMdoUtil.RT_FTP))
      {

         CfgMdoFile.RepositoryConfig repConfig = getConfig (repRecA.m_id);
         if (repConfig != null) {
         //Se busca en el fichero de configuraci�n el path y el sistema operativo
         
	         repInfo.m_type = FssMdoUtil.RT_PFS;
	         
	         repInfo.m_port = -1;
	         repInfo.m_srv = null;
	         repInfo.m_pwd = null;
	         repInfo.m_usr = null;
	         
	         repInfo.m_os = repConfig.getOperatingSystem();
	         repInfo.m_path = repConfig.getPath();
         }
      }

      return repInfo;
   }
   
   private static CfgMdoFile.RepositoryConfig getConfig (int repository) {
      
      CfgMdoFile.RepositoryConfig config;
      if ( ! configInitialized ) {
         
         synchronized (CONFIG_FILE) {
           if ( ! configInitialized ) {
		         configInitialized = true;
		         try
		         {
		            CfgMdoFile cfgMdoFile = CfgFssMdoFile.createConfigFromFile (CONFIG_FILE);
		            if (cfgMdoFile != null) 
		            {
		               List listConfig =  cfgMdoFile.getRepositoriesConfig ();
		               if (listConfig.size() > 0) 
		               {
		                  configurations = new HashMap ();
		                  
		                  for (int i = 0; i < listConfig.size (); i++) 
		                  {
		                     config = (RepositoryConfig) listConfig.get (i);
		                     configurations.put (new Integer (config.getId()), config);
		                  }
		               } 
		               else
		               {
		                  configurations = null;
		               }
		            }
		         }
		         catch (Throwable e)
		         {
		            log.warn("No se ha leido correctamente el fichero de configuraci�n " + CONFIG_FILE, e);
		         }
           }
         }
      }
      
      if (configurations != null) {
         config = (RepositoryConfig) configurations.get (new Integer (repository));
      }
      else
      {
         config = null;
      }
      return config;
   }

}

// class
