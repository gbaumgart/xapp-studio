Zeugs fuer later :


Lokale urls :  XApp-Studio

XApp-Studio http://www.mc007ibi.dyndns.org:8080/app?uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appId=myeventsapp1d0
Quick-Xapp : http://www.mc007ibi.dyndns.org:8080/quick-xapp-studio?appId=myeventsapp1d0&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9
Mobile - Smartphone : http://mc007ibi.dyndns.org:8080/XApp-portlet/mobileClientBoot.jsp?appId=myeventsapp1d0&uuid=11166763-e89c-44ba-aba7–4e9f4fdf97a9&height=480&width=320&noSim=true
Tablet : http://mc007ibi.dyndns.org:8080/XApp-portlet/mobileClientBoot.jsp?appId=myeventsapp1d0&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&noSim=true

XApp-Connect-Studio (Stand-Alone): http://www.mc007ibi.dyndns.org:8080/XApp-portlet/xappConnect.jsp?appId=myeventsapp1d0&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9

Url – Parameters : http://www.xapp-studio.com/kb?p_p_id=2_WAR_knowledgebaseportlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_pos=2&p_p_col_count=6&_2_WAR_knowledgebaseportlet_resourcePrimKey=64516&_2_WAR_knowledgebaseportlet_mvcPath=%2Fdisplay%2Fview_article.jsp

Liferay (Start)

In /liferay/tomcat-7.0.27/webapps/ROOT/WEB-INF/classes/portal-ext.properties mysql login anpassen.

2 Ways to start :
Eclipse

Console :
sh /liferay/tomcat-7.0.27/bin/catalina.sh run (forground modus)
sh /liferay/tomcat-7.0.27/bin/catalina.sh start (background modus)

Nach starten, erst alle proxies initialisieren : einfach aufmachen http://mc007ibi.dyndns.org:8080/XApp-portlet/mobileClientBoot.jsp?appId=myeventsapp1d0&uuid=11166763-e89c-44ba-aba7–4e9f4fdf97a9&height=480&width=320&noSim=true

Xapp-Studio-Update - Java (SVN update & build)

#svn update /PMaster/MyEclipseWorkspace/XAPPLiferay
cd /PMaster
sh updateXAPP.sh (macht svn update und updated auch wenn liferay is running)
____________

PHP : liegt alles in /PMaster/PHPStorm/

Projects :

1. /PMaster/PHPStorm/xappconnect/ : PHP code fuer andere Projects (Joomla-Plugin)

    es wird eigentlich nur /PMaster/PHPStorm/xappconnect/xapp benutzt, rest ist trash und alt.

    SVN URL : https://s15355725.pearls-media.com:8443/svn/development/trunk/Code/server/xappconnect

2. /PMaster/PHPStorm/xapplocal


 Das ist der PHP - PROXY (/RESTProxy.php). Wichtig !!!

 /xapplocal muss unbedingt ins htdocs root als /xapplocal

 /xapplocal wird in der Liferay instance unter /xapplocal via proxy eingehangen.


 SVN URL : https://s15355725.pearls-media.com:8443/svn/development/trunk/Code/server/xapplocal

3. /PMaster/PHPStorm/xas-joomla/

    Joomla-Plugin

    benutzt /PMaster/PHPStorm/xappconnect/ ueber svn external !

    - Kompilieren der install zip

    1. Zu erst muss /PMaster/PHPStorm/xappconnect/xapp/conf/bwListSQL.php geupdated werden. Dies liest die MySQL queries der 'custom types'
       und schreibt die in bwListSQL.php.

        1. cd /PMaster/PHPStorm/xappconnect/build/
        2. sh createDist.sh
        3. /PMaster/PHPStorm/xappconnect/xapp/conf/bwListSQL.php in svn commiten


    1. cd /PMaster/PHPStorm/xas-joomla/install/
    2. sh buildRelease.sh
    3. nun liegt /PMaster/PHPStorm/xas-joomla/DIST/com_xas.zip als Joomla plugin bereit


    in buildRelease.sh diese zeile auskommentieren :

    sh "$XAPP_SRC/buildMobileApplication.sh"

    ansonsten dauert es zu lange !


    rest kommt spaeter, lol















