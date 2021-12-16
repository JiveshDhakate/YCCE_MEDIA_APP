package com.jivesh.media;

import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import kotlin.random.Random;

public class PostViewholder extends RecyclerView.ViewHolder {


    ImageView imageView_profile,imageView_post;
    TextView tv_time,tv_desc,tv_likes,tv_comment,tv_nameprofile;
    ImageButton likebtn,menuoptions,commentbtn;
    DatabaseReference likesref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int likescount;

    public PostViewholder(@NonNull View itemView) {
        super(itemView);
    }
    public void SetPost(FragmentActivity activity,String name,String url,String postUri,String time,String uid,String type,String desc){


        SimpleExoPlayer exoPlayer;
        imageView_profile = itemView.findViewById(R.id.iv_postlayout);
        imageView_post = itemView.findViewById(R.id.iv_postlayout_item);
        //tv_comment =itemView.findViewById(R.id.tv_comment_postlayout);
        tv_desc = itemView.findViewById(R.id.tv_desc_postlayout);
        tv_likes = itemView.findViewById(R.id.tv_likes_postlayout);
        tv_time= itemView.findViewById(R.id.tv_time_postlayout);
        tv_nameprofile = itemView.findViewById(R.id.tv_name_postlayout);
        likebtn = itemView.findViewById(R.id.likebutton_postlayout);
        menuoptions = itemView.findViewById(R.id.morebutton_postlayout);
        commentbtn = itemView.findViewById(R.id.commentbutton_postlayout);


        PlayerView playerView = itemView.findViewById(R.id.exoplayer_postlayout);

        if(type.equals("iv")){
            Picasso.get().load(url).into(imageView_profile);
            Picasso.get().load(postUri).into(imageView_post);
            tv_desc.setText(desc);
            tv_time.setText(time);
            tv_nameprofile.setText(name);
            playerView.setVisibility(View.INVISIBLE);
        }
        else if(type.equals("vv")){
            imageView_post.setVisibility(View.INVISIBLE);
            tv_desc.setText(desc);
            tv_time.setText(time);
            tv_nameprofile.setText(name);
            Picasso.get().load(url).into(imageView_profile);

            try{

//                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter().Builder(activity).build();
//                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//                exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(activity);
//                Uri video = Uri.parse(postUri);
//                DefaultHttpDataSourceFactory df = new DefaultHttpDataSourceFactory("video");
//                ExtractorsFactory ef = new DefaultExtractorsFactory();
//                MediaSource mediaSource= new ExtractorMediaSource(video,df,ef,null,null);
//                playerView.setPlayer(exoPlayer);
//                exoPlayer.prepare(mediaSource);
//                exoPlayer.setPlayWhenReady(false);

                Uri uri = Uri.parse(postUri);
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(activity);
                DefaultHttpDataSourceFactory df = new DefaultHttpDataSourceFactory("video");
                ExtractorsFactory ef = new DefaultExtractorsFactory();
                MediaSource mediaSource= new ExtractorMediaSource(uri,df,ef,null,null);
                playerView.setPlayer(player);
                player.prepare(mediaSource);
                player.setPlayWhenReady(false);



            }catch (Exception e){
                Toast.makeText(activity, "Error...", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public  void likesChecker(final  String postkey){

        likebtn = itemView.findViewById(R.id.likebutton_postlayout);
        likesref = database.getReference("post likes");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(uid)){
                    likebtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likescount = (int)snapshot.child(postkey).getChildrenCount();
                    tv_likes.setText(Integer.toString(likescount)+"likes");
                }
                else {
                    likebtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    likescount = (int)snapshot.child(postkey).getChildrenCount();
                    tv_likes.setText(Integer.toString(likescount)+"likes");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
