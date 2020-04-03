package dev.alox.cardreadernfc.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import dev.alox.cardreadernfc.Model.CardModel;
import dev.alox.cardreadernfc.R;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    Context mContext;
    List<CardModel> mCardList;
    LayoutInflater mInflater;


    public PagerAdapter(Context context, List<CardModel> cardList){
        this.mContext = context;
        this.mCardList = cardList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCardList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return mCardList.indexOf(object);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((VerticalInfiniteCycleViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.card_item,container,false);
        final String cardNumber = mCardList.get(position).getCardNumber();
        final String expDate = mCardList.get(position).getExpDate();
        final String cardType = mCardList.get(position).getCardType();

        ImageView cardTypeView = view.findViewById(R.id.cardTypeSaved);
        TextView cardNumberView = view.findViewById(R.id.cardNumberSaved);
        TextView expDateView = view.findViewById(R.id.expDateSaved);
        MaterialCardView savedCard = view.findViewById(R.id.savedCard);

        savedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog savedCardD = new Dialog(mContext);
                savedCardD.requestWindowFeature(Window.FEATURE_NO_TITLE);
                savedCardD.setContentView(R.layout.card_dialog);
                savedCardD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                savedCardD.setCancelable(true);

                TextView cardNumberD = savedCardD.findViewById(R.id.cardNumberSavedD);
                TextView expdateD = savedCardD.findViewById(R.id.expDateSavedD);
                ImageView cTypeD = savedCardD.findViewById(R.id.cardTypeSavedD);


                cardNumberD.setText(cardNumber);
                expdateD.setText(expDate);

                if (cardType.equalsIgnoreCase("Visa Debit") || cardType.equalsIgnoreCase("VISA CREDIT")){
                    cTypeD.setImageResource(R.drawable.ic_visa);
                }
                else{
                    cTypeD.setImageResource(R.drawable.ic_mastercard);
                }

                savedCardD.show();
            }
        });

        cardNumberView.setText(cardNumber);
        expDateView.setText(expDate);

        if (cardType.equalsIgnoreCase("Visa Debit") || cardType.equalsIgnoreCase("VISA CREDIT")){
            cardTypeView.setImageResource(R.drawable.ic_visa);
        }
        else{
            cardTypeView.setImageResource(R.drawable.ic_mastercard);
        }

        container.addView(view);
        return view;
    }
}
