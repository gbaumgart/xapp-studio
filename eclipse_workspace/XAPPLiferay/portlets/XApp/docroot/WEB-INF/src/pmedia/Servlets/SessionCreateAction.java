package pmedia.Servlets;


/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */



import java.util.HashMap;
import java.util.Map;

import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.servlet.http.HttpSession;

/**
 * @author Brian Wing Shun Chan
 */
public class SessionCreateAction extends SessionAction {
	

	 
	public static Map<String, HttpSession> mySessions = new HashMap<String, HttpSession>();

	@Override
	public void run(HttpSession session) {
		if (_log.isDebugEnabled()) {
			_log.debug(session.getId());
		}
		mySessions.put(session.getId(), session);
		 //System.out.println("SessionCreateAction :  create session  : " +session.getId());
	}

	private static Log _log = LogFactoryUtil.getLog(SessionCreateAction.class);
	public static HttpSession find(String sessionId) {
        return mySessions.get(sessionId);
    }
}