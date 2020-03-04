package com.mabnets.www.poultry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder> implements Filterable {
    private Context context;
    private List<srchdata> searchlist;
    private List<srchdata> searchlistcpy;

    public searchAdapter (Context context,List<srchdata> searchlist){
        this.context=context;
        this.searchlist=searchlist;
        this.searchlistcpy=new ArrayList<>(searchlist);

    }
    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(parent.getContext());
        View view=inflator.inflate(R.layout.diseaseinf,parent,false);
        searchHolder srch=new searchHolder(view);
        return srch;
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {
        final srchdata srchdata=searchlist.get(position);
        holder.tvdis.setText(srchdata.disease);
        holder.tvsymptom.setText(srchdata.condition);
        holder.cvsrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("diseas",srchdata.disease);
                bundle.putString("sysmptom",srchdata.condition);
                bundle.putString("cure",srchdata.cure);
                Intent intent=new Intent(context,moreinfo.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                CustomIntent.customType(context,"right-to-left");
            }
        });
    }

    @Override
    public int getItemCount() {
        if(searchlist!=null){
            return searchlist.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return  searchfilter;
    }
    private Filter searchfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<srchdata> filteredlist=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredlist.addAll(searchlistcpy);
            }else{
                String Filteredstring=constraint.toString().toLowerCase().trim();
                for(srchdata item: searchlistcpy){
                    if(item.disease.toLowerCase().contains(Filteredstring)){
                        filteredlist.add(item);
                    }else if(item.condition.toLowerCase().contains(Filteredstring)){
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredlist;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchlist.clear();
            searchlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public static class searchHolder extends RecyclerView.ViewHolder{
        public TextView tvdis;
        public TextView tvsymptom;
        public LinearLayout cvsrch;

        public searchHolder(View itemView) {
            super(itemView);
            tvdis=(TextView)itemView.findViewById(R.id.disease);
            tvsymptom=(TextView)itemView.findViewById(R.id.symptoms);
            cvsrch=(LinearLayout)itemView.findViewById(R.id.cvsrchl);
        }
    }
}

