package com.example.accommodate;
import com.example.accommodate.global.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.accommodate.global.*;

import java.util.List;

public class ApartmentAdapter extends ArrayAdapter<Accommodation> {
    private Context context;
    private List<Accommodation> apartments;

    public ApartmentAdapter(Context context, List<Accommodation> apartments) {
        super(context, 0, apartments);
        this.context = context;
        this.apartments = apartments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_apartment, parent, false);
        }

        Accommodation apartment = apartments.get(position);

        ImageView apartmentPhoto = convertView.findViewById(R.id.apartment_photo);
        TextView apartmentName = convertView.findViewById(R.id.apartment_name);
        TextView apartmentCost = convertView.findViewById(R.id.apartment_cost);
        Button seeMoreButton = convertView.findViewById(R.id.see_more_button);

        /*apartmentPhoto.setImageResource(apartment.getPhoto());*/
        apartmentName.setText(apartment.getAccName());
        apartmentCost.setText(apartment.getPrice());

        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookRoomActivity.class);
                intent.putExtra("APARTMENT_NAME", apartment.getAccName());
                intent.putExtra("APARTMENT_COST", apartment.getPrice());
                /*intent.putExtra("APARTMENT_PHOTO", apartment.getPhotoResId());*/
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}