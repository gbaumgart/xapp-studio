package com.ddtek.xquery.jdbc.mysql;

import com.ddtek.xquery.jdbc.mysql.sio.ServerChannel;
import com.ddtek.xquery.jdbc.mysql.sio.ServerChannelLongDataContext;
import com.ddtek.xquery.jdbc.mysql.sio.ServerChannelOwner;
import com.ddtek.xquery.jdbc.mysql.sio.ServerExceptionFactory;
import com.ddtek.xquery.jdbc.mysqlbase.BaseClassUtility;
import com.ddtek.xquery.jdbc.mysqlbase.BaseConnection;
import com.ddtek.xquery.jdbc.mysqlbase.BaseConnectionProperties;
import com.ddtek.xquery.jdbc.mysqlbase.BaseDriverPropertyInfos;
import com.ddtek.xquery.jdbc.mysqlbase.BaseEscapeTranslator;
import com.ddtek.xquery.jdbc.mysqlbase.BaseExceptions;
import com.ddtek.xquery.jdbc.mysqlbase.BaseImplConnection;
import com.ddtek.xquery.jdbc.mysqlbase.BaseImplDatabaseMetaData;
import com.ddtek.xquery.jdbc.mysqlbase.BaseImplStatement;
import com.ddtek.xquery.jdbc.mysqlbase.BaseResultSetMetaData;
import com.ddtek.xquery.mysqlutil.UtilException;
import com.ddtek.xquery.mysqlutil.UtilSocketCreator;
import com.ddtek.xquery.mysqlutil.UtilTransliterator;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class MySQLImplConnection extends BaseImplConnection
  implements ServerExceptionFactory
{
  private static String footprint = "$Revision:   1.40.1.0  $";
  static final String PROP_PASSWORD = "password";
  static final String PROP_USER = "user";
  static final String PROP_DATABASENAME = "databaseName";
  static final String PROP_PORTNUMBER = "portNumber";
  static final String PROP_SERVERNAME = "serverName";
  static final String PROP_WIRESHARING = "resultProcessing";
  static final String[] PROP_WIRESHARING_CHOICES = { "BufferResults", "BufferInMemory" };
  static final String PROP_PRIMARYBUFFERSIZE = "primaryIOMemory";
  static final String PROP_OVERFLOWSIZE = "overflowIOMemory";
  static final String PROP_INORDERCOLUMNACCESS = "inOrderColumnAccess";
  static final String PROP_PREPAREMETHOD = "prepareMethod";
  static final String PROP_LOBCOMMANDSIZE = "lobCommandSize";
  static final String PROP_INTERACTIVECLIENT = "interactiveClient";		
  static final String PROP_SPYATTRIBUTES = "spyAttributes";
  static final String PROP_CODEPAGEOVERRIDE = "codePageOverride";
  static final String PROP_COMMANDCHARSET = "commandCharset";
  static final String PROP_OUTPARAMHANDLING = "fetchOutParams";
  static final String[] PROP_OUTPARAMHANDLING_CHOICES = { "AtExecute", "OnRequest" };
  static final String PROP_TREATBINARYASCHAR = "treatBinaryAsChar";
  ServerChannel serverChannel;
  private Socket socket;
  private InputStream socketInputStream;
  private OutputStream socketOutputStream;
  private ServerChannelOwner serverChannelOwner;
  private int channelPrimaryBufferSize;
  private int channelOverflowRamSize;
  private int wireContentionStrategy;
  private Statement utilityStatement;
  boolean saveCurrentRowForOutOfOrderAccess;
  String databaseName = null;
  static final int OPHS_NONE = 0;
  static final int OPHS_SINGLE_EXECUTE = 1;
  static final int OPHS_MULTIPLE_EXECUTE = 2;
  int outputParameterStrategy = 0;
  ServerChannelLongDataContext longDataContext;
  String serverVersion;
  int majorVersion;
  int minorVersion;
  int subMinorVersion;
  boolean version4Dot0;
  boolean version4Dot1;
  boolean version5orLater;
  boolean version5Dot1orLater;
  boolean version4Dot1Dot15;
  boolean version5Dot0Dot16;
  boolean version5Dot0Dot30OrGreater = true;
  int threadID;
  int serverVarMaxAllowedPacket = 4096;
  int serverStatus;
  int serverCapabilities;
  private int serverCharsetIndex;
  private String characterSet4dot0;
  UtilTransliterator connectionLevelTransliterator;
  private UtilTransliterator[] cachedTransliterators;
  private int[] cachedTransliteratorsMap;
  int cachedTransliteratorsMapNext;
  long clientParameters;
  boolean supportsLowerCaseTableName;
  boolean doPrepare = true;
  static final String outParamNamePrefix = "@_J.D.B.C_OUTPARAM";
  int outParamCount;
  boolean realAsFloat = false;
  boolean noBackslashEscapes = false;
  boolean treatBinaryAsChar = false;
  BaseConnection cancelConnection = null;
  private MySQLImplStatement cancelPendingStatement = null;
  boolean useSSL = false;

  public MySQLImplConnection()
  {
    this.numImplResultSetTypes = 2;
    this.connection = this.connection;
    this.intoIsOptional = true;
    this.useASCIISort = true;
  }

  protected void getImplPropertyInfo(BaseDriverPropertyInfos paramBaseDriverPropertyInfos)
  {
    paramBaseDriverPropertyInfos.put("user", "Login ID", "nobody", null, false);
    paramBaseDriverPropertyInfos.put("password", "Login Password", "", null, false);
    paramBaseDriverPropertyInfos.put("databaseName", "Database name", "", null, false);
    paramBaseDriverPropertyInfos.put("serverName", "Server name", "", null, true);
    paramBaseDriverPropertyInfos.put("portNumber", "Port number", "3306", null, false);
    paramBaseDriverPropertyInfos.put("inOrderColumnAccess", "ResultSet fields will only be accessed in column ordinal order.", "false", null, false);
    paramBaseDriverPropertyInfos.put("lobCommandSize", "Byte size to make commands for sending long data parameters.", "1048576", null, false);
    paramBaseDriverPropertyInfos.put("interactiveClient", "Connect as an interactive client which tells the server to timeout connections based on INTERACTIVE_TIMEOUT instead of WAIT_TIMEOUT.", "false", null, false);
    paramBaseDriverPropertyInfos.put("spyAttributes", "JDBC Spy configuration.", null, null, false);
    paramBaseDriverPropertyInfos.put("codePageOverride", "Code Page Override", "", null, false);
    paramBaseDriverPropertyInfos.put("treatBinaryAsChar", "Specifies how the driver returns data that the server reports as binary.", "false", null, false);
    String[] arrayOfString = { "NoEncryption", "SSL" };
    paramBaseDriverPropertyInfos.put("encryptionMethod", "Specifies the driver encryption method. The encryption method determines whether the driver encrypts and decrypts the data sent between the driver and the database server.", "NoEncryption", arrayOfString, false);
    paramBaseDriverPropertyInfos.put("trustStore", "An override of javax.net.ssl.trustStore", "", null, false);
    paramBaseDriverPropertyInfos.put("trustStorePassword", "An override of javax.net.ssl.trustStorePassword", "", null, false);
    paramBaseDriverPropertyInfos.put("keyStore", "The location of the key store used for SSL client authentication", "", null, false);
    paramBaseDriverPropertyInfos.put("keyStorePassword", "The password needed to access the contents of the key store", "", null, false);
    paramBaseDriverPropertyInfos.put("keyPassword", "The password of the key in the key store used for SSL client authentication", "", null, false);
    paramBaseDriverPropertyInfos.put("validateServerCertificate", "Specifies whether the driver will validate the server certificate returned by the database server.", "true", null, false);
    paramBaseDriverPropertyInfos.put("hostNameInCertificate", "Specifies the name the driver will use to compare with the Common Name in the certificate returned by the database server during the SSL session establishment.", "", null, false);
  }

  public BaseImplDatabaseMetaData createImplDatabaseMetaData()
    throws SQLException
  {
    return new MySQLImplDatabaseMetaData(this);
  }

  public BaseEscapeTranslator createEscapeTranslator()
    throws SQLException
  {
    MySQLEscapeTranslator localMySQLEscapeTranslator = new MySQLEscapeTranslator();
    localMySQLEscapeTranslator.setImplConnection(this);
    return localMySQLEscapeTranslator;
  }

  public boolean requiresUserId()
  {
    return false;
  }

  public ServerChannel lockServerChannel(ServerChannelOwner paramServerChannelOwner)
    throws Exception
  {
    if ((this.serverChannelOwner != null) && (!this.serverChannelOwner.releaseChannel(paramServerChannelOwner, this.wireContentionStrategy)) && (this.wireContentionStrategy == 2))
      throw this.exceptions.getException(6091);
    if (this.serverChannelOwner != null)
      this.serverChannel = new ServerChannel(this.socketInputStream, this.socketOutputStream, this.channelPrimaryBufferSize, this.channelOverflowRamSize, this.serverVarMaxAllowedPacket, this);
    this.serverChannelOwner = paramServerChannelOwner;
    return this.serverChannel;
  }

  public void unlockServerChannel(ServerChannel paramServerChannel, Object paramObject)
  {
    if (paramObject == this.serverChannelOwner)
      this.serverChannelOwner = null;
    else
      paramServerChannel.close();
  }

  public void open()
    throws SQLException
  {
    try
    {
      this.saveCurrentRowForOutOfOrderAccess = (!this.connectProps.getAsBoolean("inOrderColumnAccess"));
      this.databaseName = this.connectProps.get("databaseName");
      String str = this.connectProps.get("fetchOutParams");
      if (str == null)
        this.outputParameterStrategy = 1;
      else if (str.equalsIgnoreCase(PROP_OUTPARAMHANDLING_CHOICES[0]))
        this.outputParameterStrategy = 1;
      else if (str.equalsIgnoreCase(PROP_OUTPARAMHANDLING_CHOICES[1]))
        this.outputParameterStrategy = 2;
      else
        this.outputParameterStrategy = 1;
      setupSocket();
      this.serverChannel = new ServerChannel(this.socketInputStream, this.socketOutputStream, 4096, 0, 4096, this);
      doLoginHandshake();
      reconfigServerChannelPostLogin();
      this.connectionLevelTransliterator = getTransliterator(8);
      loadServerVariables();
      configureCharset();
      stopManualTransactionMode();
      if (!this.version5Dot1orLater)
        setTransactionIsolation(2);
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
  }

  public void close()
    throws SQLException
  {
    try
    {
      this.serverChannel.resetForNewRequest(0);
      this.serverChannel.writeByte((byte)1);
      this.serverChannel.flushWrites();
      this.serverChannel.close();
      this.serverChannel = null;
      this.socketInputStream.close();
      this.socketInputStream = null;
      this.socketOutputStream.close();
      this.socketOutputStream = null;
      this.socket.close();
      this.connectionLevelTransliterator = null;
      this.longDataContext = null;
      if (this.cancelConnection != null)
      {
        this.cancelConnection.close();
        this.cancelConnection = null;
      }
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
  }

  protected BaseImplStatement createImplStatement(int paramInt1, int paramInt2)
    throws SQLException
  {
    return new MySQLImplStatement(paramInt1, paramInt2, this.exceptions);
  }

  protected BaseResultSetMetaData createResultSetMetaData(Object paramObject, BaseExceptions paramBaseExceptions)
  {
    return new MySQLResultSetMetaData(this, paramObject, paramBaseExceptions);
  }

  public int getEmptyRowInsertSyntax()
  {
    return 2;
  }

  protected void startManualTransactionMode()
    throws SQLException
  {
    executeSQL("set autocommit=0");
  }

  protected void commitTransaction()
    throws SQLException
  {
    executeSQL("commit");
  }

  protected void rollbackTransaction()
    throws SQLException
  {
    executeSQL("rollback");
  }

  protected void rollbackTransaction(String paramString)
    throws SQLException
  {
    StringBuffer localStringBuffer = new StringBuffer("ROLLBACK TO SAVEPOINT ");
    localStringBuffer.append('`');
    localStringBuffer.append(paramString);
    localStringBuffer.append('`');
    executeSQL(localStringBuffer.toString());
  }

  protected void setSavepoint(String paramString)
    throws SQLException
  {
    StringBuffer localStringBuffer = new StringBuffer("SAVEPOINT ");
    localStringBuffer.append('`');
    localStringBuffer.append(paramString);
    localStringBuffer.append('`');
    executeSQL(localStringBuffer.toString());
  }

  protected void stopManualTransactionMode()
    throws SQLException
  {
    try
    {
      this.serverChannel.resetForNewRequest(0);
      this.serverChannel.writeByte((byte)3);
      this.serverChannel.writeString("set autocommit=1", this.connectionLevelTransliterator, false, false);
      this.serverChannel.flushWrites();
      this.serverChannel.readSuccess();
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
  }

  private void executeSQL(String paramString)
    throws SQLException
  {
    try
    {
      if (this.utilityStatement == null)
        this.utilityStatement = this.connection.createStatement();
      this.utilityStatement.execute(paramString);
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
  }

  protected int getTransactionIsolation()
    throws SQLException
  {
    Statement localStatement = null;
    ResultSet localResultSet = null;
    try
    {
      localStatement = this.connection.createStatement();
      localResultSet = localStatement.executeQuery("SELECT @@tx_isolation");
      localResultSet.next();
      String str = localResultSet.getString(1);
      int i;
      if (str.equals("READ-UNCOMMITTED"))
      {
        i = 1;
        return i;
      }
      if (str.equals("READ-COMMITTED"))
      {
        i = 2;
        return i;
      }
      if (str.equals("REPEATABLE-READ"))
      {
        i = 4;
        return i;
      }
      if (str.equals("SERIALIZABLE"))
      {
        i = 8;
        return i;
      }
    }
    finally
    {
      if (localResultSet != null)
        localResultSet.close();
      if (localStatement != null)
        localStatement.close();
    }
    return 4;
  }

  protected void setTransactionIsolation(int paramInt)
    throws SQLException
  {
    String str = null;
    switch (paramInt)
    {
    case 0:
      throw this.exceptions.getException(7004);
    case 2:
      str = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
      break;
    case 1:
      str = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
      break;
    case 4:
      str = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
      break;
    case 8:
      str = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
    case 3:
    case 5:
    case 6:
    case 7:
    }
    try
    {
      this.serverChannel.resetForNewRequest(0);
      this.serverChannel.writeByte((byte)3);
      this.serverChannel.writeString(str, this.connectionLevelTransliterator, false, false);
      this.serverChannel.flushWrites();
      this.serverChannel.readSuccess();
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
  }

  protected String getCatalog()
    throws SQLException
  {
    Statement localStatement = null;
    ResultSet localResultSet = null;
    try
    {
      localStatement = this.connection.createStatement();
      localResultSet = localStatement.executeQuery("SELECT database()");
      localResultSet.next();
      String str = localResultSet.getString(1);
      return str;
    }
    finally
    {
      if (localResultSet != null)
        localResultSet.close();
      if (localStatement != null)
        localStatement.close();
    }
    //throw SQLException();
  }

  protected void setCatalog(String paramString)
    throws SQLException
  {
    Object localObject1 = null;
    try
    {
      executeSQL("USE " + this.quotingChar + paramString + this.quotingChar);
    }
    finally
    {
      /*if (localObject1 != null)
        localObject1.close();
       */
    }
  }

  public boolean supportsCancel()
  {
    return true;
  }

  public boolean supportsQueryTimeout()
  {
    return false;
  }

  protected boolean enableXlobOnLongVarX()
  {
    return true;
  }

  public void reset()
    throws SQLException
  {
  }

  public UtilTransliterator getTransliterator(int paramInt)
    throws UtilException
  {
    if (this.cachedTransliterators == null)
    {
      this.cachedTransliterators = new UtilTransliterator[10];
      this.cachedTransliteratorsMap = new int[this.cachedTransliterators.length];
      this.cachedTransliteratorsMapNext = 0;
    }
    UtilTransliterator localUtilTransliterator = null;
    for (int i = 0; i < this.cachedTransliteratorsMapNext; i++)
    {
      if (this.cachedTransliteratorsMap[i] != paramInt)
        continue;
      localUtilTransliterator = this.cachedTransliterators[i];
    }
    if (localUtilTransliterator == null)
    {
      localUtilTransliterator = UtilTransliterator.GetNewTransliterator(mySQLCharsetIndexToJavaCharset(paramInt));
      if (this.cachedTransliteratorsMapNext < this.cachedTransliteratorsMap.length)
        this.cachedTransliterators[(this.cachedTransliteratorsMapNext++)] = localUtilTransliterator;
    }
    return localUtilTransliterator;
  }

  private void setupSocket()
    throws Exception
  {
    try
    {
      this.socket = UtilSocketCreator.getSocket(this.connectProps.get("serverName"), Integer.parseInt(this.connectProps.get("portNumber")), this.connectProps.getProperties());
    }
    catch (Exception localException)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = localException.getMessage();
      throw this.exceptions.getException(6001, arrayOfString, "08001");
    }
    String str = this.connectProps.get("primaryIOMemory");
    if (str == null)
      this.channelPrimaryBufferSize = 65535;
    else
      this.channelPrimaryBufferSize = Integer.parseInt(str);
    if (this.channelPrimaryBufferSize < 4)
      this.channelPrimaryBufferSize = 4;
    UtilSocketCreator.applySocketBufferSizeOptions(this.socket, this.connectProps.getProperties(), this.channelPrimaryBufferSize, this.channelPrimaryBufferSize);
    this.socketInputStream = this.socket.getInputStream();
    this.socketOutputStream = this.socket.getOutputStream();
  }

  
  private void doLoginHandshake()
    throws Exception
  {
	  UtilTransliterator utiltransliterator = UtilTransliterator.GetNewTransliterator("ASCII");
      String s = connectProps.get("encryptionMethod").toUpperCase();
      if(s != null && s.equals("SSL"))
          useSSL = true;
      
      //useSSL=true;
      
      serverChannel.readByte();
      serverVersion = serverChannel.readString(null, -1L);
      parseServerVersion(serverVersion);
      if(!version5orLater)
          throw exceptions.getException(7014);
      threadID = (int)serverChannel.readInt32();
      String s1 = serverChannel.readString(null, -1L);
      String s2 = s1;
      serverCapabilities = serverChannel.readInt16();
      serverCharsetIndex = serverChannel.readInt8();
      serverStatus = serverChannel.readInt16();
      if(version4Dot1 || version5orLater)
      {
          serverChannel.discardBytes(13);
          String s3 = serverChannel.readString(null, -1L);
          StringBuffer stringbuffer = new StringBuffer(20);
          stringbuffer.append(s1);
          stringbuffer.append(s3);
          s1 = stringbuffer.toString();
          clientParameters |= 512L;
          clientParameters |= 8192L;
          clientParameters |= 0x20000L;
          clientParameters |= 0x10000L;
      } else
      {
          serverChannel.discardRemainderOfPacket();
      }
      if((serverCapabilities & 0x800) == 0 && useSSL)
      {
          socketInputStream.close();
          socketInputStream = null;
          socketOutputStream.close();
          socketOutputStream = null;
          socket.close();
          if(connection.getConnectPropList().size() == 1)
              connection.setMaxConnectAttempts(1);
          throw exceptions.getException(7016, "08001");
      }
      if((serverCapabilities & 4) != 0)
          clientParameters |= 4L;
      clientParameters |= 2L;
      if(connectProps.getAsBoolean("interactiveClient"))
          clientParameters |= 1024L;
      serverChannel.resetForNewRequest(1);
      String s4 = connectProps.get("user");
      String s5 = connectProps.get("password");
      if(databaseName != null && databaseName.length() > 0)
          clientParameters |= 8L;
      clientParameters |= 1L;
      if((serverCapabilities & 0x8000) != 0)
      {
          clientParameters |= 32768L;
          if(useSSL)
          {
              clientParameters |= 2048L;
              String as[] = {
                  "TLSv1"
              };
              serverChannel.writeInt32(clientParameters);
              serverChannel.flushWrites();
              socket = UtilSocketCreator.getSSLSocket(connectProps.get("serverName"), Integer.parseInt(connectProps.get("portNumber")), socket, false, as, connectProps.getProperties());
              UtilSocketCreator.applySocketBufferSizeOptions(socket, connectProps.getProperties(), channelPrimaryBufferSize, channelPrimaryBufferSize);
              socketInputStream = socket.getInputStream();
              socketOutputStream = socket.getOutputStream();
              serverChannel.setDataInputStream(socketInputStream);
              serverChannel.setDataOutputStream(socketOutputStream);
              serverChannel.resetForNewRequest(2);
          }
          serverChannel.writeInt32(clientParameters);
          serverChannel.writeInt32(0xffffffL);
          serverChannel.writeByte((byte)8);
          byte abyte0[] = {
              0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
              0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
              0, 0, 0
          };
          serverChannel.writeBytes(abyte0, 0, 23, false);
          serverChannel.writeString(s4, utiltransliterator, false, true);
          if(s5.length() == 0)
          {
              serverChannel.writeByte((byte)0);
          } else
          {
              serverChannel.writeByte((byte)20);
              MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
              byte abyte1[] = messagedigest.digest(s5.getBytes("US-ASCII"));
              messagedigest.reset();
              byte abyte2[] = messagedigest.digest(abyte1);
              messagedigest.reset();
              byte abyte3[] = s1.getBytes("US-ASCII");
              messagedigest.update(abyte3);
              messagedigest.update(abyte2);
              byte abyte4[] = messagedigest.digest();
              int i = abyte4.length;
              for(int j = 0; j < i; j++)
                  abyte4[j] = (byte)(abyte4[j] ^ abyte1[j]);

              serverChannel.writeBytes(abyte4, 0, abyte4.length, false);
          }
      } else
      {
          if(useSSL)
          {
              clientParameters |= 2048L;
              String as1[] = {
                  "TLSv1"
              };
              serverChannel.writeInt16((int)clientParameters);
              serverChannel.flushWrites();
              socket = UtilSocketCreator.getSSLSocket(connectProps.get("serverName"), Integer.parseInt(connectProps.get("portNumber")), socket, false, as1, connectProps.getProperties());
              UtilSocketCreator.applySocketBufferSizeOptions(socket, connectProps.getProperties(), channelPrimaryBufferSize, channelPrimaryBufferSize);
              socketInputStream = socket.getInputStream();
              socketOutputStream = socket.getOutputStream();
              serverChannel.setDataInputStream(socketInputStream);
              serverChannel.setDataOutputStream(socketOutputStream);
              serverChannel.resetForNewRequest(2);
          }
          serverChannel.writeInt16((int)clientParameters);
          serverChannel.writeInt24(0xffffff);
          serverChannel.writeString(s4, utiltransliterator, false, true);
          serverChannel.writeString(encrypt(s5, s1), utiltransliterator, false, true);
      }
      if((serverCapabilities & 8) != 0 && databaseName.length() > 0)
          serverChannel.writeString(databaseName, utiltransliterator, false, true);
      serverChannel.flushWrites();
      serverChannel.checkForErrorInNextPacket = true;
      byte byte0 = serverChannel.readByte();
      if(byte0 == -2)
      {
          serverChannel.resetForNewRequest(3);
          serverChannel.writeString(encrypt(s5, s2), utiltransliterator, false, true);
          serverChannel.flushWrites();
        	  
          serverChannel.readSuccess();
        
          
      } else
      {
          serverChannel.discardRemainderOfPacket();
      }
      String s6 = connectProps.get("prepareMethod");
      if(s6 != null && s6.equalsIgnoreCase("direct"))
          doPrepare = false;
  }

  private void reconfigServerChannelPostLogin()
    throws Exception
  {
    String str1 = this.connectProps.get("treatBinaryAsChar");
    this.treatBinaryAsChar = Boolean.valueOf(str1).booleanValue();
    String str2 = this.connectProps.get("resultProcessing");
    if (str2 == null)
      this.wireContentionStrategy = 1;
    else if (str2.equalsIgnoreCase(PROP_WIRESHARING_CHOICES[1]))
      this.wireContentionStrategy = 2;
    else
      this.wireContentionStrategy = 1;
    String str3 = this.connectProps.get("overflowIOMemory");
    if (str3 == null)
      this.channelOverflowRamSize = 131070;
    else
      this.channelOverflowRamSize = Integer.parseInt(str3);
    if (this.channelOverflowRamSize < this.channelPrimaryBufferSize)
      this.channelOverflowRamSize = this.channelPrimaryBufferSize;
    this.serverChannel.resetBufferSizes(this.channelPrimaryBufferSize, this.channelOverflowRamSize);
  }

  private void configureCharset()
    throws Exception
  {
    try
    {
      if (this.version4Dot0)
      {
        String str1 = mySQLCharsetToJavaCharset(this.characterSet4dot0);
        this.connectionLevelTransliterator = UtilTransliterator.GetNewTransliterator(str1);
      }
      else
      {
        int i = this.serverCharsetIndex;
        int j = this.serverCharsetIndex;
        String str2 = this.connectProps.get("codePageOverride");
        if ((str2 != null) && (!str2.equals("")))
          j = javaCharsetToMySQLCharsetIndex(str2);
        if ((this.version4Dot1Dot15) || (this.version5Dot0Dot16))
          i = j;
        this.connectionLevelTransliterator = getTransliterator(i);
        this.serverChannel.resetForNewRequest(0);
        this.serverChannel.writeByte((byte)3);
        this.serverChannel.writeString("SET NAMES " + mySQLCharsetIndexToMySQLCharsetName(i), this.connectionLevelTransliterator, false, false);
        this.serverChannel.flushWrites();
        this.serverChannel.readSuccess();
        this.serverChannel.resetForNewRequest(0);
        this.serverChannel.writeByte((byte)3);
        this.serverChannel.writeString("SET character_set_results = NULL", this.connectionLevelTransliterator, false, false);
        this.serverChannel.flushWrites();
        this.serverChannel.readSuccess();
      }
    }
    catch (SQLException localSQLException)
    {
    }
  }

  static String mySQLCharsetIndexToJavaCharset(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    case 0:
      str = "ISO8859_1";
      break;
    case 1:
      str = "MYSQL_ICU_BIG5";
      break;
    case 2:
      str = "ISO8859_2";
      break;
    case 3:
      str = "ISO8859_1";
      break;
    case 4:
      str = "Cp437";
      break;
    case 5:
      str = "ISO8859_1";
      break;
    case 6:
      str = "ISO8859_1";
      break;
    case 7:
      str = "KOI8_R";
      break;
    case 8:
      str = "MYSQL_ICU_LATIN1";
      break;
    case 9:
      str = "ISO8859_2";
      break;
    case 10:
      str = "ISO8859_1";
      break;
    case 11:
      str = "US-ASCII";
      break;
    case 12:
      str = "EUC_JP";
      break;
    case 13:
      str = "SJIS";
      break;
    case 14:
      str = "Cp1251";
      break;
    case 15:
      str = "ISO8859_1";
      break;
    case 16:
      str = "MYSQL_ICU_HEBREW";
      break;
    case 17:
      str = "ISO8859_1";
      break;
    case 18:
      str = "TIS620";
      break;
    case 19:
      str = "MYSQL_ICU_EUCKR";
      break;
    case 20:
      str = "ISO8859_13";
      break;
    case 21:
      str = "ISO8859_2";
      break;
    case 22:
      str = "ISO8859_1";
      break;
    case 23:
      str = "Cp1251";
      break;
    case 24:
      str = "EUC_CN";
      break;
    case 25:
      str = "ISO8859_7";
      break;
    case 26:
      str = "Cp1250";
      break;
    case 27:
      str = "ISO8859_2";
      break;
    case 28:
      str = "MYSQL_ICU_GBK";
      break;
    case 29:
      str = "Cp1257";
      break;
    case 30:
      str = "ISO8859_9";
      break;
    case 31:
      str = "ISO8859_1";
      break;
    case 32:
      str = "ISO8859_1";
      break;
    case 33:
      str = "UTF-8";
      break;
    case 34:
      str = "ISO8859_1";
      break;
    case 35:
      str = "UTF-16BE";
      break;
    case 36:
      str = "Cp866";
      break;
    case 37:
      str = "ISO8859_1";
      break;
    case 38:
      str = "MacCentralEurope";
      break;
    case 39:
      str = "MacRoman";
      break;
    case 40:
      str = "ISO8859_1";
      break;
    case 41:
      str = "ISO8859_13";
      break;
    case 42:
      str = "ISO8859_13";
      break;
    case 43:
      str = "ISO8859_1";
      break;
    case 44:
      str = "ISO8859_1";
      break;
    case 45:
      str = "ISO8859_1";
      break;
    case 46:
      str = "ISO8859_1";
      break;
    case 47:
      str = "ISO8859_1";
      break;
    case 48:
      str = "ISO8859_1";
      break;
    case 49:
      str = "ISO8859_1";
      break;
    case 50:
      str = "ISO8859_1";
      break;
    case 51:
      str = "Cp1251";
      break;
    case 52:
      str = "Cp1251";
      break;
    case 53:
      str = "ISO8859_1";
      break;
    case 54:
      str = "ISO8859_1";
      break;
    case 55:
      str = "ISO8859_1";
      break;
    case 56:
      str = "ISO8859_1";
      break;
    case 57:
      str = "Cp1256";
      break;
    case 58:
      str = "ISO8859_1";
      break;
    case 59:
      str = "ISO8859_1";
      break;
    case 60:
      str = "ISO8859_1";
      break;
    case 61:
      str = "ISO8859_1";
      break;
    case 62:
      str = "ISO8859_1";
      break;
    case 63:
      str = "US-ASCII";
      break;
    case 64:
      str = "ISO8859_1";
      break;
    case 65:
      str = "US-ASCII";
      break;
    case 66:
      str = "Cp1250";
      break;
    case 67:
      str = "Cp1256";
      break;
    case 68:
      str = "Cp866";
      break;
    case 69:
      str = "ISO8859_1";
      break;
    case 70:
      str = "ISO8859_7";
      break;
    case 71:
      str = "ISO8859_8";
      break;
    case 72:
      str = "ISO8859_1";
      break;
    case 73:
      str = "ISO8859_1";
      break;
    case 74:
      str = "KOI8_R";
      break;
    case 75:
      str = "ISO8859_1";
      break;
    case 76:
      str = "ISO8859_1";
      break;
    case 77:
      str = "ISO8859_2";
      break;
    case 78:
      str = "ISO8859_9";
      break;
    case 79:
      str = "ISO8859_7";
      break;
    case 80:
      str = "ISO8859_1";
      break;
    case 81:
      str = "ISO8859_1";
      break;
    case 82:
      str = "ISO8859_1";
      break;
    case 83:
      str = "UTF-8";
      break;
    case 84:
      str = "Big5";
      break;
    case 85:
      str = "EUC_KR";
      break;
    case 86:
      str = "EUC_CN";
      break;
    case 87:
      str = "GBK";
      break;
    case 88:
      str = "SJIS";
      break;
    case 89:
      str = "TIS620";
      break;
    case 90:
      str = "UTF-16BE";
      break;
    case 91:
      str = "EUC_JP";
      break;
    case 92:
      str = "ISO8859_1";
      break;
    case 93:
      str = "ISO8859_1";
      break;
    case 94:
      str = "ISO8859_1";
      break;
    case 95:
    case 96:
      str = "Windows-31J";
      break;
    case 97:
    case 98:
      str = "EUC_JP_Solaris";
      break;
    default:
      str = "ISO8859_1";
    }
    return str;
  }

  static String mySQLCharsetIndexToMySQLCharsetName(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    case 0:
      str = "latin1";
      break;
    case 1:
      str = "big5";
      break;
    case 2:
      str = "czech";
      break;
    case 3:
      str = "dec8";
      break;
    case 4:
      str = "dos";
      break;
    case 5:
      str = "german1";
      break;
    case 6:
      str = "hp8";
      break;
    case 7:
      str = "koi8_ru";
      break;
    case 8:
      str = "latin1";
      break;
    case 9:
      str = "latin2";
      break;
    case 10:
      str = "swe7";
      break;
    case 11:
      str = "usa7";
      break;
    case 12:
      str = "ujis";
      break;
    case 13:
      str = "sjis";
      break;
    case 14:
      str = "cp1251";
      break;
    case 15:
      str = "danish";
      break;
    case 16:
      str = "hebrew";
      break;
    case 17:
      str = "latin1";
      break;
    case 18:
      str = "tis620";
      break;
    case 19:
      str = "euc_kr";
      break;
    case 20:
      str = "estonia";
      break;
    case 21:
      str = "hungarian";
      break;
    case 22:
      str = "koi8_ukr";
      break;
    case 23:
      str = "win1251ukr";
      break;
    case 24:
      str = "gb2312";
      break;
    case 25:
      str = "greek";
      break;
    case 26:
      str = "win1250";
      break;
    case 27:
      str = "croat";
      break;
    case 28:
      str = "gbk";
      break;
    case 29:
      str = "cp1257";
      break;
    case 30:
      str = "latin5";
      break;
    case 31:
      str = "latin1_de";
      break;
    case 32:
      str = "armscii8";
      break;
    case 33:
      str = "utf8";
      break;
    case 34:
      str = "win1250ch";
      break;
    case 35:
      str = "ucs2";
      break;
    case 36:
      str = "cp866";
      break;
    case 37:
      str = "keybcs2";
      break;
    case 38:
      str = "macce";
      break;
    case 39:
      str = "macroman";
      break;
    case 40:
      str = "pclatin2";
      break;
    case 41:
      str = "latvian";
      break;
    case 42:
      str = "latvian1";
      break;
    case 43:
      str = "maccebin";
      break;
    case 44:
      str = "macceciai";
      break;
    case 45:
      str = "maccecias";
      break;
    case 46:
      str = "maccecsas";
      break;
    case 47:
      str = "latin1bin";
      break;
    case 48:
      str = "latin1cias";
      break;
    case 49:
      str = "latin1csas";
      break;
    case 50:
      str = "cp1251bin";
      break;
    case 51:
      str = "cp1251cias";
      break;
    case 52:
      str = "cp1251csas";
      break;
    case 53:
      str = "macromanbin";
      break;
    case 54:
      str = "macromancias";
      break;
    case 55:
      str = "macromanciai";
      break;
    case 56:
      str = "macromancsas";
      break;
    case 57:
      str = "cp1256";
      break;
    case 58:
      str = "latin1";
      break;
    case 59:
      str = "latin1";
      break;
    case 60:
      str = "latin1";
      break;
    case 61:
      str = "latin1";
      break;
    case 62:
      str = "latin1";
      break;
    case 63:
      str = "binary";
      break;
    case 64:
      str = "armscii";
      break;
    case 65:
      str = "ascii";
      break;
    case 66:
      str = "cp1250";
      break;
    case 67:
      str = "cp1256";
      break;
    case 68:
      str = "cp866";
      break;
    case 69:
      str = "dec8";
      break;
    case 70:
      str = "greek";
      break;
    case 71:
      str = "hebrew";
      break;
    case 72:
      str = "hp8";
      break;
    case 73:
      str = "keybcs2";
      break;
    case 74:
      str = "koi8r";
      break;
    case 75:
      str = "koi8ukr";
      break;
    case 76:
      str = "latin1";
      break;
    case 77:
      str = "latin2";
      break;
    case 78:
      str = "latin5";
      break;
    case 79:
      str = "latin7";
      break;
    case 80:
      str = "cp850";
      break;
    case 81:
      str = "cp852";
      break;
    case 82:
      str = "swe7";
      break;
    case 83:
      str = "utf8";
      break;
    case 84:
      str = "big5";
      break;
    case 85:
      str = "euckr";
      break;
    case 86:
      str = "gb2312";
      break;
    case 87:
      str = "gbk";
      break;
    case 88:
      str = "sjis";
      break;
    case 89:
      str = "tis620";
      break;
    case 90:
      str = "ucs2";
      break;
    case 91:
      str = "ujis";
      break;
    case 92:
      str = "geostd8";
      break;
    case 93:
      str = "geostd8";
      break;
    case 94:
      str = "latin1";
      break;
    case 95:
    case 96:
      str = "cp932";
      break;
    case 97:
    case 98:
      str = "eucjpms";
      break;
    default:
      str = "latin1";
    }
    return str;
  }

  static String mySQLCharsetToJavaCharset(String paramString)
  {
    String str;
    if (paramString.equals("big5"))
      str = "Big5";
    else if (paramString.equals("czech"))
      str = "ISO8859_2";
    else if (paramString.equals("dec8"))
      str = "ISO8859_1";
    else if (paramString.equals("dos"))
      str = "Cp437";
    else if (paramString.equals("german1"))
      str = "ISO8859_1";
    else if (paramString.equals("hp8"))
      str = "ISO8859_1";
    else if (paramString.equals("koi8_ru"))
      str = "KOI8_R";
    else if (paramString.equals("latin1"))
      str = "ISO8859_1";
    else if (paramString.equals("latin2"))
      str = "ISO8859_2";
    else if (paramString.equals("swe7"))
      str = "ISO8859_1";
    else if (paramString.equals("usa7"))
      str = "US-ASCII";
    else if (paramString.equals("ujis"))
      str = "EUC_JP";
    else if (paramString.equals("sjis"))
      str = "SJIS";
    else if (paramString.equals("cp1251"))
      str = "Cp1251";
    else if (paramString.equals("danish"))
      str = "ISO8859_1";
    else if (paramString.equals("hebrew"))
      str = "ISO8859_8";
    else if (paramString.equals("tis620"))
      str = "TIS620";
    else if (paramString.equals("euc_kr"))
      str = "EUC_KR";
    else if (paramString.equals("estonia"))
      str = "ISO8859_13";
    else if (paramString.equals("hungarian"))
      str = "ISO8859_2";
    else if (paramString.equals("koi8_ukr"))
      str = "ISO8859_1";
    else if (paramString.equals("win1251ukr"))
      str = "Cp1251";
    else if (paramString.equals("gb2312"))
      str = "EUC_CN";
    else if (paramString.equals("greek"))
      str = "ISO8859_7";
    else if (paramString.equals("win1250"))
      str = "Cp1250";
    else if (paramString.equals("croat"))
      str = "ISO8859_2";
    else if (paramString.equals("gbk"))
      str = "GBK";
    else if (paramString.equals("cp1257"))
      str = "Cp1257";
    else if (paramString.equals("latin5"))
      str = "ISO8859_9";
    else if (paramString.equals("latin1_de"))
      str = "ISO8859_1";
    else if (paramString.equals("armscii8"))
      str = "ISO8859_1";
    else if (paramString.equals("utf8"))
      str = "UTF-8";
    else if (paramString.equals("win1250ch"))
      str = "ISO8859_1";
    else if (paramString.equals("ucs2"))
      str = "UTF-16BE";
    else if (paramString.equals("cp866"))
      str = "Cp866";
    else if (paramString.equals("keybcs2"))
      str = "ISO8859_1";
    else if (paramString.equals("macce"))
      str = "MacCentralEurope";
    else if (paramString.equals("macroman"))
      str = "MacRoman";
    else if (paramString.equals("pclatin2"))
      str = "ISO8859_1";
    else if (paramString.equals("latvian"))
      str = "ISO8859_13";
    else if (paramString.equals("latvian1"))
      str = "ISO8859_13";
    else if (paramString.equals("maccebin"))
      str = "ISO8859_1";
    else if (paramString.equals("macceciai"))
      str = "ISO8859_1";
    else if (paramString.equals("maccecias"))
      str = "ISO8859_1";
    else if (paramString.equals("maccecsas"))
      str = "ISO8859_1";
    else if (paramString.equals("latin1bin"))
      str = "ISO8859_1";
    else if (paramString.equals("latin1cias"))
      str = "ISO8859_1";
    else if (paramString.equals("latin1csas"))
      str = "ISO8859_1";
    else if (paramString.equals("cp1251bin"))
      str = "ISO8859_1";
    else if (paramString.equals("cp1251cias"))
      str = "Cp1251";
    else if (paramString.equals("cp1251csas"))
      str = "Cp1251";
    else if (paramString.equals("macromanbin"))
      str = "ISO8859_1";
    else if (paramString.equals("macromancias"))
      str = "ISO8859_1";
    else if (paramString.equals("macromanciai"))
      str = "ISO8859_1";
    else if (paramString.equals("macromancsas"))
      str = "ISO8859_1";
    else if (paramString.equals("cp1256"))
      str = "Cp1256";
    else if (paramString.equals("binary"))
      str = "US-ASCII";
    else if (paramString.equals("armscii"))
      str = "ISO8859_1";
    else if (paramString.equals("ascii"))
      str = "US-ASCII";
    else if (paramString.equals("cp1250"))
      str = "Cp1250";
    else if (paramString.equals("cp1256"))
      str = "Cp1256";
    else if (paramString.equals("cp866"))
      str = "Cp866";
    else if (paramString.equals("dec8"))
      str = "ISO8859_1";
    else if (paramString.equals("greek"))
      str = "ISO8859_7";
    else if (paramString.equals("hebrew"))
      str = "ISO8859_8";
    else if (paramString.equals("hp8"))
      str = "ISO8859_1";
    else if (paramString.equals("keybcs2"))
      str = "ISO8859_1";
    else if (paramString.equals("koi8r"))
      str = "KOI8_R";
    else if (paramString.equals("koi8ukr"))
      str = "ISO8859_1";
    else if (paramString.equals("latin2"))
      str = "ISO8859_2";
    else if (paramString.equals("latin5"))
      str = "ISO8859_9";
    else if (paramString.equals("latin7"))
      str = "ISO8859_7";
    else if (paramString.equals("cp850"))
      str = "ISO8859_1";
    else if (paramString.equals("cp852"))
      str = "ISO8859_1";
    else if (paramString.equals("swe7"))
      str = "ISO8859_1";
    else if (paramString.equals("utf8"))
      str = "UTF-8";
    else if (paramString.equals("big5"))
      str = "Big5";
    else if (paramString.equals("euckr"))
      str = "EUC_KR";
    else if (paramString.equals("gb2312"))
      str = "EUC_CN";
    else if (paramString.equals("gbk"))
      str = "GBK";
    else if (paramString.equals("sjis"))
      str = "SJIS";
    else if (paramString.equals("tis620"))
      str = "TIS620";
    else if (paramString.equals("ujis"))
      str = "EUC_JP";
    else if (paramString.equals("geostd8"))
      str = "ISO8859_1";
    else if (paramString.equals("geostd8"))
      str = "ISO8859_1";
    else if (paramString.equals("latin1"))
      str = "ISO8859_1";
    else if (paramString.equals("cp932"))
      str = "Windows-31J";
    else if (paramString.equals("eucjpms"))
      str = "EUC_JP_Solaris";
    else
      str = "ISO8859_1";
    return str;
  }

  private int javaCharsetToMySQLCharsetIndex(String paramString)
    throws SQLException
  {
    int i;
    if (paramString.equalsIgnoreCase("Big5"))
      i = 1;
    else if (paramString.equalsIgnoreCase("Cp1250"))
      i = 26;
    else if (paramString.equalsIgnoreCase("Cp1251"))
      i = 14;
    else if (paramString.equalsIgnoreCase("Cp1256"))
      i = 57;
    else if (paramString.equalsIgnoreCase("Cp1257"))
      i = 29;
    else if (paramString.equalsIgnoreCase("Cp437"))
      i = 4;
    else if (paramString.equalsIgnoreCase("Cp866"))
      i = 36;
    else if (paramString.equalsIgnoreCase("Cp932"))
      i = 95;
    else if (paramString.equalsIgnoreCase("EUC_CN"))
      i = 24;
    else if (paramString.equalsIgnoreCase("EUC_JP"))
      i = 12;
    else if (paramString.equalsIgnoreCase("EUC_JP_Solaris"))
      i = 97;
    else if (paramString.equalsIgnoreCase("EUC_KR"))
      i = 19;
    else if (paramString.equalsIgnoreCase("GBK"))
      i = 28;
    else if (paramString.equalsIgnoreCase("ISO8859_1"))
      i = 8;
    else if (paramString.equalsIgnoreCase("ISO8859_13"))
      i = 20;
    else if (paramString.equalsIgnoreCase("ISO8859_2"))
      i = 9;
    else if (paramString.equalsIgnoreCase("ISO8859_7"))
      i = 25;
    else if (paramString.equalsIgnoreCase("ISO8859_8"))
      i = 16;
    else if (paramString.equalsIgnoreCase("ISO8859_9"))
      i = 30;
    else if (paramString.equalsIgnoreCase("KOI8_R"))
      i = 7;
    else if (paramString.equalsIgnoreCase("MacCentralEurope"))
      i = 38;
    else if (paramString.equalsIgnoreCase("MacRoman"))
      i = 39;
    else if (paramString.equalsIgnoreCase("SJIS"))
      i = 13;
    else if (paramString.equalsIgnoreCase("TIS620"))
      i = 18;
    else if ((paramString.equalsIgnoreCase("UnicodeBig")) || (paramString.equalsIgnoreCase("UTF-16BE")))
      i = 33;
    else if (paramString.equalsIgnoreCase("US-ASCII"))
      i = 11;
    else if (paramString.equalsIgnoreCase("UTF-8"))
      i = 33;
    else
      i = 33;
    return i;
  }

  private void loadServerVariables()
    throws SQLException
  {
    try
    {
      this.serverChannel.resetForNewRequest(0);
      this.serverChannel.writeByte((byte)3);
      this.serverChannel.writeString("SHOW VARIABLES", this.connectionLevelTransliterator, false, false);
      this.serverChannel.flushWrites();
      this.serverChannel.readSuccess();
      this.serverChannel.discardUntilEndOfDataPacket(false);
      String str1 = null;
      int i = 1;
      while (!this.serverChannel.readEndOfData(true))
      {
        long l1 = this.serverChannel.readEncodedLength_Type1();
        str1 = this.serverChannel.readString(this.connectionLevelTransliterator, l1);
        long l2;
        if (str1.equals("max_allowed_packet"))
        {
          l2 = this.serverChannel.readEncodedLength_Type1();
          this.serverVarMaxAllowedPacket = Integer.parseInt(this.serverChannel.readString(this.connectionLevelTransliterator, l2));
          continue;
        }
        String str2;
        if (str1.equals("lower_case_table_names"))
        {
          l2 = this.serverChannel.readEncodedLength_Type1();
          str2 = this.serverChannel.readString(this.connectionLevelTransliterator, l2);
          this.supportsLowerCaseTableName = (("on".equalsIgnoreCase(str2)) || ("1".equalsIgnoreCase(str2)) || ("2".equalsIgnoreCase(str2)));
          continue;
        }
        if (str1.equals("sql_mode"))
        {
          l2 = this.serverChannel.readEncodedLength_Type1();
          str2 = this.serverChannel.readString(this.connectionLevelTransliterator, l2).toUpperCase();
          int j = 0;
          try
          {
            j = Integer.parseInt(str2);
          }
          catch (NumberFormatException localNumberFormatException)
          {
            if (str2 != null)
            {
              if (str2.indexOf("ANSI_QUOTES") != -1)
                j |= 4;
              if (str2.indexOf("NO_BACKSLASH_ESCAPES") != -1)
                this.noBackslashEscapes = true;
              if (str2.indexOf("REAL_AS_FLOAT") != -1)
                this.realAsFloat = true;
            }
          }
          if ((j & 0x4) > 0)
          {
            this.quotingChar = '"';
            continue;
          }
          this.quotingChar = '`';
          continue;
        }
        if (str1.equals("character_set"))
        {
          l2 = this.serverChannel.readEncodedLength_Type1();
          this.characterSet4dot0 = this.serverChannel.readString(this.connectionLevelTransliterator, l2);
          continue;
        }
        if (str1.equals("version_comment"))
        {
            
          l2 = this.serverChannel.readEncodedLength_Type1();
          str2 = this.serverChannel.readString(this.connectionLevelTransliterator, l2).toUpperCase();
            

          System.out.println("s2: " + str2 );
          if (!this.version5Dot0Dot30OrGreater)
          {
              if ((str2.indexOf("COMMERCIAL") == -1) || (str2.indexOf("GPL") != -1)){
                  //continue;
              }
            i = 0;
            continue;
          }
            if ((str2.indexOf("COMMERCIAL") == -1) && (str2.indexOf("ENTERPRISE") == -1)){
                //continue;
            }
          i = 0;
          continue;
        }
        this.serverChannel.discardRemainderOfPacket();
      }
        if (i != 0){
            throw this.exceptions.getException(7013);
        }
    }
    catch (Exception localException)
    {
      throw createSQLException(localException);
    }
    this.serverChannel.resetMaxAllowedPacket(this.serverVarMaxAllowedPacket);
  }

  public Exception getServerException(String paramString1, int paramInt, String paramString2)
  {
    String[] arrayOfString = { paramString1 };
    if (paramString2 == null)
      paramString2 = parseServerSQLState(paramInt);
    else if (paramInt == 1317)
      paramString2 = "HY008";
    return this.exceptions.getException(6001, arrayOfString, paramString2, paramInt);
  }

  SQLException createSQLException(Exception paramException)
  {
    if ((paramException instanceof SQLException))
      return (SQLException)paramException;
    if (((paramException instanceof UtilException)) && (((UtilException)paramException).getReason() == 1018))
      return this.exceptions.getException(paramException, "08S01");
    if ((paramException instanceof SocketException))
      return this.exceptions.getException(1018, "08S01");
    String str = paramException.getMessage();
    Object localObject = null;
    int i = 0;
    if (str == null)
      str = paramException.toString();
    String[] arrayOfString = { str };
    SQLException localSQLException = this.exceptions.getException(6001, arrayOfString, "HY000");
    return localSQLException;
  }

  private void parseServerVersion(String paramString)
    throws SQLException
  {
    try
    {
      int i = paramString.indexOf('.');
      this.majorVersion = Integer.parseInt(paramString.substring(0, i));
      String str = paramString.substring(i + 1, paramString.length());
      i = str.indexOf('.');
      this.minorVersion = Integer.parseInt(str.substring(0, i));
      str = str.substring(i + 1, str.length());
      int j = str.length();
      int k = 0;
      char c = str.charAt(k);
      StringBuffer localStringBuffer = new StringBuffer(3);
      while ((c >= '0') && (c <= '9'))
      {
        localStringBuffer.append(c);
        k++;
        if (k >= str.length())
          break;
        c = str.charAt(k);
      }
      this.subMinorVersion = Integer.parseInt(localStringBuffer.toString());
      if (this.majorVersion == 4)
      {
        this.version5Dot0Dot30OrGreater = false;
        if ((this.minorVersion == 0) && (this.subMinorVersion > 11))
        {
          this.version4Dot0 = true;
          return;
        }
        if ((this.minorVersion == 1) && (this.subMinorVersion > 6))
        {
          this.version4Dot1 = true;
          if (this.subMinorVersion > 14)
            this.version4Dot1Dot15 = true;
          return;
        }
      }
      else if (this.majorVersion == 5)
      {
        if ((this.minorVersion == 0) && (this.subMinorVersion > 14))
        {
          this.version5orLater = true;
          if (this.subMinorVersion > 15)
            this.version5Dot0Dot16 = true;
          if (this.subMinorVersion < 30)
            this.version5Dot0Dot30OrGreater = false;
          return;
        }
        if (this.minorVersion >= 1)
        {
          this.version5orLater = true;
          this.version5Dot1orLater = true;
          return;
        }
      }
      else
      {
        this.version5orLater = true;
        this.version5Dot1orLater = true;
        return;
      }
    }
    catch (Exception localException)
    {
    }
    String[] arrayOfString = { paramString };
    throw this.exceptions.getException(7000, arrayOfString);
  }

  private static String encrypt(String paramString1, String paramString2)
  {
      return "";
      /*
    if ((paramString1 == null) || (paramString1.length() == 0))
      return paramString1;
    long[] arrayOfLong1 = hash(paramString2);
    long[] arrayOfLong2 = hash(paramString1);
    long l1 = 1073741823L;
    long l2 = (arrayOfLong1[0] ^ arrayOfLong2[0]) % l1;
    long l3 = (arrayOfLong1[1] ^ arrayOfLong2[1]) % l1;
    char[] arrayOfChar = new char[paramString2.length()];
    for (int j = 0; j < paramString2.length(); j++)
    {
      l2 = (l2 * 3L + l3) % l1;
      l3 = (l2 + l3 + 33L) % l1;
      d = l2 / l1;
      i = (byte)(int)Math.floor(d * 31.0D + 64.0D);
      arrayOfChar[j] = (char)i;
    }
    l2 = (l2 * 3L + l3) % l1;
    l3 = (l2 + l3 + 33L) % l1;
    double d = l2 / l1;
    int i = (byte)(int)Math.floor(d * 31.0D);
    for (int j = 0; j < paramString2.length(); j++)
    {
      int tmp205_203 = j;
      char[] tmp205_201 = arrayOfChar;
      tmp205_201[tmp205_203] = (char)(tmp205_201[tmp205_203] ^ (char)i);
    }
    return new String(arrayOfChar);
      */
  }

  private static long[] hash(String paramString)
  {
    long l1 = 1345345333L;
    long l2 = 7L;
    long l3 = 305419889L;
    for (int i = 0; i < paramString.length(); i++)
    {
      if ((paramString.charAt(i) == ' ') || (paramString.charAt(i) == '\t'))
        continue;
      long l4 = 0xFF & paramString.charAt(i);
      l1 ^= ((l1 & 0x3F) + l2) * l4 + (l1 << 8);
      l3 += (l3 << 8 ^ l1);
      l2 += l4;
    }
    long[] arrayOfLong = new long[2];
    arrayOfLong[0] = (l1 & 0x7FFFFFFF);
    arrayOfLong[1] = (l3 & 0x7FFFFFFF);
    return arrayOfLong;
  }

  private String parseServerSQLState(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    case 1022:
    case 1048:
    case 1052:
    case 1062:
    case 1169:
    case 1216:
    case 1217:
      str = "23000";
      break;
    case 1037:
    case 1038:
      str = "HY001";
      break;
    case 1040:
    case 1251:
      str = "08004";
      break;
    case 1042:
    case 1043:
    case 1047:
    case 1053:
    case 1080:
    case 1081:
    case 1152:
    case 1153:
    case 1154:
    case 1155:
    case 1156:
    case 1157:
    case 1158:
    case 1159:
    case 1160:
    case 1161:
    case 1184:
    case 1189:
    case 1190:
    case 1218:
      str = "08S01";
      break;
    case 1044:
    case 1049:
    case 1055:
    case 1056:
    case 1057:
    case 1059:
    case 1061:
    case 1063:
    case 1064:
    case 1065:
    case 1066:
    case 1067:
    case 1068:
    case 1069:
    case 1070:
    case 1071:
    case 1072:
    case 1073:
    case 1074:
    case 1075:
      str = "42000";
      break;
    case 1045:
      str = "28000";
      break;
    case 1046:
      str = "3D000";
      break;
    case 1050:
      str = "42S01";
      break;
    case 1051:
    case 1109:
    case 1146:
      str = "42S02";
      break;
    case 1054:
    case 1247:
      str = "42S22";
      break;
    case 1058:
    case 1136:
      str = "21S01";
      break;
    case 1060:
      str = "42S21";
      break;
    case 1082:
      str = "42S12";
      break;
    case 1083:
    case 1084:
    case 1090:
    case 1091:
    case 1101:
    case 1102:
    case 1103:
    case 1104:
    case 1106:
    case 1107:
    case 1110:
    case 1112:
    case 1113:
    case 1115:
    case 1118:
    case 1120:
    case 1121:
    case 1131:
    case 1132:
    case 1133:
    case 1138:
    case 1139:
    case 1140:
    case 1141:
    case 1142:
    case 1143:
    case 1144:
    case 1145:
    case 1147:
    case 1148:
    case 1149:
    case 1162:
    case 1163:
    case 1164:
    case 1166:
    case 1167:
    case 1170:
    case 1171:
    case 1172:
    case 1173:
    case 1177:
    case 1178:
    case 1203:
    case 1211:
    case 1226:
    case 1230:
    case 1231:
    case 1232:
    case 1234:
    case 1235:
    case 1239:
    case 1248:
    case 1250:
    case 1252:
    case 1253:
    case 1280:
    case 1281:
    case 1286:
      str = "42000";
      break;
    case 1249:
    case 1261:
    case 1262:
    case 1263:
      str = "01000";
      break;
    case 1264:
    case 1265:
      str = "01004";
      break;
    case 1179:
    case 1207:
      str = "25000";
      break;
    case 1213:
      str = "40001";
      break;
    case 1222:
    case 1241:
    case 1242:
      str = "21000";
      break;
    case 1317:
      str = "HY008";
      break;
    case 1023:
    case 1024:
    case 1025:
    case 1026:
    case 1027:
    case 1028:
    case 1029:
    case 1030:
    case 1031:
    case 1032:
    case 1033:
    case 1034:
    case 1035:
    case 1036:
    case 1039:
    case 1041:
    case 1076:
    case 1077:
    case 1078:
    case 1079:
    case 1085:
    case 1086:
    case 1087:
    case 1088:
    case 1089:
    case 1092:
    case 1093:
    case 1094:
    case 1095:
    case 1096:
    case 1097:
    case 1098:
    case 1099:
    case 1100:
    case 1105:
    case 1108:
    case 1111:
    case 1114:
    case 1116:
    case 1117:
    case 1119:
    case 1122:
    case 1123:
    case 1124:
    case 1125:
    case 1126:
    case 1127:
    case 1128:
    case 1129:
    case 1130:
    case 1134:
    case 1135:
    case 1137:
    case 1150:
    case 1151:
    case 1165:
    case 1168:
    case 1174:
    case 1175:
    case 1176:
    case 1180:
    case 1181:
    case 1182:
    case 1183:
    case 1185:
    case 1186:
    case 1187:
    case 1188:
    case 1191:
    case 1192:
    case 1193:
    case 1194:
    case 1195:
    case 1196:
    case 1197:
    case 1198:
    case 1199:
    case 1200:
    case 1201:
    case 1202:
    case 1204:
    case 1205:
    case 1206:
    case 1208:
    case 1209:
    case 1210:
    case 1212:
    case 1214:
    case 1215:
    case 1219:
    case 1220:
    case 1221:
    case 1223:
    case 1224:
    case 1225:
    case 1227:
    case 1228:
    case 1229:
    case 1233:
    case 1236:
    case 1237:
    case 1238:
    case 1240:
    case 1243:
    case 1244:
    case 1245:
    case 1246:
    case 1254:
    case 1255:
    case 1256:
    case 1257:
    case 1258:
    case 1259:
    case 1260:
    case 1266:
    case 1267:
    case 1268:
    case 1269:
    case 1270:
    case 1271:
    case 1272:
    case 1273:
    case 1274:
    case 1275:
    case 1276:
    case 1277:
    case 1278:
    case 1279:
    case 1282:
    case 1283:
    case 1284:
    case 1285:
    case 1287:
    case 1288:
    case 1289:
    case 1290:
    case 1291:
    case 1292:
    case 1293:
    case 1294:
    case 1295:
    case 1296:
    case 1297:
    case 1298:
    case 1299:
    case 1300:
    case 1301:
    case 1302:
    case 1303:
    case 1304:
    case 1305:
    case 1306:
    case 1307:
    case 1308:
    case 1309:
    case 1310:
    case 1311:
    case 1312:
    case 1313:
    case 1314:
    case 1315:
    case 1316:
    default:
      str = "HY000";
    }
    return str;
  }

  public synchronized MySQLImplStatement getCancelPendingStatement()
  {
    return this.cancelPendingStatement;
  }

  public synchronized void setCancelPendingStatement(MySQLImplStatement paramMySQLImplStatement)
  {
    this.cancelPendingStatement = paramMySQLImplStatement;
  }

  public void setUpCancelRequest()
    throws SQLException
  {
    if (this.cancelConnection == null)
    {
      this.cancelConnection = BaseClassUtility.getConnection("MySQL");
      this.cancelConnection.open(this.connectProps, this.exceptions, this.debug);
    }
  }

  public Socket getQueryTimeoutSocket()
  {
    return this.socket;
  }
}

/* Location:           C:\Dokumente und Einstellungen\Administrator\Desktop\ddx\xqmysql.jar
 * Qualified Name:     com.ddtek.xquery.jdbc.mysql.MySQLImplConnection
 * JD-Core Version:    0.6.0
 */