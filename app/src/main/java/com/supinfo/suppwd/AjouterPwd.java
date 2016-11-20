package com.supinfo.suppwd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AjouterPwd extends Activity {
    private boolean filePwdsValide = false;
    private JSONArray objetListePwds;
    EditText etxtTitre;
    EditText etxtUsername;
    EditText etxtPassword;
    private String titre;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_pwd);

        // id text titre, username, password
        etxtTitre = (EditText) findViewById(R.id.etxtTitre);
        etxtUsername = (EditText) findViewById(R.id.etxtUsername);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);

        // id du bouton ajouter
        Button btnAjouter = (Button) findViewById(R.id.btnAdd);

        // listener du bouton Ajouter
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pour récupérer la liste afin d'ajouter le nouvelle objet
                readFilePwd();

                // dans le cas ou readFilePwd() ne retournerais aucun fichier créer
                if (filePwdsValide == false) {
                    creerFilePwd();
                }

                // récupère les textes
                titre = etxtTitre.getText().toString();
                username = etxtUsername.getText().toString();
                password = etxtPassword.getText().toString();

                // ajoute le Pwd
                ajoutPwd(titre, username, password);

                // redirection sur la page main
                Intent intent = new Intent(AjouterPwd.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        // id du bouton cancel
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        // listener du bouton cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirection sur la page main
                Intent intent = new Intent(AjouterPwd.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }


    // lecture de la liste des Pwds
    public void readFilePwd(){
        try {
            FileInputStream fis = openFileInput("listePwds.json");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();

            objetListePwds = new JSONArray(b.toString());

            filePwdsValide = true;

        } catch (Exception e) {
            Log.e("LectureFileListePwds", e.getMessage());
            filePwdsValide = false;
        }
    }


    public void creerFilePwd(){

        try {

            JSONArray arrayPwds = new JSONArray();

            String txtArrayPwds = arrayPwds.toString(); // convertie l'objet json en string afin de pouvoir l'enregistrer dans un fichier

            Log.d("objetPwd", txtArrayPwds);

            FileOutputStream fos = openFileOutput("listePwds.json", MODE_PRIVATE);
            fos.write(txtArrayPwds.getBytes("ISO-8859-1"));
            fos.close();

            readFilePwd();
        } catch (Exception e) {
            Log.e("CreationFileListePwds", e.getMessage());
        }

    }


    // création du json du Pwd
    public void ajoutPwd(String titre, String username, String password) {

        try {

            JSONObject objetPwd = new JSONObject(); // initialise l'objet json
            objetPwd.put("titre", titre);
            objetPwd.put("username", username);
            objetPwd.put("password", password);

            objetListePwds.put(objetPwd); // ajoute le nouvelle objet a la liste des Pwds

            String txtListePwds = objetListePwds.toString();

            Log.d("objetPwd", txtListePwds);

            FileOutputStream fos = openFileOutput("listePwds.json", MODE_PRIVATE);
            fos.write(txtListePwds.getBytes("ISO-8859-1"));
        } catch (Exception e) {
            Log.e("AjoutPwd", e.getMessage());
        }

    }

    //détruit l'activité si clique sur le bouton physique de retour en arrière
    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
