package com.supinfo.suppwd;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.FileInputStream;


public class MainActivity extends Activity {

    private Boolean filePwdValide = false;
    private JSONArray objetListePwds;
    private ListView lvListePwds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // id du bouton ajouter
        Button btnAjouter = (Button) findViewById(R.id.btnAdd);

        // listener du bouton Ajouter
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AjouterPwd.class);
                startActivity(intent);
            }
        });

        lvListePwds = (ListView) findViewById(R.id.lvListePwds);

        // listener onclick pour les items de la liste
        lvListePwds.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                // récupère le texte de la celule sélectionné dans la liste
                String titrePwdSelect = (String) ((TextView) v).getText();

                String extraPwd = titrePwdSelect;

                String Extra_Pwd = null;

                Intent intent = new Intent(MainActivity.this, DescriptionPwd.class);
                intent.putExtra(Extra_Pwd, extraPwd);
                startActivity(intent);

            }


        });

        //deleteFile("listePwds.json");

        readFilePwd();

        afficheListePwds();

    }


    public void afficheListePwds(){

        String[] tbListePwds = null;

        if (filePwdValide == true) {
            // Recupère le tableau d'enseignes
            tbListePwds = listePwds();
        } else {
            tbListePwds =  new String[1];
            tbListePwds[0] = "Aucun password";
        }

        ArrayAdapter<String> arrayadp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, tbListePwds);
        lvListePwds.setAdapter(arrayadp);

    }


    public String[] listePwds(){
        String[] listePwds = null;

        try {

            Integer nbrPwds = objetListePwds.length();
            listePwds = new String[nbrPwds];

            for (int i = 0; i < nbrPwds; i++) {
                String titrePwd = objetListePwds.getJSONObject(i).getString("titre");
                listePwds[i] = titrePwd;
            }

        } catch (Exception e) {
            Log.e("ListePwds", e.getMessage());
        }

        return listePwds;
    }


    // lecture de la liste des passwords
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

            Integer nbrPwds = objetListePwds.length();

            if (nbrPwds != 0) {
                filePwdValide = true;
            }
            else {
                filePwdValide = false;
            }

        } catch (Exception e) {
            Log.e("LectureFileListePwds", e.getMessage());
            filePwdValide = false;
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
