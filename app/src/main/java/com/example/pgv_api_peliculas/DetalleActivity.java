package com.example.pgv_api_peliculas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {
    private Button btnActores;
    private ImageView imgPelicula;
    private TextView txtTitulo;
    private TextView txtSinopsis;
    public static final String MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        //setTitle("Detalles de la película");

        //Obtenemos una referencia a los controles de la interfaz
        btnActores = (Button) findViewById(R.id.btnActores);
        imgPelicula = (ImageView) findViewById(R.id.imgPelicula);
        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtSinopsis = (TextView) findViewById(R.id.txtSinopsis);

        // Poner el boton de vuelta atrás.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Leer la informacion pasada por la activity anterior
        Bundle p = getIntent().getExtras();
        setTitle(p.getString("titulo"));
        txtTitulo.setText(p.getString("titulo"));
        txtSinopsis.setText(p.getString("sinopsis"));
        Picasso.get().load(MOVIE_BASE_URL + p.getString("imagen")).into(imgPelicula);

        btnActores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActoresActivity.class);
                intent.putExtra("id", p.getString("id"));
                intent.putExtra("titulo", p.getString("titulo"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}