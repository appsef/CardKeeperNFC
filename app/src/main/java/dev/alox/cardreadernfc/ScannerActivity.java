package dev.alox.cardreadernfc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vignesh.nfccardreader.NfcCardManager;
import com.vignesh.nfccardreader.NfcCardReader;
import com.vignesh.nfccardreader.NfcCardResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.alox.cardreadernfc.Adapter.PagerAdapter;
import dev.alox.cardreadernfc.Model.CardModel;


public class ScannerActivity extends AppCompatActivity {

    private NfcCardManager nfcCardManager;
    private NfcCardReader nfcCardReader;

    private TextView cardNumber,expDate;
    private ImageView cardType;
    private String FcardN;
    private String expD;
    private String cardTypeS;
    MaterialButton scanBtn;
    private VerticalInfiniteCycleViewPager cardPager;
    PagerAdapter mPagerAdapter;
    List<CardModel> savedCards;
    private Boolean isScanning = false;
    private Boolean cardScanned = false;
    Set<String> cards;
    private ImageButton deleteCard;
    private ImageView devImage;
    int deleteBtnCount= 1;


    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        devImage = findViewById(R.id.devImage);
        devImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink("https://github.com/alokjkashyap");
            }
        });



        nfcCardManager = new NfcCardManager(this);
        nfcCardReader = new NfcCardReader();

        cardNumber = findViewById(R.id.cardNumber);
        expDate = findViewById(R.id.expDate);
        cardType = findViewById(R.id.cardType);
        scanBtn = findViewById(R.id.addCardbtn);
        cardPager = findViewById(R.id.viewPager);
        deleteCard = findViewById(R.id.clearCard);
        mSharedPreferences = getSharedPreferences("savedCard",MODE_PRIVATE);

        savedCards = new ArrayList();

        cards = mSharedPreferences.getStringSet("savedCard", null);
        if (cards != null){
            List<String> cardsList =new ArrayList<String>(cards);
            for (int i = 0; i <cards.size();i++){
                String name = cardsList.get(i);
                String cardN = name.substring(0,name.indexOf("$"));
                String eD = name.substring(name.indexOf("$")+1,name.indexOf("$$"));
                String cType = name.substring(name.indexOf("$$")+2);
                CardModel cardModel = new CardModel();
                cardModel.setCardNumber(cardN);
                cardModel.setExpDate(eD);
                cardModel.setCardType(cType);
                savedCards.add(cardModel);
            }
        }




        mPagerAdapter = new PagerAdapter(this,savedCards);
        cardPager.setAdapter(mPagerAdapter);



        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = Snackbar.make(v,"Click one more time to delete all cards.",Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimary));
                Set<String> Tcards = mSharedPreferences.getStringSet("savedCard",null);
                if (Tcards!= null){
                    if (deleteBtnCount == 1){
                        snackbar.show();
                        deleteBtnCount++;
                    }
                    else if (deleteBtnCount == 2){
                        mSharedPreferences = getSharedPreferences("savedCard",MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        snackbar.setText("Cards removed.");
                        snackbar.show();
                        finish();
                        startActivity(new Intent(ScannerActivity.this,ScannerActivity.class));
                        deleteBtnCount =1;

                    }
                }
                else{
                    snackbar.setText("No saved card found.");
                    snackbar.show();
                }


            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isScanning){
                    nfcCardManager.disableDispatch();
                    scanBtn.setIcon(getResources().getDrawable(R.drawable.ic_add,getTheme()));
                    scanBtn.setText("Add card");
                    isScanning = false;
                    cardNumber.setText("");
                    expDate.setText("");
                    cardType.setImageResource(0);



                }
                else{
                    android.nfc.NfcAdapter mNfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(v.getContext());

                    if (!mNfcAdapter.isEnabled()) {

                        MaterialAlertDialogBuilder alertbox = new MaterialAlertDialogBuilder(v.getContext(),R.style.AlertDialogTheme);
                        alertbox.setTitle("Please turn on the NFC.");
                        alertbox.setBackground(getDrawable(R.drawable.bg));
                        alertbox.setPositiveButton("TURN ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        alertbox.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertbox.show();

                    }
                    else{
                        nfcCardManager.enableDispatch();
                        scanBtn.setIcon(getResources().getDrawable(R.drawable.ic_clear,getTheme()));
                        scanBtn.setText("Scanning...");
                        isScanning = true;
                    }
                }

            }
        });





    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // When the card is brought closer to the device, a new intent with TAG info is dispatched
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            NfcCardResponse cardResponse = nfcCardReader.readCard(tag); // read the card data with tag
            if (cardResponse != null && cardResponse.getEmvCard() != null) {
                // use card data such as cardNumber, expire date etcc
                cardScanned =true;
                String cardNumber = cardResponse.getEmvCard().getCardNumber();
                expD = cardResponse.getEmvCard().getExpireDate();
                cardTypeS = cardResponse.getEmvCard().getApplicationLabel();
                String div = " ";
                FcardN = cardNumber.substring(0,4) +div + cardNumber.substring(4,8) + div +cardNumber.substring(8,12) + div
                        + cardNumber.substring(12,16);

                String savedCardDetails = FcardN +"$" + expD +"$$" + cardTypeS;
                Set<String> prev = mSharedPreferences.getStringSet("savedCard",null);
                Set<String> fin = new HashSet<String>();

                if (prev != null){
                    fin.addAll(prev);
                }
                fin.add(savedCardDetails);


                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putStringSet("savedCard",fin);
                editor.apply();


                setDetail();
                fillViewpager();
            }
        }
    }

    private void setDetail(){
        cardNumber.setText(FcardN);
        expDate.setText(expD);
        if (cardTypeS.equalsIgnoreCase("Visa Debit") || cardTypeS.equalsIgnoreCase("VISA CREDIT")){
            cardType.setImageResource(R.drawable.ic_visa);
        }
        else{
            cardType.setImageResource(R.drawable.ic_mastercard);

        }

    }

    private void fillViewpager(){
        if (cardScanned){
            Set<String> Cards = mSharedPreferences.getStringSet("savedCard", null);;
            if (Cards != null){
                List<String> cardsList =new ArrayList<String>(Cards);
                for (int i = 0; i <Cards.size();i++){
                    String name = cardsList.get(i);
                    String cardN = name.substring(0,name.indexOf("$"));
                    String eD = name.substring(name.indexOf("$")+1,name.indexOf("$$"));
                    String cType = name.substring(name.indexOf("$$")+2);
                    CardModel cardModel = new CardModel();
                    cardModel.setCardNumber(cardN);
                    cardModel.setExpDate(eD);
                    cardModel.setCardType(cType);
                    savedCards.add(cardModel);
                    mPagerAdapter = new PagerAdapter(ScannerActivity.this,savedCards);
                    cardPager.setAdapter(mPagerAdapter);
                }
            }
            else{

                CardModel cardModel = new CardModel();
                cardModel.setCardNumber(FcardN);
                cardModel.setExpDate(expD);
                cardModel.setCardType(cardTypeS);
                savedCards.add(cardModel);
                mPagerAdapter = new PagerAdapter(ScannerActivity.this,savedCards);
                cardPager.setAdapter(mPagerAdapter);
            }
        }
    }
    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}


