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
public class InfantryArchaicStaffWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicStaffWeapon() {
        super();

        name = "Staff (Unofficial)";
      //IO Combines this weapon into the Polearm Weapon
        setInternalName(name);
        addLookupName("InfantryStaff");
        ammoType = AmmoType.T_NA;
        cost = 5;
        bv = 0.04;
        flags = flags.or(F_NO_FIRES).or(F_INF_NONPENETRATING).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.04;
        infantryRange = 0;
        introDate = 1950;
        techLevel.put(1950,TechConstants.T_IS_UNOFFICIAL);
        availRating = new int[]{RATING_A,RATING_A,RATING_A,RATING_A};
        techRating = RATING_A;
        rulesRefs =" 272, TM";
    }
}
