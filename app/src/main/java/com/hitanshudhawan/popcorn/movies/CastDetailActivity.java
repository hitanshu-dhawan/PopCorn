package com.hitanshudhawan.popcorn.movies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView mCastImageView;
    private AVLoadingIndicatorView mProgressBar;
    private TextView mCastNameTextView;
    private TextView mCastAgeTextView;
    private TextView mCastBirthPlaceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(""); //todo

        Intent intent = getIntent();
        mCastId = intent.getIntExtra("cast_id",-1);

        mCastImageView = (ImageView) findViewById(R.id.image_view_cast_detail);
        mProgressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar_cast_detail);
        mCastNameTextView = (TextView) findViewById(R.id.text_view_name_cast_detail);
        mCastAgeTextView = (TextView) findViewById(R.id.text_view_age_cast_detail);
        mCastBirthPlaceTextView = (TextView) findViewById(R.id.text_view_birthplace_cast_detail);

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
                mCastBirthPlaceTextView.setText(response.body().getPlaceOfBirth());
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
