package com.example.locsaleapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.CommentActivity;
import com.example.locsaleapplication.FollowersActivity;
import com.example.locsaleapplication.Fragments.PostDetailFragment;
import com.example.locsaleapplication.Fragments.ProfileFragment;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.utils.AppGlobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("All")
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    private FirebaseUser firebaseUser;
    private String type;
    private boolean isClickable;

    public PostAdapter(Context mContext, List<Post> mPosts, boolean isClickable) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        this.isClickable = isClickable;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Post post = mPosts.get(position);
        AppGlobal.loadImage(mContext, post.getImageUrl(), 700, holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageurl().equals("default")) {
                    holder.imageProfile.setImageResource(R.drawable.ic_profile);
                } else {
                    AppGlobal.loadImageUser(mContext, user.getImageurl(), 300, holder.imageProfile);
                }

                holder.username.setText(user.getBusiness_name());
                holder.author.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.isLiked(post.getPostId(), holder.like);
        noOfLikes(post.getPostId(), holder.noOfLikes);
        getComments(post.getPostId(), holder.noOfComments);
        holder.isSaved(post.getPostId(), holder.save);
        holder.isPromoted(post.getPostId(), holder.promotedPost, holder.mainPostBackground);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("Like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(firebaseUser.getUid())
                            .setValue(true);
                    addNotification(post.getPostId(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(firebaseUser.getUid())
                            .removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();
                ((FragmentActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new ProfileFragment()).addToBackStack(null).commit();

            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();
                ((FragmentActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new ProfileFragment()).addToBackStack(null).commit();

            }
        });

        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();
                ((FragmentActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new ProfileFragment()).addToBackStack(null).commit();

            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postId", post.getPostId())
                            .apply();
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, new PostDetailFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id", post.getPublisher());
                intent.putExtra("title", "likes");
                mContext.startActivity(intent);*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        //public ImageView more;
        public TextView promotedPost;

        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialTextView description;

        public RelativeLayout mainPostBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            promotedPost = itemView.findViewById(R.id.promoted_post);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);

            mainPostBackground = itemView.findViewById(R.id.main_post_background);
        }

        private void isSaved(final String postId, final ImageView image) {
            FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(postId).exists()) {
                                image.setImageResource(R.drawable.ic_save_black);
                                image.setTag("saved");
                            } else {
                                image.setImageResource(R.drawable.ic_save);
                                image.setTag("save");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private void isLiked(String postId, final ImageView imageView) {
            FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        imageView.setImageResource(R.drawable.liked);
                        imageView.setTag("Liked");
                    } else {
                        imageView.setImageResource(R.drawable.ic_like);
                        imageView.setTag("Like");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void isPromoted(String postId, final TextView promotedPost, final RelativeLayout mainPostBackground) {
            FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                        type = map.get("type");
                        if (type.equals("2")) {
                            promotedPost.setVisibility(View.GONE);
                            mainPostBackground.setBackgroundResource(R.drawable.post_background);
                        } else {
                            promotedPost.setVisibility(View.VISIBLE);
                            mainPostBackground.setBackgroundResource(R.drawable.promoted_post_background);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void noOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1) {
                    text.setText(dataSnapshot.getChildrenCount() + " like");
                } else {
                    text.setText(dataSnapshot.getChildrenCount() + " likes");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 1) {
                            text.setText("View All " + dataSnapshot.getChildrenCount() + " comment");
                        } else {
                            text.setText("View All " + dataSnapshot.getChildrenCount() + " comments");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void addNotification(String postId, String publisherId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", firebaseUser.getUid());
        map.put("test", "Liked your post");
        map.put("postid", postId);
        map.put("isPost", true);
        map.put("is_read",false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherId).push().setValue(map);
    }
}
