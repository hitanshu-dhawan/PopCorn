package com.hitanshudhawan.popcorn.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitanshudhawan.popcorn.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView featureGraphicImageView;

    private ImageButton shareImageButton;
    private ImageButton rateUsImageButton;
    private ImageButton feedbackImageButton;

    private CardView sourceCodeOnGitHubCardView;

    private FrameLayout openSourceLicensesFrameLayout;
    private TextView versionNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.about);

        featureGraphicImageView = (ImageView) findViewById(R.id.image_view_feature_graphic_about);
        featureGraphicImageView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
        featureGraphicImageView.getLayoutParams().height = (int)((double)getResources().getDisplayMetrics().widthPixels * (500.0/1024.0));

        shareImageButton = (ImageButton) findViewById(R.id.image_button_share_about);
        rateUsImageButton = (ImageButton) findViewById(R.id.image_button_rate_us_about);
        feedbackImageButton = (ImageButton) findViewById(R.id.image_button_feedback_about);

        sourceCodeOnGitHubCardView = (CardView) findViewById(R.id.card_view_source_code_on_github);

        openSourceLicensesFrameLayout = (FrameLayout) findViewById(R.id.frame_layout_open_source_licenses);
        versionNumberTextView = (TextView) findViewById(R.id.text_view_version_number);

        loadActivity();

    }

    private void loadActivity() {

        shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String packageName = getApplicationContext().getPackageName();
                Intent appShareIntent = new Intent(Intent.ACTION_SEND);
                appShareIntent.setType("text/plain");
                String extraText = "Hey! Check out this amazing app.\n";
                extraText += "https://play.google.com/store/apps/details?id=" + packageName;
                appShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(appShareIntent);
            }
        });

        rateUsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String packageName = getApplicationContext().getPackageName();
                String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
                Intent appRateUsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
                startActivity(appRateUsIntent);
            }
        });

        feedbackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
                feedbackIntent.setData(Uri.parse("mailto:"));
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {"hitanshudhawan1996@gmail.com"});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback: " + getResources().getString(R.string.app_name));
                startActivity(feedbackIntent);
            }
        });

        sourceCodeOnGitHubCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String githubLink = "https://github.com/hitanshu-dhawan/" + getResources().getString(R.string.app_name);
                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                startActivity(githubIntent);
            }
        });

        openSourceLicensesFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String attributionsLink = "https://github.com/hitanshu-dhawan/" + getResources().getString(R.string.app_name) + "/blob/master/ATTRIBUTIONS.md";
                Intent attributionsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(attributionsLink));
                startActivity(attributionsIntent);
            }
        });

        try {
            versionNumberTextView.setText((getPackageManager().getPackageInfo(getPackageName(), 0)).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
