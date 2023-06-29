package com.example.tp8caruyer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private TextView latitude;
    private TextView longitude;
    private TextView location;
    private ImageButton loadImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.imageView);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        loadImage = findViewById(R.id.imageButton);

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                //textView.setText(imageUri.toString());
                                loadImage(imageUri);
                            }
                        }
                    }
                }
        );
    }

    //permet de d'ouvrir la galerie et de choisir une image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    //permet de charger une image
    private void loadImage(Uri imageUri) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        try {
            Bitmap bmOriginal = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
            image.setImageBitmap(bmOriginal);
        } catch (Exception e) {
            e.printStackTrace();
            //textView.setText("Erreur lors du chargement de l'image");
        }
    }

}