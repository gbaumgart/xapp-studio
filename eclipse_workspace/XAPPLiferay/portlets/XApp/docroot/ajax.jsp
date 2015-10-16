<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 20-Jan-2007
  Time: 19:46:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
      <title>Simple jsp page</title>
      <script type="text/javascript" src="log4javascript.js"></script>
      <script type="text/javascript">
          // <![CDATA[
          
          var log = log4javascript.getLogger("asdf/asd");
          

          var serverLogTimer;

          var ajaxAppender = new log4javascript.AjaxAppender("http://localhost:8080/XApp-portlet/log4javascript.do");
          ajaxAppender.setWaitForResponse(true);
          ajaxAppender.setLayout(new log4javascript.JsonLayout());
          ajaxAppender.setBatchSize(10);
          log.addAppender(ajaxAppender);

          var ajaxAppender2 = new log4javascript.AjaxAppender("http://localhost:8080/XApp-portlet/log4javascript.do");
          ajaxAppender2.setWaitForResponse(false);
          ajaxAppender2.setLayout(new log4javascript.HttpPostDataLayout());
          ajaxAppender2.setBatchSize(4);
          log.addAppender(ajaxAppender2);

          var words = ["Watford", "booked", "their", "place", "in", "the", "Premiership",
              "with", "a", "convincing", "victory", "over", "Leeds", "in", "a", "frantic",
              "play-off", "final", "The", "Hornets", "went", "in", "front", "through",
              "Jay", "DeMerit", "powerful", "header", "from", "five", "yards", "after",
              "Ashley", "Young", "corner", "Aidy", "Boothroyd", "side", "were", "two",
              "up", "when", "James", "Chambers", "shot", "deflected", "off", "Eddie",
              "Lewis", "and", "looped", "on", "to", "the", "post", "before", "dropping",
              "in", "off", "Neil", "Sullivan", "Darius", "Henderson", "completed", "the",
              "rout", "with", "a", "cool", "penalty", "after", "Shaun", "Derry",
              "cynically", "felled", "Marlon", "King"];

          var loaded = false;

          function generateRandom() {
              var numberOfEntries = 1;
              for (var i = 0; i < numberOfEntries; i++) {
                  var numberOfWords = 1 + Math.floor(10 * Math.random());
                  var entryWords = [];
                  for (var j = 0; j < numberOfWords; j++) {
                      entryWords.push(words[Math.floor(Math.random() * words.length)]);
                  }
                  var entryMessage = entryWords.join(" ");
                  var levelNum = Math.floor(Math.random() * 5);
                  switch (levelNum) {
                      case 0:
                          log.debug(entryMessage);
                          break;
                      case 1:
                          log.info(entryMessage);
                          break;
                      case 2:
                          log.warn(entryMessage);
                          break;
                      case 3:
                          log.error(entryMessage);
                          break;
                      case 4:
                          log.fatal(entryMessage);
                          break;
                  }
              }
          }
          // ]]>
      </script>
      </head>
  <body>log4javascript</body>
<form action="log4javascript.do" method="POST">
    Generate <input type="text" size="5" id="numberOfLogEntries" value="50" /> random log entries
    <input type="button" value="go" onclick="generateRandom()" />
    <input type="button" value="send all remaining" onclick="ajaxAppender.sendAllRemaining()" />
</form>
</html>