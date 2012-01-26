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

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InventoryScannerActivity extends Activity {

    TextView inventoryTitle;
    TextView inventoryList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        inventoryTitle = (TextView) findViewById(R.id.inventoryTitle);
        inventoryList = (TextView) findViewById(R.id.inventoryList);

        if (savedInstanceState != null) {
            inventoryTitle.setText(savedInstanceState.getCharSequence("inventoryTitle"));
            inventoryList.setText(savedInstanceState.getCharSequence("inventoryList"));
        }

        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(InventoryScannerActivity.this);
                integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
            }

        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String recipient = prefs.getString("email_to", "");
                String subject = prefs.getString("email_subject", "");
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipient.split(",\\s*"));
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, inventoryList.getText());
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send)));
            }

        });

        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(InventoryScannerActivity.this).setTitle(R.string.clear_contents)
                        .setMessage(R.string.sure_question)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inventoryList.setText("");
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String format = prefs.getString("format", getString(R.string.format_default));
            inventoryList.append(String.format(format, inventoryTitle.getText(), scanResult.getContents(), new Date()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putCharSequence("inventoryTitle", inventoryTitle.getText());
        bundle.putCharSequence("inventoryList", inventoryList.getText());
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, Menu.FIRST, 1, R.string.settings).setShortcut('9', 's')
                .setIcon(android.R.drawable.ic_menu_preferences);

        menu.add(0, Menu.FIRST + 1, 1, R.string.about).setShortcut('0', 'i')
                .setIcon(android.R.drawable.ic_menu_info_details);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case Menu.FIRST:
            Intent intent = new Intent();
            intent.setClass(this, InventoryScannerPreferences.class);
            startActivity(intent);
            return true;

        case Menu.FIRST + 1:
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle(R.string.about);
            alert.setMessage(R.string.about_detail);

            alert.setPositiveButton(R.string.about_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // nothing
                }
            });

            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

}