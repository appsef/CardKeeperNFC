package dev.alox.cardreadernfc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView scanCard;
    private TextView Title,info,buttonT,dev;
    private ImageView devImg,nfcImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.RGB_565);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        Title = findViewById(R.id.Title);
        info = findViewById(R.id.info);
        buttonT = findViewById(R.id.cardText);
        dev = findViewById(R.id.devText);
        devImg = findViewById(R.id.DevImg);
        nfcImg = findViewById(R.id.nfcImg);
        scanCard = findViewById(R.id.scanCardBtn);
        scanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PackageManager pm = getApplication().getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                    // device has NFC functionality
                    Intent cardScanner = new Intent(MainActivity.this,ScannerActivity.class);

                    Pair[] pairs = new Pair[5];
                    pairs[0] = new Pair<View, String>(info,"titleBold");
                    pairs[1] = new Pair<View, String>(dev,"bottomText");
                    pairs[2] = new Pair<View, String>(devImg,"DevLogo");
                    pairs[3] = new Pair<View, String>(scanCard,"cardLayout");
                    pairs[4] = new Pair<View, String>(nfcImg,"nfc");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(cardScanner,options.toBundle());
                }
                else {
                    MaterialAlertDialogBuilder alertbox = new MaterialAlertDialogBuilder(v.getContext(),R.style.AlertDialogTheme);
                    alertbox.setTitle("Error");
                    alertbox.setBackground(getDrawable(R.drawable.bg));
                    alertbox.setMessage("Your phone doesn't have NFC and this app won't work in your phone.");
                    alertbox.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertbox.show();
                }






            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGB_565);
    }
}
