package com.jivesh.media;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Viewholder_Notification extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView time_result,name_result,notification_result,deletebtn,replybtn;
    ImageButton fvrt_Btn;
    DatabaseReference favouriteref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public Viewholder_Notification(@NonNull View itemView) {
        super(itemView);
    }

    public void setitem(FragmentActivity activity,String name, String url, String userid, String key, String notification,String privacy, String time){

        imageView = itemView.findViewById(R.id.iv_notification_item);
        time_result = itemView.findViewById(R.id.time_item_tv);
        name_result = itemView.findViewById(R.id.name_item_tv);
        notification_result = itemView.findViewById(R.id.notification_item_tv);
        replybtn = itemView.findViewById(R.id.reply_item_notification);

        Picasso.get().load(url).into(imageView);
        time_result.setText(time);
        name_result.setText(name);
        notification_result.setText(notification);

    }

    public void favouriteChecker(String postkey) {

        fvrt_Btn= itemView.findViewById(R.id.fvrt_item_f2);



        favouriteref = database.getReference("favourites");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        favouriteref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(uid)){
                    fvrt_Btn.setImageResource(R.drawable.ic_baseline_turned_in_24);

                }
                else {
                        fvrt_Btn.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setitemRelated(Application activity, String name, String url, String userid, String key, String notification, String privacy, String time){

        TextView timetv = itemView.findViewById(R.id.time_related_tv);
        ImageView imageView = itemView.findViewById(R.id.iv_notification_related);
        TextView nametv = itemView.findViewById(R.id.name_related_tv);
        TextView notificationtv = itemView.findViewById(R.id.notification_related_tv);
        TextView replybtn = itemView.findViewById(R.id.reply_related_notification);

        Picasso.get().load(url).into(imageView);
        nametv.setText(name);
        timetv.setText(time);
        notificationtv.setText(notification);


    }

    public void setitemYourNotification(Application activity, String name, String url, String userid, String key, String notification, String privacy, String time){

        TextView timetv = itemView.findViewById(R.id.time_your_notification_tv);
        ImageView imageView = itemView.findViewById(R.id.iv_notification_your);
        TextView nametv = itemView.findViewById(R.id.name_your_notification_tv);
        TextView notificationtv = itemView.findViewById(R.id.notification_your_notification_tv);
         deletebtn = itemView.findViewById(R.id.delete_your_notification_tv);

        Picasso.get().load(url).into(imageView);
        nametv.setText(name);
        timetv.setText(time);
        notificationtv.setText(notification);


    }
}
