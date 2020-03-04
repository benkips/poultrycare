package com.mabnets.www.poultry;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class vetadapter extends RecyclerView.Adapter<vetadapter.vetholder> {
    public ArrayList vetlist;
    public Context context;

    public vetadapter(ArrayList vetlist, Context context) {
        this.vetlist = vetlist;
        this.context = context;
    }

    @NonNull
    @Override
    public vetholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflator=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflator.inflate(R.layout.consulinf,viewGroup,false);
        vetadapter.vetholder vh=new vetadapter.vetholder(view);

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull vetholder vetholder, int i) {
        final vetinaryz vetinaryz=(vetinaryz)vetlist.get(i);
        vetholder.tvname.setText("Vetinary officer"+vetinaryz.id);
        vetholder.cvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("vid",vetinaryz.id);
                Intent intent=new Intent(context,chats.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                CustomIntent.customType(context,"right-to-left");

            }
        });
        vetholder.ivmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",vetinaryz.email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });
        vetholder.ivcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0719864925"));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(vetlist!=null){
            return vetlist.size();
        }
        return 0;
    }

    public static class vetholder extends RecyclerView.ViewHolder {
        private TextView tvname;
        private LinearLayout cvet;
        private ImageView ivcall;
        private ImageView ivmail;

    public vetholder(@NonNull View itemView) {
        super(itemView);
        tvname=(TextView)itemView.findViewById(R.id.vet);
        cvet=(LinearLayout)itemView.findViewById(R.id.cvvet);
        ivcall=(ImageView) itemView.findViewById(R.id.ivcall);
        ivmail=(ImageView) itemView.findViewById(R.id.ivemail);
    }
}
}
