package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hitanshudhawan.popcorn.R;
import com.hitanshudhawan.popcorn.network.ApiClient;
import com.hitanshudhawan.popcorn.network.ApiInterface;
import com.hitanshudhawan.popcorn.network.movies.Cast;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastDetailActivity extends AppCompatActivity {

    private int mCastId;

    private CardView mCastImageCardView;
    private int mCastImageSideSize;
    private ImageView mCastImageView;
    private AVLoadingIndicatorView mProgressBar;
    private TextView mCastNameTextView;
    private TextView mCastAgeTextView;
    private TextView mCastBirthPlaceTextView;

    private TextView mCastBioHeaderTextView;
    private TextView mCastBioTextView;
    private TextView mCastReadMoreBioTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(""); //todo

        Intent intent = getIntent();
        mCastId = intent.getIntExtra("cast_id",-1);

        mCastImageCardView = (CardView) findViewById(R.id.card_view_cast_detail);
        mCastImageSideSize = (int)(getResources().getDisplayMetrics().widthPixels * 0.33);
        mCastImageCardView.getLayoutParams().height = mCastImageSideSize;
        mCastImageCardView.getLayoutParams().width = mCastImageSideSize;
        mCastImageCardView.setRadius(mCastImageSideSize/2);
        mCastImageView = (ImageView) findViewById(R.id.image_view_cast_detail);
        mProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_cast_detail);
        mCastNameTextView = (TextView) findViewById(R.id.text_view_name_cast_detail);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mCastNameTextView.getLayoutParams();
        params.setMargins(params.leftMargin,mCastImageSideSize/3,params.rightMargin,params.bottomMargin);
        mCastAgeTextView = (TextView) findViewById(R.id.text_view_age_cast_detail);
        mCastBirthPlaceTextView = (TextView) findViewById(R.id.text_view_birthplace_cast_detail);

        mCastBioHeaderTextView = (TextView) findViewById(R.id.text_view_bio_header_cast_detail);
        mCastBioTextView = (TextView) findViewById(R.id.text_view_bio_cast_detail);
        mCastReadMoreBioTextView = (TextView) findViewById(R.id.text_view_read_more_cast_detail);

        loadActivity();

    }

    private void loadActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Cast> call = apiService.getCastDetails(mCastId, getResources().getString(R.string.MOVIE_DB_API_KEY));
        call.enqueue(new Callback<Cast>() {
            @Override
            public void onResponse(Call<Cast> call, Response<Cast> response) {
                if(response.code() != 200) return;
                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w1000/" + response.body().getProfilePath())
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                mProgressBar.hide();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mProgressBar.hide();
                                return false;
                            }
                        })
                        .into(mCastImageView);
                mCastNameTextView.setText(response.body().getName());
                setAge(response.body().getDateOfBirth());
                if(response.body().getPlaceOfBirth() != null)
                    mCastBirthPlaceTextView.setText(response.body().getPlaceOfBirth());

                if(response.body().getBiography() != null && !response.body().getBiography().trim().equals("")) {
                    mCastBioHeaderTextView.setVisibility(View.VISIBLE);
                    mCastReadMoreBioTextView.setVisibility(View.VISIBLE);
                    mCastBioTextView.setText(response.body().getBiography());
                    mCastReadMoreBioTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCastBioTextView.setMaxLines(Integer.MAX_VALUE);
                            mCastReadMoreBioTextView.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Cast> call, Throwable t) {

            }
        });
    }

    private void setAge(String dateOfBirthString) {
        if(dateOfBirthString != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(dateOfBirthString);
                mCastAgeTextView.setText((Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(sdf2.format(releaseDate)))+"");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
