package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.types.ToolTipInfo;
import pmedia.utils.StringUtils;
import pmedia.utils.ToolTipFactory;

import cmx.tools.StyleTreeFactory;
import cmx.types.ApplicationManager;
import cmx.types.ContentManager;
import cmx.types.DBConnectionError;
import cmx.types.Page;
import cmx.types.ShowcaseInfo;
import cmx.types.ShowcaseManager;
import cmx.types.StyleTree;
import cmx.types.TemplateInfo;
import cmx.types.TemplateManager;
import flexjson.JSONSerializer;

public class TooltipManagerAction extends CMBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;

	
	@SMDMethod
	public ToolTipInfo getToolTip(
			String key,String lang)
	{
			return ToolTipFactory.getToolTip(key, lang);
	}
	
}
