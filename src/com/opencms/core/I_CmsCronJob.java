/*
 * File   : $Source: /alkacon/cvs/opencms/src/com/opencms/core/Attic/I_CmsCronJob.java,v $
 * Date   : $Date: 2003/10/29 13:00:42 $
 * Version: $Revision: 1.4 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2002 - 2003 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
package com.opencms.core;


/**
 * This interface is an empty extension of the con- job interface in the org.opencms
 * package to guarantee backward compatibility with OpenCms version &lt; 5.0.0.
 * Use the interface {@link org.opencms.cron.I_CmsCronJob} instead for OpenCms
 * versions &gt; 5.0.0<p>
 * 
 * @author Thomas Weckert (t.weckert@alkacon.com) 
 * @version $Revision: 1.4 $ $Date: 2003/10/29 13:00:42 $
 * @since 5.1.12
 * @deprecated
 * @see org.opencms.cron.I_CmsCronJob
 */
public interface I_CmsCronJob extends org.opencms.cron.I_CmsCronJob {
    
    // noop

}