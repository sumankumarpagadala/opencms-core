/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH & Co. KG (http://www.alkacon.com)
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

package org.opencms.ui.apps.linkvalidation;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsPropertyDefinition;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.relations.CmsInternalLinksValidator;
import org.opencms.relations.CmsRelation;
import org.opencms.ui.A_CmsUI;
import org.opencms.ui.CmsVaadinUtils;
import org.opencms.ui.apps.A_CmsWorkplaceApp;
import org.opencms.ui.apps.CmsFileExplorer;
import org.opencms.ui.apps.Messages;
import org.opencms.util.CmsStringUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * Class for the Link validation app.<p>
 */
public class CmsLinkValidationApp extends A_CmsWorkplaceApp {

    public class InternalValidator extends A_CmsLinkValidator {

        CmsInternalLinksValidator validator;

        @Override
        public List<CmsResource> failedResources(List<String> resources) {

            validator = new CmsInternalLinksValidator(A_CmsUI.getCmsObject(), resources);
            return validator.getResourcesWithBrokenLinks();
        }

        @Override
        public String failMessage(CmsResource resource) {

            String res = "";
            List<CmsRelation> brokenLinks = validator.getBrokenLinksForResource(resource.getRootPath());
            if (brokenLinks != null) {
                Iterator<CmsRelation> j = brokenLinks.iterator();
                while (j.hasNext()) {
                    res += getBrokenLinkString(j.next().getTargetPath());
                }
            }
            return res;
        }

        @Override
        public String getValidationName() {

            return CmsVaadinUtils.getMessageText(Messages.GUI_LINKVALIDATION_BROKENLINKS_DETAIL_LINKS_NAME_0);
        }

        /**
         * get string to show for broken link.<p>
         *
         * 1:1 the same like old workplace app<p>
         *
         * @param rootPath to get Broken links for.
         * @return broken link string
         */
        private String getBrokenLinkString(String rootPath) {

            String ret = "";

            CmsObject rootCms;
            try {
                rootCms = OpenCms.initCmsObject(A_CmsUI.getCmsObject());

                rootCms.getRequestContext().setSiteRoot("");

                String siteRoot = OpenCms.getSiteManager().getSiteRoot(rootPath);
                String siteName = siteRoot;
                if (siteRoot != null) {
                    try {

                        siteName = rootCms.readPropertyObject(
                            siteRoot,
                            CmsPropertyDefinition.PROPERTY_TITLE,
                            false).getValue(siteRoot);
                    } catch (CmsException e) {
                        siteName = siteRoot;
                    }
                    ret = rootPath.substring(siteRoot.length());
                } else {
                    siteName = "/";
                }
                if (!A_CmsUI.getCmsObject().getRequestContext().getSiteRoot().equals(siteRoot)) {
                    ret = CmsVaadinUtils.getMessageText(
                        org.opencms.workplace.commons.Messages.GUI_DELETE_SITE_RELATION_2,
                        new Object[] {siteName, rootPath});
                }
            } catch (CmsException e1) {
                //
            }
            return ret;
        }

    }

    /**
     * @see org.opencms.ui.apps.A_CmsWorkplaceApp#getBreadCrumbForState(java.lang.String)
     */
    @Override
    protected LinkedHashMap<String, String> getBreadCrumbForState(String state) {

        LinkedHashMap<String, String> crumbs = new LinkedHashMap<String, String>();

        //Main page.
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(state)) {
            crumbs.put("", CmsVaadinUtils.getMessageText(Messages.GUI_LINKVALIDATION_ADMIN_TOOL_NAME_SHORT_0));
            return crumbs;
        }
        return new LinkedHashMap<String, String>(); //size==1 & state was not empty -> state doesn't match to known path
    }

    /**
     * @see org.opencms.ui.apps.A_CmsWorkplaceApp#getComponentForState(java.lang.String)
     */
    @Override
    protected Component getComponentForState(String state) {

        if (state.isEmpty()) {
            m_rootLayout.setMainHeightFull(true);
            return getInternalComponent();
        }
        return null;
    }

    /**
     * @see org.opencms.ui.apps.A_CmsWorkplaceApp#getSubNavEntries(java.lang.String)
     */
    @Override
    protected List<NavEntry> getSubNavEntries(String state) {

        return null;
    }

    /**
     * Returns the component for the internal link validation.<p>
     *
     * @return vaadin component
     */
    private HorizontalSplitPanel getInternalComponent() {

        m_rootLayout.setMainHeightFull(true);
        HorizontalSplitPanel sp = new HorizontalSplitPanel();
        sp.setSizeFull();
        VerticalLayout result = new VerticalLayout();
        result.setSizeFull();
        VerticalLayout intro = CmsVaadinUtils.getInfoLayout(Messages.GUI_LINKVALIDATION_INTRODUCTION_0);
        VerticalLayout nullResult = CmsVaadinUtils.getInfoLayout(Messages.GUI_LINKVALIDATION_NO_BROKEN_LINKS_0);

        nullResult.setVisible(false);
        CmsLinkValidationInternalTable table = new CmsLinkValidationInternalTable(
            intro,
            nullResult,
            new InternalValidator());
        table.setVisible(false);
        table.setSizeFull();
        table.setWidth("100%");

        result.addComponent(table);
        result.addComponent(intro);
        result.addComponent(nullResult);

        VerticalLayout leftCol = new VerticalLayout();
        leftCol.setSizeFull();
        CmsInternalResources resources = new CmsInternalResources(table);
        leftCol.addComponent(resources);

        leftCol.setExpandRatio(resources, 1);
        sp.setFirstComponent(leftCol);
        sp.setSecondComponent(result);
        sp.setSplitPosition(CmsFileExplorer.LAYOUT_SPLIT_POSITION, Unit.PIXELS);
        return sp;
    }
}
