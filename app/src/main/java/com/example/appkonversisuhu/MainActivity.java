package com.example.appkonversisuhu;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etInputTemperature;
    private Spinner spinnerTemperatureUnit;
    private TextView tvResult;
    private Button btnConvert, btnClear;
    private TextView universitas, mercuBuana;
    private ImageView logoUMB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout pertama kali ke activity_main.xml
        setContentView(R.layout.activity_main);

        // Inisialisasi elemen-elemen UI dan set visibility ke GONE
        logoUMB = findViewById(R.id.logoUMB);
        universitas = findViewById(R.id.universitas);
        mercuBuana = findViewById(R.id.mercuBuana);

        logoUMB.setVisibility(View.GONE);
        universitas.setVisibility(View.GONE);
        mercuBuana.setVisibility(View.GONE);

        // Animasi untuk layout pertama (activity_main.xml)
        startMainActivityAnimations();

        // Setelah 4 detik, pindah ke layout konversi_layout.xml
        new Handler().postDelayed(this::showConversionLayout, 4000); // 4000 ms = 4 detik
    }

    // Method untuk menampilkan layout konversi_layout.xml tanpa animasi
    private void showConversionLayout() {
        // Pindah ke layout konversi_layout.xml setelah animasi selesai
        setContentView(R.layout.konversi_layout);

        // Inisialisasi semua elemen di layout konversi_layout.xml
        etInputTemperature = findViewById(R.id.etInputTemperature);
        spinnerTemperatureUnit = findViewById(R.id.spinnerTemperatureUnit);
        tvResult = findViewById(R.id.tvResult);
        btnConvert = findViewById(R.id.btnConvert);
        btnClear = findViewById(R.id.btnClear);

        // Set pilihan pada Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.temperature_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemperatureUnit.setAdapter(adapter);

        // Set tombol konversi
        btnConvert.setOnClickListener(v -> {
            String input = etInputTemperature.getText().toString();
            String selectedUnit = spinnerTemperatureUnit.getSelectedItem().toString();

            if (!input.isEmpty()) {
                try {
                    double temperature = Double.parseDouble(input);
                    showConversionResults(temperature, selectedUnit);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Silahkan Masukkan Suhu", Toast.LENGTH_SHORT).show();
            }
        });

        // Set tombol clear
        btnClear.setOnClickListener(v -> {
            etInputTemperature.setText("");
            tvResult.setVisibility(View.GONE);
        });
    }

    // Method untuk animasi layout pertama (activity_main.xml)
    private void startMainActivityAnimations() {
        // Animasi logo muncul dari atas ke bawah
        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(logoUMB, "translationY", -500f, 0f);
        logoAnimator.setDuration(4000); // Durasi animasi logo 1 detik
        logoAnimator.start();

        // Animasi teks "UNIVERSITAS" muncul dari bawah ke atas
        ObjectAnimator universitasAnimator = ObjectAnimator.ofFloat(universitas, "translationY", 500f, 0f);
        universitasAnimator.setDuration(2000); // Durasi animasi teks 1 detik
        universitasAnimator.setStartDelay(1000); // Delay sedikit agar logo muncul dulu
        universitasAnimator.start();

        // Animasi teks "MERCU BUANA" muncul dari bawah ke atas
        ObjectAnimator mercuBuanaAnimator = ObjectAnimator.ofFloat(mercuBuana, "translationY", 500f, 0f);
        mercuBuanaAnimator.setDuration(2000); // Durasi animasi teks 1 detik
        mercuBuanaAnimator.setStartDelay(2000); // Delay agar teks muncul setelah "UNIVERSITAS"
        mercuBuanaAnimator.start();

        // Setelah animasi selesai, baru tampilkan elemen-elemen tersebut
        new Handler().postDelayed(() -> {
            logoUMB.setVisibility(View.VISIBLE);
            universitas.setVisibility(View.VISIBLE);
            mercuBuana.setVisibility(View.VISIBLE);
        }, 2000); // Delay 2 detik setelah animasi selesai
    }

    private void showConversionResults(double temperature, String unit) {
        double celsius, fahrenheit, kelvin, reamur;

        switch (unit) {
            case "Celsius":
                celsius = temperature;
                fahrenheit = (celsius * 9 / 5) + 32;
                kelvin = celsius + 273.15;
                reamur = celsius * 4 / 5;
                break;
            case "Fahrenheit":
                fahrenheit = temperature;
                celsius = (fahrenheit - 32) * 5 / 9;
                kelvin = celsius + 273.15;
                reamur = celsius * 4 / 5;
                break;
            case "Kelvin":
                kelvin = temperature;
                celsius = kelvin - 273.15;
                fahrenheit = (celsius * 9 / 5) + 32;
                reamur = celsius * 4 / 5;
                break;
            case "Reamur":
                reamur = temperature;
                celsius = reamur * 5 / 4;
                fahrenheit = (celsius * 9 / 5) + 32;
                kelvin = celsius + 273.15;
                break;
            default:
                throw new IllegalStateException("Unexpected unit: " + unit);
        }

        // Tampilkan hasil konversi
        String resultText = String.format(
                "Celsius: %.2f°C\nFahrenheit: %.2f°F\nKelvin: %.2fK\nReamur: %.2f°R\n\n" +
                        "Perhitungan:\n" +
                        "Celsius: %s\n" +
                        "Fahrenheit: %.2f = (%.2f × 9/5) + 32\n" +
                        "Kelvin: %.2f = %.2f + 273.15\n" +
                        "Reamur: %.2f = %.2f × 4/5",
                celsius, fahrenheit, kelvin, reamur,
                unit,
                fahrenheit, celsius, kelvin, celsius, reamur, celsius
        );

        tvResult.setText(resultText);
        tvResult.setVisibility(View.VISIBLE);
    }
}
