package net.ridhoperdana.jalan.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.ridhoperdana.jalan.drawer.BaseActivity;
import net.ridhoperdana.jalan.R;
import net.ridhoperdana.jalan.interface_retrofit.GetPlace;
import net.ridhoperdana.jalan.pojo_class.Results;
import net.ridhoperdana.jalan.pojo_class.Tempat;
import net.ridhoperdana.jalan.pojo_class.Tempat_sementara;
import net.ridhoperdana.jalan.recycler_view.CustomAdapter;
import net.ridhoperdana.jalan.recycler_view.CustomAdapterDipilihkan;
import net.ridhoperdana.jalan.recycler_view.CustomAdapterKonfirmasi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DipilihkanActivity extends BaseActivity {

    public Tempat places;

    List<Tempat_sementara> finalResultRestaurant = new ArrayList<>();
    List<Tempat_sementara> finalResultShopping = new ArrayList<>();
    List<Tempat_sementara> finalResultWisata = new ArrayList<>();
    List<Tempat_sementara> finalResultWorship = new ArrayList<>();
    ArrayList<Results> tampung_final = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dipilihkan);


        Bundle bundle = getIntent().getBundleExtra("bundle");
        final Double lat, longt;
        lat = Double.parseDouble(bundle.getString("lat"));
        longt = Double.parseDouble(bundle.getString("long"));
        try {
            getRetrofitObject(lat, longt);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.d("tmp: ", String.valueOf(tampung_final.size()));
    }

    private void getRetrofitObject(final Double lat, final Double longt) throws IOException {

        places = new Tempat();
        final ArrayList<Tempat_sementara> tampung = new ArrayList<>();
        final List<Results> tampung2 = new ArrayList<>();

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
//                    tampung.addAll(getRetrofitObject2(lat, longt));
                    finalResultRestaurant = getRetrofitRestaurant(lat, longt);
                    finalResultShopping = getRetrofitShopping(lat, longt);
                    finalResultWisata = getRetrofitWisata(lat, longt);
                    finalResultWorship = getRetrofitObjectWorship(lat, longt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                tampung.addAll(finalResultRestaurant);
                tampung.addAll(finalResultShopping);
                tampung.addAll(finalResultWisata);
                tampung.addAll(finalResultWorship);
                for(int i=0; i<tampung.size(); i++)
                {
                    Log.d("nama lokasi final: ", tampung.get(i).getNama_tempat());
                }
                RecyclerView recyclerView;
                LinearLayoutManager linearLayoutManager;
                recyclerView = (RecyclerView)findViewById(R.id.rv_konfirmasi);
                CustomAdapterDipilihkan adapter = new CustomAdapterDipilihkan(tampung, getApplicationContext());
                recyclerView.setAdapter(adapter);
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                Button button = (Button)findViewById(R.id.tombol_konfirmasi_benar);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DipilihkanActivity.this, MenghitungJalanActivity.class);
                        Bundle bundle1 = new Bundle();
                        intent.putParcelableArrayListExtra("pilihan", tampung);
                        startActivity(intent);
                    }
                });
            }
        };
        task.execute();
    }

    private ArrayList<Tempat_sementara> getRetrofitRestaurant(Double lat, Double longt) throws IOException {

        places = new Tempat();
        final ArrayList<Tempat_sementara> tampung = new ArrayList<>();
        Tempat_sementara tempat_sementara;

        StringBuilder urlbaru = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        urlbaru.append("location=" + lat + "," + longt);
        urlbaru.append("&radius=" + 5000);
        urlbaru.append("&types=" + "restaurant|cafe|bar|bakery");
        urlbaru.append("&rankBy=" + "prominence");
        urlbaru.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ridhoperdana.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetPlace service = retrofit.create(GetPlace.class);
        Call<Tempat> call = service.getPlaceResponseFood(urlbaru.toString());

        places = call.execute().body();
        Random r = new Random();
        int random_number = r.nextInt(5 - 1 + 1) + 1;
        tempat_sementara = new Tempat_sementara(places.getResults().get(random_number).getName(),
                places.getResults().get(random_number).getVicinity(),
                places.getResults().get(random_number).getGeometry().getLocation().getLat(),
                places.getResults().get(random_number).getGeometry().getLocation().getLng());
        tampung.add(tempat_sementara);
        return tampung;
    }

    private ArrayList<Tempat_sementara> getRetrofitShopping(Double lat, Double longt) throws IOException {

        places = new Tempat();
        final ArrayList<Tempat_sementara> tampung = new ArrayList<>();
        Tempat_sementara tempat_sementara;

        StringBuilder urlbaru = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        urlbaru.append("location=" + lat + "," + longt);
        urlbaru.append("&radius=" + 5000);
        urlbaru.append("&types=" + "bookstore|clothing_store|shopping_mall|store");
        urlbaru.append("&rankBy=" + "prominence");
        urlbaru.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ridhoperdana.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetPlace service = retrofit.create(GetPlace.class);
        Call<Tempat> call = service.getPlaceResponseFood(urlbaru.toString());

        places = call.execute().body();
        Random r = new Random();
        int random_number = r.nextInt(5 - 1 + 1) + 1;
        tempat_sementara = new Tempat_sementara(places.getResults().get(random_number).getName(),
                places.getResults().get(random_number).getVicinity(),
                places.getResults().get(random_number).getGeometry().getLocation().getLat(),
                places.getResults().get(random_number).getGeometry().getLocation().getLng());
        tampung.add(tempat_sementara);
        return tampung;
    }

    private ArrayList<Tempat_sementara> getRetrofitWisata(Double lat, Double longt) throws IOException {

        places = new Tempat();
        final ArrayList<Tempat_sementara> tampung = new ArrayList<>();
        Tempat_sementara tempat_sementara;

        StringBuilder urlbaru = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        urlbaru.append("location=" + lat + "," + longt);
        urlbaru.append("&radius=" + 5000);
        urlbaru.append("&types=" + "zoo|amusement_park|stadium|museum|art_gallery|night_club");
        urlbaru.append("&rankBy=" + "prominence");
        urlbaru.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ridhoperdana.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetPlace service = retrofit.create(GetPlace.class);
        Call<Tempat> call = service.getPlaceResponseFood(urlbaru.toString());

        places = call.execute().body();
        Random r = new Random();
        int random_number = r.nextInt(5 - 1 + 1) + 1;
        tempat_sementara = new Tempat_sementara(places.getResults().get(random_number).getName(),
                places.getResults().get(random_number).getVicinity(),
                places.getResults().get(random_number).getGeometry().getLocation().getLat(),
                places.getResults().get(random_number).getGeometry().getLocation().getLng());
        tampung.add(tempat_sementara);
        return tampung;
    }

    private ArrayList<Tempat_sementara> getRetrofitObjectWorship(Double lat, Double longt) throws IOException {

        places = new Tempat();
        final ArrayList<Tempat_sementara> tampung = new ArrayList<>();
        Tempat_sementara tempat_sementara;

        StringBuilder urlbaru = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        urlbaru.append("location=" + lat + "," + longt);
        urlbaru.append("&radius=" + 5000);
        urlbaru.append("&types=" + "mosque|church");
        urlbaru.append("&rankBy=" + "prominence");
        urlbaru.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ridhoperdana.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetPlace service = retrofit.create(GetPlace.class);
        Call<Tempat> call = service.getPlaceResponseFood(urlbaru.toString());

        Random r = new Random();
        int random_number = r.nextInt(5 - 1 + 1) + 1;
        places = call.execute().body();
        tempat_sementara = new Tempat_sementara(places.getResults().get(random_number).getName(),
                places.getResults().get(random_number).getVicinity(),
                places.getResults().get(random_number).getGeometry().getLocation().getLat(),
                places.getResults().get(random_number).getGeometry().getLocation().getLng());
        tampung.add(tempat_sementara);
        return tampung;
    }
}
