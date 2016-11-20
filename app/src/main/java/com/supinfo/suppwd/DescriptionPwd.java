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
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class DescriptionPwd extends Activity {
    private boolean filePwdValide = false;
    private JSONArray objetListePwds;
    private Integer positionPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_pwd);

        String Extra_Pwd = null;

        // récupère le titre de la Pwd sélectionné
        Intent intent = getIntent();
        String titrePwd = intent.getStringExtra(Extra_Pwd);

        // id du bouton ajouter
        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        // listener du bouton delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // supprimer le Pwd
                supprimerPwd();

                // redirection sur la page main
                Intent intent = new Intent(DescriptionPwd.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        readFilePwd();

        recherchePwd(titrePwd);

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

            filePwdValide = true;

        } catch (Exception e) {
            Log.e("LectureFileListePwds", e.getMessage());
            filePwdValide = false;
        }
    }


    public void recherchePwd(String titre){

       Integer nbrPwds = objetListePwds.length();

       try{

           for (int i = 0; i < nbrPwds; i++) {

               String titrePwd = objetListePwds.getJSONObject(i).getString("titre");

               if ( titrePwd.equals(titre) ){
                   positionPwd = i;
                   String username = objetListePwds.getJSONObject(i).getString("username");
                   String password = objetListePwds.getJSONObject(i).getString("password");

                   afficherPwd(titre, username, password);
               }

           }

       } catch (Exception e) {
           Log.e("DonneePwd", e.getMessage());
       }


    }


    public void afficherPwd(String titre, String username, String password){

        // id text titre, username et password
        TextView tvTitre = (TextView) findViewById(R.id.tvTitre);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvpassword = (TextView) findViewById(R.id.tvPassword);

        tvTitre.setText(titre);
        tvUsername.setText(username);
        tvpassword.setText(password);

    }

    public void supprimerPwd(){
        try {
            // suppression du json
            objetListePwds.remove(positionPwd);

            //on recré le nouveau json avec le Pwd supprimer
            String txtListePwds = objetListePwds.toString();

            FileOutputStream fos = openFileOutput("listePwds.json", MODE_PRIVATE);
            fos.write(txtListePwds.getBytes("ISO-8859-1"));
        } catch (Exception e) {
            Log.e("suppressionPwd", e.getMessage());
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
