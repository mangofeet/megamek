/*  
* MegaMek - Copyright (C) 2020 - The MegaMek Team  
*  
* This program is free software; you can redistribute it and/or modify it under  
* the terms of the GNU General Public License as published by the Free Software  
* Foundation; either version 2 of the License, or (at your option) any later  
* version.  
*  
* This program is distributed in the hope that it will be useful, but WITHOUT  
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS  
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more  
* details.  
*/  
package megamek.client.ui.swing.tooltip;

import megamek.client.ui.swing.GUIPreferences;
import megamek.client.ui.swing.util.UIUtil;
import megamek.common.Configuration;
import megamek.common.Crew;
import megamek.common.Entity;
import megamek.common.Game;
import megamek.common.options.OptionsConstants;
import megamek.common.util.CrewSkillSummaryUtil;
import org.apache.logging.log4j.LogManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static megamek.client.ui.swing.tooltip.TipUtil.TABLE_BEGIN;
import static megamek.client.ui.swing.tooltip.TipUtil.TABLE_END;
import static megamek.client.ui.swing.tooltip.TipUtil.getOptionList;
import static megamek.client.ui.swing.tooltip.TipUtil.scaledHTMLSpacer;
import static megamek.client.ui.swing.util.UIUtil.guiScaledFontHTML;
import static megamek.client.ui.swing.util.UIUtil.uiQuirksColor;

public final class PilotToolTip {
    
    /** the portrait base size */
    private final static int PORTRAIT_BASESIZE = 72;
    final static String BG_COLOR = "#313131";

    public static StringBuilder getPilotTipDetailed(Entity entity, boolean showPortrait) {
        return getPilotTip(entity, true, showPortrait, true);
    }
    
    public static StringBuilder getPilotTipShort(Entity entity, boolean showPortrait) {
        return getPilotTip(entity, false, showPortrait, false);
    }

    // PRIVATE

    private static StringBuilder getPilotTip(final Entity entity, boolean detailed, boolean showPortrait, boolean showDefaultPortrait) {
        StringBuilder result = new StringBuilder();
        
        // The crew info (names etc.) and portraits, if shown, are placed
        // in a table side by side
        result.append("<TABLE BORDER=0 BGCOLOR=" + BG_COLOR + " width=100%><TR><TD>");

        if (showPortrait) {
            result.append(crewPortraits(entity, showDefaultPortrait));
            // Add a spacer cell
            int dist = (int) (GUIPreferences.getInstance().getGUIScale() * 10);
            result.append("<TD WIDTH=" + dist + "></TD>");
        }

        result.append(crewInfoCell(entity));

        result.append(TABLE_END);
        result.append(scaledHTMLSpacer(3));
        return result;
    }

    /** The crew advantages and MD */
    public static StringBuilder getCrewAdvs(Entity entity, boolean detailed) {
        StringBuilder result = new StringBuilder();
        result.append(scaledHTMLSpacer(3));
        result.append(crewAdvs(entity, detailed));
        return result;
    }
    
    /** Returns a tooltip part with names and skills of the crew. */
    private static StringBuilder crewInfoCell(final Entity entity) {
        Crew crew = entity.getCrew();
        Game game = entity.getGame();
        StringBuilder result = new StringBuilder();
        result.append(guiScaledFontHTML());
        
        // Name / Callsign and Status for each crew member
        for (int i = 0; i < crew.getSlotCount(); i++) {
            if (crew.isMissing(i)) {
                continue;
            }

            if ((crew.getNickname(i) != null) && !crew.getNickname(i).isBlank()) {
                result.append(guiScaledFontHTML(UIUtil.uiNickColor()) + "<B>'" 
                        + crew.getNickname(i).toUpperCase() + "'</B></FONT>");
            } else if ((crew.getName(i) != null) && !crew.getName(i).isBlank()) {
                result.append(crew.getName(i));
            } else {
                result.append("Pilot");
            }

            if (crew.getSlotCount() > 1) {
                result.append(" \u2B1D " + crew.getCrewType().getRoleName(i));
            }
            
            if (!crew.getStatusDesc(i).isEmpty()) {
                result.append(guiScaledFontHTML(GUIPreferences.getInstance().getWarningColor()));
                result.append(" (" + crew.getStatusDesc(i) + ")</FONT>");
            }
            result.append("<BR>");
        }
        
        // Effective entity skill for the whole crew
        boolean rpg_skills = game.getOptions().booleanOption(OptionsConstants.RPG_RPG_GUNNERY);
        result.append(CrewSkillSummaryUtil.getSkillNames(entity) + ": " + crew.getSkillsAsString(rpg_skills));
        
        result.append("</FONT>");
        return result;
    }
    
    /** Returns a tooltip part with crew portraits. */
    private static StringBuilder crewPortraits(final Entity entity, boolean showDefaultPortrait) {
        Crew crew = entity.getCrew();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < crew.getSlotCount(); i++) {
            if ((!showDefaultPortrait) && crew.getPortrait(i).isDefault()) {
                continue;
            }

            try {
                // Adjust the portrait size to the GUI scale and number of pilots
                float imgSize = UIUtil.scaleForGUI(PORTRAIT_BASESIZE);
                imgSize /= 0.2f * (crew.getSlotCount() - 1) + 1;
                Image portrait = crew.getPortrait(i).getBaseImage().getScaledInstance(-1, (int) imgSize, Image.SCALE_SMOOTH);
                // Write the scaled portrait to file
                // This is done to avoid using HTML rescaling on the portrait which does
                // not do any smoothing and has extremely ugly results
                String tempPath = Configuration.imagesDir() + "/temp/TT_Portrait_" + entity.getExternalIdAsString() + "_" + i + ".png";
                File tempFile = new File(tempPath);
                if (!tempFile.exists()) {
                    BufferedImage bufferedImage = new BufferedImage(portrait.getWidth(null), portrait.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    bufferedImage.getGraphics().drawImage(portrait, 0, 0, null);
                    ImageIO.write(bufferedImage, "PNG", tempFile);
                }
                result.append("<TD VALIGN=TOP><IMG SRC=file:").append(tempPath).append("></TD>");
            } catch (Exception e) {
                LogManager.getLogger().error("", e);
            }
            result.append("<TD WIDTH=3></TD>");
        }
        return result;
    }
    
    /** 
     * Returns a tooltip part with crew advantages. When detailed is
     * true, the advantages will be fully listed, otherwise only the
     * groups and number of advantages per group are given.
     */
    private static StringBuilder crewAdvs(final Entity entity, boolean detailed) {
        Crew crew = entity.getCrew();
        StringBuilder result = new StringBuilder();
        result.append(guiScaledFontHTML(uiQuirksColor(), UnitToolTip.TT_SMALLFONT_DELTA));
        result.append(getOptionList(crew.getOptions().getGroups(), crew::countOptions, detailed));
        result.append("</FONT>");
        return result; 
    }
    
}
