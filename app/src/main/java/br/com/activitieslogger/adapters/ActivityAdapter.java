package br.com.activitieslogger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.activitieslogger.R;
import br.com.activitieslogger.entities.Activity;
import br.com.activitieslogger.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by PedroTome on 21/08/15.
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {
    private List<Activity> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private TextView tv_time_total;
    private int totalHours;

    public ActivityAdapter(Context c, List<Activity> l, TextView tv_time_total){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tv_time_total = tv_time_total;

        for(Activity activity : mList){
            totalHours+=activity.getTimeActivity();
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_activity, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.tv_dateCreated.setText(dateFormat.format(mList.get(position).getDateCreated()));
        myViewHolder.tv_timeActivity.setText(mList.get(position).getTimeActivity()+"h");
        myViewHolder.tv_typeActivity.setText(mList.get(position).getTypeActivity());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Activity c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        totalHours-= mList.get(position).getTimeActivity();
        mList.remove(position);
        notifyItemRemoved(position);

        if (mList.isEmpty())
            tv_time_total.setText("Você não realizou nenhum exercício");
        else{
            tv_time_total.setText("Você já realizou "+ totalHours + " hora(s) de exercício(s)");
        }


    }

    public Activity getActivity(int position){
        return  mList.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_timeActivity;
        public TextView tv_typeActivity;
        public TextView tv_dateCreated;
        public ImageView iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_dateCreated = (TextView) itemView.findViewById(R.id.tv_dateCreated);
            tv_timeActivity = (TextView) itemView.findViewById(R.id.tv_timeActivity);
            tv_typeActivity = (TextView) itemView.findViewById(R.id.tv_typeActivity);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onDeleteClickListener(v, getPosition());
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
