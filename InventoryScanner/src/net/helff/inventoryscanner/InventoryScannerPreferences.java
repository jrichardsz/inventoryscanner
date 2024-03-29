/* 
 * Copyright (C) 2012 Martin Helff
 * 
 * This file is part of InventoryScanner.
 * 
 * InventoryScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * InventoryScanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with InventoryScanner.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.helff.inventoryscanner;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class InventoryScannerPreferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
