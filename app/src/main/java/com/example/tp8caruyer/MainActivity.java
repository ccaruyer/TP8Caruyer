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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private TextView latitude;
    private TextView longitude;
    private EditText location;
    private ImageButton loadImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.imageView);
        latitude = findViewById(R.id.latitudePhoto);
        longitude = findViewById(R.id.longitudePhoto);
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
                                location.setText(imageUri.toString());
                                loadImage(imageUri);
                                setLocation(imageUri.toString());
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

    private void setLocation(String path){
        try {
            // Créer une instance ExifInterface en utilisant le chemin de la photo
            ExifInterface exifInterface = new ExifInterface(path);

            // Obtenir les coordonnées GPS
            float[] latLong = new float[2];
            if (exifInterface.getLatLong(latLong)) {
                // Les coordonnées GPS sont présentes
                float latitudeNb = latLong[0];
                float longitudeNb = latLong[1];

                // Afficher les coordonnées GPS

                latitude.setText(""+latitude);
                longitude.setText(""+longitude);
            } else {
                // Les coordonnées GPS ne sont pas présentes
                latitude.setText("Coordonnées inconnues");
                longitude.setText("Coordonnées inconnues");
            }

            // Afficher la photo dans ImageView
            // Vous devez charger la photo dans imageView en utilisant la bibliothèque de votre choix

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}