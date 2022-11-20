package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "ACCESS_TAG";
    private static final int REQES_LOCATION_PERMISSION = 1;
    TextView infoTextView;
    Button getLocalizationButton;
    Button getAdresButton;
    private Location lastLocation;
    private TextView locationTextView;
    private TextView adresTextView;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //infoTextView = findViewById(R.id.info_text_view);

        getLocalizationButton = findViewById(R.id.get_localization_button);
        getLocalizationButton.setOnClickListener( v -> getLocation() );

        getAdresButton = findViewById(R.id.get_adres_button);
        getAdresButton.setOnClickListener( v -> executeGeocoding() );

        locationTextView = findViewById(R.id.info_text_view);
        adresTextView = findViewById(R.id.adres_text_view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void getLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION},
                    REQES_LOCATION_PERMISSION);
        }
        else{
            //Log.d(TAG, "getLocation: Permission granted!");

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    location -> {
                        if(location != null){
                            lastLocation = location;
                            locationTextView.setText(getString(R.string.location_text,
                                                               location.getLatitude(),
                                                               location.getLongitude(),
                                                               location.getTime() ));
                        }
                        else{
                            locationTextView.setText(R.string.noLocation);
                        }


                    }
            );
        }
    }

    private String locationGeoCoding(Context context, Location location){

        /* Pierwsza część metody (na screenie powyżej) zawiera uruchomienie procesu geokodowania. W
            wyniku zwracana jest lista adresów. Ostatni parametr metody getFromLocation odpowiada za
            maksymalną liczbę adresów do odczytania. W bloku catch obsługiwane są ewentualne problemy
            z serwisem Geocoder.
        */

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> adresList = null;
        String resultMessage = "";

        try{
            adresList = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);

        }
        catch (IOException ioException) {
            resultMessage = context.getString(R.string.serviceNotAvailable);
            Log.e(TAG, resultMessage, ioException);
        }


        /* Druga część metody zawiera obsługę przypadku, w którym Geocoder nie będzie w stanie
            odnaleźć adresu dla podanych współrzędnych. Jeśli jednak uda się znaleźć adres, wszystkie jego
            linie zostaną złączone w jeden łańcuch znakowy. Wynikowy adres w postaci jednej linii tekstu
            jest zwracany w metodzie jako resultMessage.
        */
        if (adresList == null || adresList.isEmpty()) {
            if(resultMessage.isEmpty()){
                resultMessage = context.getString(R.string.noAdresFound);
                Log.e(TAG, resultMessage);
            }
        }
        else {
            Address address = adresList.get(0);
            List<String> adresParts = new ArrayList<>();

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                adresParts.add(address.getAddressLine(i));
            }

            resultMessage = TextUtils.join("\n", adresParts);
        }

        return resultMessage;
    }


    private void executeGeocoding() {
        /* Metoda ta wywołuje geokodowanie w oddzielnym wątku (odpowiada za to interfejs Executor.
            Zwrócenie wartości wynikowej z wątku odbywa się przy użyciu interfejsu Future.
        */

        if (lastLocation != null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Future<String> returnedAdres = executorService.submit( () -> locationGeoCoding(getApplicationContext(), lastLocation) );

            try{
                String result = returnedAdres.get();
                adresTextView.setText(getString(R.string.adresText, result, System.currentTimeMillis()));
            }
            catch (ExecutionException | InterruptedException exception) {
                Log.e(TAG, exception.getMessage(), exception);
                Thread.currentThread().interrupt();
            }

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQES_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}