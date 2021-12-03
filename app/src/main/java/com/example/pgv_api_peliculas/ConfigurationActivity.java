package com.example.pgv_api_peliculas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ConfigurationActivity extends AppCompatActivity {
    private SharedPreferences preferencias;
    private EditText editTextApiKey;
    private EditText editTextPeliculas;
    private EditText editTextCreditos;
    private Button btbGuardar;

    private TextInputLayout txtInputLayoutApiKey, txtInputLayoutPeliculas, txtInputLayoutCreditos;
    private boolean hayErrorApiKey = false;
    private boolean hayErrorPeliculas = false;
    private boolean hayErrorCreditos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        setTitle("Configuración");

        //Obtenemos una referencia a los controles de la interfaz
        editTextApiKey = findViewById(R.id.editTextApiKey);
        editTextPeliculas = findViewById(R.id.editTextPeliculas);
        editTextCreditos = findViewById(R.id.editTextCreditos);
        btbGuardar = (Button) findViewById(R.id.btbGuardar);

        txtInputLayoutApiKey = (TextInputLayout) findViewById(R.id.TiLayoutApiKey);
        txtInputLayoutApiKey.setErrorEnabled(true);
        txtInputLayoutPeliculas = (TextInputLayout) findViewById(R.id.TiLayoutPeliculas);
        txtInputLayoutPeliculas.setErrorEnabled(true);
        txtInputLayoutCreditos = (TextInputLayout) findViewById(R.id.TiLayoutCreditos);
        txtInputLayoutCreditos.setErrorEnabled(true);

        //Recogemos los datos de pasados de la otra activity
        Bundle p= getIntent().getExtras();
        editTextApiKey.setText(p.getString("api"));
        editTextPeliculas.setText(p.getString("epp"));

        // Preferencias.
        preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);

        btbGuardar.setOnClickListener(e->{
            // Guardar preferencias y salir.
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("api_key", editTextApiKey.getText().toString());
            editor.commit();

            //mensaje flotante temporal
            Toast t = Toast.makeText(getApplicationContext(), "Configuración guardada correctamente.", Toast.LENGTH_SHORT);
            t.show();

            finish();
        });
    }
}
/*
        shared preferences

        GUARDAR PREFERENCIAS.

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Configuracion", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString("api", API.getText().toString());
        myEdit.putString("api_key", APIKEY.getText().toString());
        myEdit.putString("url_images", name.getText().toString());

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();

        LEER PREFERENCIAS

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String s1 = sh.getString("name", "");
        int a = sh.getInt("age", 0);

        // We can then use the data
        name.setText(s1);
        age.setText(String.valueOf(a));
        */