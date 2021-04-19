package com.example.contactos;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactos.R;
import com.example.contactos.ApiClient;
import com.example.contactos.ApiInterface;
import com.example.contactos.Contact;
import com.thebluealliance.spectrum.SpectrumPalette;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {

    EditText et_telefono, et_nombre;
    ProgressDialog progressDialog;

    String telefono;
    String nombre;

    Menu actionMenu;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        et_telefono = findViewById(R.id.telefono);
        et_nombre = findViewById(R.id.nombre);

//      Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        apiInterface= ApiUtil.getAPI();

        Intent intent = getIntent();
        telefono = intent.getStringExtra("Telefono");
        nombre = intent.getStringExtra("Nombre");
        if(telefono==null) telefono="";
        if(nombre==null) nombre="";
        setDataFromIntentExtra();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if (telefono != "") {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String tel = et_telefono.getText().toString().trim();
        String nom = et_nombre.getText().toString().trim();

        switch (item.getItemId()) {
            case R.id.save:
                //Save
                if (tel.isEmpty()) {
                    et_telefono.setError("Please enter a name");
                } else if (nom.isEmpty()) {
                    et_nombre.setError("Please enter a telefone");
                } else {
                    apiInterface.saveContact(tel,nom).enqueue(new Callback<Contact>() {
                        @Override
                        public void onResponse(Call<Contact> call, Response<Contact> response) {

                            if(response.isSuccessful()) {
                                Log.i("sucess", response.body().toString());
                            } else {
                                Log.i("not success", response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Contact> call, Throwable t) {
                            Log.e("fail", "Unable to submit post to API.");
                        }
                    });
                }
                return true;

            case R.id.edit:

                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return true;

            case R.id.update:
                //Update

                if (tel.isEmpty()) {
                    et_telefono.setError("Please enter a telefone");
                } else if (nom.isEmpty()) {
                    et_nombre.setError("Please enter a name");
                } else {
                    apiInterface.updateContact(tel,nom).enqueue(new Callback<Contact>() {
                        @Override
                        public void onResponse(Call<Contact> call, Response<Contact> response) {

                            if(response.isSuccessful()) {
                                Log.i("sucess", response.body().toString());
                            } else {
                                Log.i("not success", response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Contact> call, Throwable t) {
                            Log.e("fail", "Unable to submit post to API.");
                        }
                    });
                }

                return true;

            case R.id.delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm !");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("Cancel",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.setNegativeButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                   apiInterface.deleteContact(tel).enqueue(new Callback<Contact>() {
                       @Override
                       public void onResponse(Call<Contact> call, Response<Contact> response) {

                           if(response.isSuccessful()) {
                               Log.i("sucess", response.body().toString());
                           } else {
                               Log.i("not success", response.body().toString());
                           }
                       }

                       @Override
                       public void onFailure(Call<Contact> call, Throwable t) {
                           Log.e("fail", "Unable to submit post to API.");
                       }
                   });
                });


                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.hide();
    }

    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish(); //back to main activity
    }

    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    private void setDataFromIntentExtra() {

        if (telefono != "") {
            et_telefono.setText(telefono);
            et_nombre.setText(nombre);

            getSupportActionBar().setTitle("Update Contact");
            readMode();
        } else {
            newMode();
        }

    }

    private void editMode() {
        et_telefono.setFocusableInTouchMode(false);
        et_nombre.setFocusableInTouchMode(true);
    }

    private void newMode() {
        et_telefono.setFocusableInTouchMode(true);
        et_nombre.setFocusableInTouchMode(true);
    }

    private void readMode() {
        et_telefono.setFocusableInTouchMode(false);
        et_nombre.setFocusableInTouchMode(false);
        et_telefono.setFocusable(false);
        et_nombre.setFocusable(false);
    }
}