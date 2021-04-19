
package com.example.contactos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.contactos.R;
import com.example.contactos.EditorActivity;
import com.example.contactos.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    ArrayList<String> arrayList;
    Map<String, Integer> m = new HashMap<>();
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ListView list;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = ApiUtil.getAPI();
        list = findViewById(R.id.list);
        updateData();
        recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.add);
        fab.setOnClickListener(view ->
                startActivityForResult(
                        new Intent(this, EditorActivity.class),
                        INTENT_ADD)
        );
    }
    public void finishUI(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String clickedItem=(String) list.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this,clickedItem,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("Telefono", String.valueOf(m.get((String) list.getItemAtPosition(position))));
                intent.putExtra("Nombre", (String) list.getItemAtPosition(position));
                startActivityForResult(
                        intent,
                        INTENT_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateData();
/*
        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            //presenter.getData(); //reload data
        }
        else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            //presenter.getData(); //reload data
        }
*/
    }
    public void updateData(){
        arrayList = new ArrayList<>();
        apiInterface.getContacts().enqueue(new Callback<List<Contact>>(){

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> array = response.body();

                if(response.isSuccessful()) {
                    for (Contact contact : array) {
                        if (contact!=null) {
                            arrayList.add(contact.getNombre());
                            m.put(contact.getNombre(), contact.getTelefono());
                        }
                    }
                    finishUI();
                }
                else
                    Log.i("response unsuccessful",response.toString());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.e("API ERROR", "Unable to submit post to API.");
            }
        });
    }
    public void onGetResult(List<Contact> contacts) {
        /*
        adapter = new MainAdapter(this, notes, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
        */
    }


    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}