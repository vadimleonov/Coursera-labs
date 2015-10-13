package com.vadimsleonovs.modernart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class main extends AppCompatActivity {

    SeekBar seekBar;
    LinearLayout mScreen;
    View viewBlue;
    int[] viewBlueColor = {0, 102, 204};
    View viewGray;
    int[] viewGrayColor = {153, 153, 153};
    View viewGold;
    int[] viewGoldColor = {230, 177, 33};
    View viewGreen;
    int[] viewGreenColor = {0, 128, 0};
    View viewRed;
    int[] viewRedColor = {187, 0, 0};
    private int seek;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateBackground();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScreen = (LinearLayout) findViewById(R.id.main_linear);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        //for colors
        viewBlue = mScreen.findViewById(R.id.blue_rect);
        viewBlue.setBackgroundColor(Color.rgb(viewBlueColor[0], viewBlueColor[1], viewBlueColor[2]));

        viewGray = mScreen.findViewById(R.id.grey_rect);
        viewGray.setBackgroundColor(Color.rgb(viewGrayColor[0], viewGrayColor[1], viewGrayColor[2]));

        viewGold = mScreen.findViewById(R.id.yellow_rect);
        viewGold.setBackgroundColor(Color.rgb(viewGoldColor[0], viewGoldColor[1], viewGoldColor[2]));

        viewGreen = mScreen.findViewById(R.id.green_rect);
        viewGreen.setBackgroundColor(Color.rgb(viewGreenColor[0], viewGreenColor[1], viewGreenColor[2]));

        viewRed = mScreen.findViewById(R.id.red_rect);
        viewRed.setBackgroundColor(Color.rgb(viewRedColor[0], viewRedColor[1], viewRedColor[2]));


        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void updateBackground() {
        seek = seekBar.getProgress();

        viewBlue.setBackgroundColor(Color.rgb(seek / 2, viewBlueColor[1], viewBlueColor[2]));
        viewGold.setBackgroundColor(Color.rgb(viewGoldColor[0], viewGoldColor[1], seek / 2));
        viewGreen.setBackgroundColor(Color.rgb(viewGreenColor[0], viewGreenColor[1], seek / 2));
        viewRed.setBackgroundColor(Color.rgb(viewRedColor[0], viewRedColor[1], seek / 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Inspired by the works of artists sush as Piet Mondrian and Ben Nicholson.\n\nClick below to learn more.");
            alertDialog.setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String url = "http://www.moma.org";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            alertDialog.setNegativeButton(R.string.not_now, null);
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
