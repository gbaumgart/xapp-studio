package cmx.XQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQSequence;
import javax.xml.xquery.XQStaticContext;

import com.ddtek.xquery.xqj.DDXQDataSource;
import com.ddtek.xquery.xqj.DDXQJDBCConnection;
import javax.xml.xquery.XQItemType;

import pmedia.utils.StringUtils;

public class XqueryProcessor {

	public static String toUrl( String filename )
	{
	    String url = "file:" + filename.replace( '\\', '/' );
	    return url;
	}
	@SuppressWarnings("finally")
	public static int convert(String xqueryFile, String connectionURL,String targetFileName,String dbUser,String dbPwd,String lang) throws Exception
	{
		XQConnection         xqconnection = null;
		XQExpression xqExpr       = null;
		File outfile = new File(targetFileName);
		FileOutputStream    outWriter    = new FileOutputStream(outfile) ;


			DDXQDataSource dataSource = new DDXQDataSource();

			DDXQJDBCConnection[] jdbcConnection = new DDXQJDBCConnection[1];
			jdbcConnection[0] = createConnection("mysql_localhost_3306", connectionURL, dbUser, dbPwd, true, "full", "");

			dataSource.setDdxqJdbcConnection(jdbcConnection);

			dataSource.setOptions("serialize=indent=yes");

			xqconnection = dataSource.getConnection();

			XQStaticContext context = xqconnection.getStaticContext();
			context.setBindingMode(XQConstants.BINDING_MODE_DEFERRED);
			//context.
			xqconnection.setStaticContext(context);

			//xqExpr = xqconnection.prepareExpression(new FileReader(xqueryFile));

			xqExpr = xqconnection.createExpression();

			if(lang!=null)
			{
//				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));

			}
			//System.out.println("xquery file : " + xqueryFile );
			FileReader xqFile = new FileReader(xqueryFile);
			XQSequence xqSequence = xqExpr.executeQuery(xqFile);//
			xqSequence.writeSequenceToResult(new StreamResult(outWriter));

			outWriter.close();
			outWriter.flush();
			



			return 1;

	}
	@SuppressWarnings("finally")
	public static int convertEx(String connectionName, String xqueryFile, String connectionURL,String targetFileName,String dbUser,String dbPwd,String lang,String prefix) throws Exception
	{
		XQConnection         xqconnection = null;
		XQExpression xqExpr       = null;
		File outfile = new File(targetFileName);
		FileOutputStream    outWriter    = new FileOutputStream(outfile) ;


			DDXQDataSource dataSource = new DDXQDataSource();

			DDXQJDBCConnection[] jdbcConnection = new DDXQJDBCConnection[1];
			jdbcConnection[0] = createConnection(connectionName, connectionURL, dbUser, dbPwd, true, "full", "");

			dataSource.setDdxqJdbcConnection(jdbcConnection);

			dataSource.setOptions("serialize=indent=yes");

			xqconnection = dataSource.getConnection();

			XQStaticContext context = xqconnection.getStaticContext();
			context.setBindingMode(XQConstants.BINDING_MODE_DEFERRED);
			//context.
			xqconnection.setStaticContext(context);

			//xqExpr = xqconnection.prepareExpression(new FileReader(xqueryFile));

			xqExpr = xqconnection.createExpression();
//for $jos_content in collection($table/content)
			//declare variable $prefix as as document-node(element(*, xs:untyped)) external;
			//declare variable $prefix as as document-node(element(*, xs:untyped)) external;
			//declare variable $prefix as xs:string external;
			
			//for $jos_content in collection( concat($prefix,"content"))/bob_content
			//for $jos_content in collection("bob_content")/bob_content
			if(lang!=null)
			{
//				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
				
				//xqExpr.bindString(new QName("lang"), lang, xqconnection.createDocumentElementType(XQItemType.XQBASETYPE_STRING);
				
				//xqconnection.createDocumentElementType(XQItemType.XQBASETYPE_STRING);
			}
			if(prefix!=null)
			{
//				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
				///xqExpr.bindString(new QName("prefix"), prefix, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
			}
			//System.out.println("xquery file : " + xqueryFile );
			//FileReader xqFile = new FileReader(xqueryFile);
			//xqExpr.executeCommand(arg0)
			//InputStreamReader reader = xqFile.
			String xqFileContent = StringUtils.readFileAsString(xqueryFile);
			xqFileContent = xqFileContent.replace("prefixx", prefix);
			
			//System.out.println("excecute xquery file : " + xqueryFile);
			//XQSequence xqSequence = xqExpr.executeQuery(xqFile);//
			XQSequence xqSequence = xqExpr.executeQuery(xqFileContent);
			xqSequence.writeSequenceToResult(new StreamResult(outWriter));

			outWriter.close();
			outWriter.flush();
			
			xqconnection.close();



			return 1;

	}

	@SuppressWarnings("finally")
	public static int convertFromXML(String xqueryFile,String inputFile,String targetFileName) throws Exception
	{
		XQConnection         xqconnection = null;
		//XQExpression xqExpr       = null;
		javax.xml.xquery.XQPreparedExpression xqExpr       = null;
		File outfile = new File(targetFileName);
		FileOutputStream    outWriter    = new FileOutputStream(outfile) ;
		//String    inputUri                = "file:///x:/ProjectRoot/master/code/myeclipse/IEventsServer/WebRoot/fnac_raw.xml";

		inputFile = toUrl(inputFile);


		try {



			DDXQDataSource dataSource = new DDXQDataSource();
			//dataSource.setDdxqJdbcConnection(jdbcConnection);
			dataSource.setOptions("serialize=indent=yes");
			xqconnection = dataSource.getConnection();

			XQStaticContext context = xqconnection.getStaticContext();
			context.setBindingMode(XQConstants.BINDING_MODE_DEFERRED);
			xqconnection.setStaticContext(context);
			//

			xqExpr = xqconnection.prepareExpression(new FileReader(xqueryFile));
			//xqExpr = xqconnection.createExpression();
			//System.out.println("xquery file : " + xqueryFile );

			//FileReader xqFile = new FileReader(xqueryFile);

			javax.xml.namespace.QName varNames[] = xqExpr.getAllExternalVariables();
			for(int i=0; i<varNames.length; i++)
			{
				if (varNames[i]==XQConstants.CONTEXT_ITEM)
				{
					xqExpr.bindDocument(XQConstants.CONTEXT_ITEM, new StreamSource(inputFile), null);
					break;
				}
			}

			xqExpr.executeQuery().writeSequenceToResult(new StreamResult(outWriter));
			//XQSequence xqSequence = xqExpr.executeQuery(xqFile);//
			//xqSequence.writeSequenceToResult(new StreamResult(outWriter));

			outWriter.close();
			outWriter.flush();




		} catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			if (outWriter!=null) outWriter.flush();
			if (xqExpr != null)       xqExpr.close();
			if (xqconnection != null) xqconnection.close();

			return 1;
		}
	}

	private static DDXQJDBCConnection createConnection(String name, String url, String user, String pswd, boolean forest, String esc, String sqlProfile) {
		DDXQJDBCConnection c = new DDXQJDBCConnection(name);
		
		c.setUrl(url);
		if (user.length() > 0) c.setUser(user);
		if (pswd.length() > 0) c.setPassword(pswd);
		c.setSqlXmlForest(forest);
		c.setSqlXmlIdentifierEscaping(esc);
		if (sqlProfile.length() > 0) c.setSqlProfile(sqlProfile);
		return c;
	}
}
