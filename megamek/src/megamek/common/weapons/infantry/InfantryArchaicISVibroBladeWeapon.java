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
public class InfantryArchaicISVibroBladeWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicISVibroBladeWeapon() {
        super();

        name = "Blade (Vibro-blade)";
        setInternalName(name);
        addLookupName("InfantryVibroBlade");
        addLookupName("Vibro Blade");
        ammoType = AmmoType.T_NA;
        cost = 100;
        bv = 0.19;
        flags = flags.or(F_NO_FIRES).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.21;
        infantryRange = 0;
        introDate = 2393;
        techLevel.put(2393, TechConstants.T_IS_EXPERIMENTAL);
        techLevel.put(2400, TechConstants.T_IS_ADVANCED);
        availRating = new int[] { RATING_B,RATING_C ,RATING_B ,RATING_B};
        techRating = RATING_D;
        rulesRefs = "272, TM";
    }
}
