/**
 * MegaMek - Copyright (C) 2004,2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
/*
 * Created on Sep 7, 2005
 *
 */
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryArchaicHeavyCrossbowWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicHeavyCrossbowWeapon() {
        super();

        name = "Crossbow (Heavy)(Unofficial)";
        //IO Combines the crossbow types into the Basic Crossbow
        setInternalName(name);
        addLookupName("InfantryHeavyCrossbow");
        addLookupName("Heavy Crossbow");
        ammoType = AmmoType.T_NA;
        cost = 20;
        bv = 0.02;
        flags = flags.or(F_NO_FIRES).or(F_BALLISTIC).or(F_INF_ARCHAIC);
        infantryDamage = 0.02;
        infantryRange = 0;
        introDate = 1950;
        techLevel.put(1950,TechConstants.T_IS_UNOFFICIAL);
        availRating = new int[]{RATING_A,RATING_A,RATING_A,RATING_B};
        techRating = RATING_A;
        rulesRefs =" 272, TM";
    }
}
