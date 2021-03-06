package net.ridhoperdana.jalan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.ridhoperdana.jalan.R;
import net.ridhoperdana.jalan.drawer.BaseActivity;
import net.ridhoperdana.jalan.pojo_class.Tempat_sementara;
import net.ridhoperdana.jalan.recycler_view.CustomAdapter;
import net.ridhoperdana.jalan.recycler_view.CustomAdapterKonfirmasi;

import java.util.ArrayList;

public class KonfirmasiActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        RecyclerView recyclerView;
        Button tombolKonfirmasiBenar;
        tombolKonfirmasiBenar = (Button)findViewById(R.id.tombol_konfirmasi_benar);
        LinearLayoutManager linearLayoutManager;
        final ArrayList<Tempat_sementara> getObject;
        getObject = getIntent().getParcelableArrayListExtra("list");
        for(int i=0; i<getObject.size(); i++)
        {
            Log.d("nama: " + getObject.get(i).getNama_tempat(), "Lat: " + getObject.get(i).getLat_tempat());
        }
        recyclerView = (RecyclerView)findViewById(R.id.rv_konfirmasi);
        CustomAdapterKonfirmasi adapter = new CustomAdapterKonfirmasi(getObject, this);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        tombolKonfirmasiBenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonfirmasiActivity.this, MenghitungJalanActivity.class);
                Bundle bundle1 = new Bundle();
                intent.putParcelableArrayListExtra("pilihan", getObject);
                startActivity(intent);
            }
        });
    }
}
