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
public class InfantryArchaicDoubleStunStaffWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicDoubleStunStaffWeapon() {
        super();

        name = "Staff (Stun Staff, Double-End)";
        setInternalName(name);
        addLookupName("InfantryDoubleStunStaff");
        addLookupName("Double Ended Stun Staff");
        ammoType = AmmoType.T_NA;
        cost = 500;
        bv = 0.06;
        flags = flags.or(F_NO_FIRES).or(F_INF_NONPENETRATING).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.07;
        infantryRange = 0;
        introDate = 2100;
        techLevel.put(2100,TechConstants.T_ALLOWED_ALL);
        availRating = new int[]{RATING_A,RATING_C,RATING_A,RATING_C};
        techRating = RATING_A;
        rulesRefs =" 272, TM";
    }
}
