package com.example.pgv_api_peliculas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //private Button btnObtener;
    //private TextView txtPeliculas;
    private ArrayList<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
    private ListView listView;

    //CONFIGURACION
    public static final String endPointPeliculas = "http://api.themoviedb.org/3/discover/movie?api_key=4c0eb811755c3401c348bf7ad9b982d3&language=es";
    //public static final String endPointPeliculas = "http://api.themoviedb.org/3/discover/movie?api_key=1865f43a0549ca50d341dd9ab8b29f49&language=es";
    public static final String MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    //----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boton y textView para pruebas
        //btnObtener = (Button) findViewById(R.id.btnObtener);
        //txtPeliculas = (TextView) findViewById(R.id.txtPeliculas);
        listView = (ListView) findViewById(R.id.listView);

        //Obtengo las peliculas
        new ObtenerPeliculasAsync().execute(endPointPeliculas);
        /*
        //on click para pruebas, saco el Obtenerpeliclas para que caruen al abrir la app
        btnObtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //27:00
                new ObtenerPeliculasAsync().execute(endPointPeliculas);
                txtPeliculas.append("Cargando las películas");
            }
        });
        /*
        //Expresion landa
        btnObtener.setOnClickListener( e -> {
            txtPeliculas.append("Cargando las películas");
        });
        */
        //Paso a la segunda Activity (DetalleActivity)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), DetalleActivity.class);
                intent.putExtra("id", String.valueOf (listaPeliculas.get(position).getId() ) );
                intent.putExtra("titulo", listaPeliculas.get(position).getTitle() );
                intent.putExtra("imagen", listaPeliculas.get(position).getPoster_path() );
                intent.putExtra("sinopsis", listaPeliculas.get(position).getOverview() );

                Log.d("test", "Pasando id " + listaPeliculas.get(position).getId() );

                startActivity(intent);
            }
        });
    }

    //menu
    public boolean onCreateOptionsMenu(Menu menu){
        //AÑADIENDO EL MENU Y HACIENDOLO VISIBLE
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuConfiguracion:
                intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                intent.putExtra("api", endPointPeliculas.substring(51));
                intent.putExtra("epp", endPointPeliculas);
                startActivity(intent);
                return true;

            case R.id.mnuAcerca:
                intent = new Intent(getApplicationContext(), AcercaDeActivity.class);
                startActivity(intent);

                return true;
            case R.id.mnuSalir:
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //clase para obtener peliculas  proceso asíncrono
    class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progreso;

        protected void onPreExecute (){
            super.onPreExecute();

            // Mostrar progress bar.
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Obteniendo peliculas ...");
            progreso.setCancelable(false);
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }

        //me bajo Json de las peliculas y se lo paso al onPostExecute
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try{                //url que voy a usar
                URL urlObj = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                //Leo todo
                while ((line = reader.readLine()) != null) result.append(line);

                //Muestro por consola
                Log.d("test", "respuesta: " + result.toString());

            } catch (Exception e) {
                Log.d("test", "error2: " + e.toString());
            }
            return result.toString();
        }

        protected void onProgressUpdate(Integer...a){
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {
            JSONObject resp = null;
            JSONArray peliculas = null;

            try{
                resp = new JSONObject(result);
                peliculas = resp.getJSONArray("results");

                for (int i=0; i<peliculas.length(); i++) {
                    JSONObject pelicula = peliculas.getJSONObject(i);

                    //txtPeliculas.append( pelicula.getString("original_title") + "\n" );

                    //Añadir las peliculas al array que hemos creado (listaPeliculas)
                    listaPeliculas.add(new Pelicula(
                            pelicula.getInt("id"),
                            pelicula.getString("title"),
                            pelicula.getString("backdrop_path"),
                            pelicula.getString("poster_path"),
                            pelicula.getString("original_title"),
                            pelicula.getString("overview"),
                            pelicula.getDouble("popularity"),
                            pelicula.getString("release_date") ));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            try{
                Thread.sleep(1000); //pausa de 1 segundo
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            //Cierra el proceso
            progreso.dismiss();

            AdaptadorPelicula adaptador = new AdaptadorPelicula(getApplicationContext(), listaPeliculas);
            listView.setAdapter(adaptador);
        }
    }

    class AdaptadorPelicula extends BaseAdapter {
        Context context;
        ArrayList<Pelicula> arrayList;

        public AdaptadorPelicula(Context context, ArrayList<Pelicula> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }
        public Pelicula getItem(int position) {
            return arrayList.get(position);
        }
        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lista_pelicula, parent, false);
            }

            // Titulo
            TextView fecha = (TextView) convertView.findViewById(R.id.tvFecha);
            fecha.setText(arrayList.get(position).getRelease_date());

            // Titulo
            TextView name = (TextView) convertView.findViewById(R.id.tvTitle);
            name.setText(arrayList.get(position).getTitle());

            // Titulo
            TextView descripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
            descripcion.setText(arrayList.get(position).getOverview().substring(0,100) + " ... ");

            // Imagen
            ImageView imagen = (ImageView) convertView.findViewById(R.id.list_image);
            Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getBackdrop_path()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);

            return convertView;
        }
    }
}