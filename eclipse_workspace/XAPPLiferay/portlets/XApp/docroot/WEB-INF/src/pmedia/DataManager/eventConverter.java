package pmedia.DataManager;
public class eventConverter {
	//private static Properties dbProperties;

	static final String xquery =
				  "<Results>\n"
				+ "\t{\n"
				+ "\tfor $jos_jevents_vevdetail in collection(\"jos_jevents_vevdetail\")/jos_jevents_vevdetail \n"
				+ "\tlet $b := collection(\"jos_jev_locations\")/jos_jev_locations[string(loc_id) = string($jos_jevents_vevdetail/location)] , \n"
				+ "\t    $s_repeat := collection(\"jos_jevents_repetition\")/jos_jevents_repetition[string(eventid) = string($jos_jevents_vevdetail/evdet_id)] , \n"
				+ "\t    $files_link := collection(\"jos_jev_files\")/jos_jev_files[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)] , \n"
				+ "\t    $eventID := collection(\"jos_jevents_vevent\")/jos_jevents_vevent[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)] \n"
				+ "\torder by $jos_jevents_vevdetail/evdet_id \n"
				+ "\treturn \n"
				+ "\t<event_item>\n"
				+ "\t\t<Summary>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/summary/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</Summary>\n"
				+ "\t\t<event_id>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/evdet_id/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</event_id>\n"
				+ "\t\t<location>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$b/title/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</location>\n"
				+ "\t\t<start_repeat>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$s_repeat/startrepeat/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</start_repeat>\n"
				+ "\t\t<end_repeat>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$s_repeat/endrepeat/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</end_repeat>\n"
				+ "\t\t<DTSTART>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/dtstart/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</DTSTART>\n"
				+ "\t\t<DTEND>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/dtend/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</DTEND>\n"
				+ "\t\t<NOENDTIME>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/noendtime/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</NOENDTIME>\n"
				+ "\t\t<std_pic>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$files_link/filename/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</std_pic>\n"
				+ "\t\t<description>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/description/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</description>\n"
				+ "\t\t<uid>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$eventID/uid/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</uid>\n"
				+ "\t\t<loc_id>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$jos_jevents_vevdetail/location/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</loc_id>\n"
				+ "\t\t<cat_id>\n"
				+ "\t\t\t{\n"
				+ "\t\t\t$eventID/catid/text()\n"
				+ "\t\t\t}\n"
				+ "\t\t</cat_id>\n"
				+ "\t</event_item>\n"
				+ "\t}\n"
				+ "</Results>\n"
				;



	
}
