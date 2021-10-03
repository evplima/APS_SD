package com.example.apssistemasdistribuidos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity {

    String operacao;
    String parametro;
    ArrayList<SPTrans> spTrans = new ArrayList<>();
    ArrayList<String> resultados = new ArrayList<>();
    ListView lista;
    Button buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        this.setTitle("APS SD - Bem-vindo!");
        buscar = (Button) findViewById(R.id.btBuscar);
        Spinner spinner = findViewById(R.id.spinner);
        EditText parametros = (EditText) findViewById(R.id.txtParam);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.operacoes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinner.getSelectedItem().equals("Buscar localizações")){
                    parametros.setEnabled(false);
                    parametros.setText("Aperte a busca");
                }else{
                    parametros.setEnabled(true);
                    parametros.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

         lista = (ListView) findViewById(R.id.lista);


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temRede()){
                    parametro = parametros.getText().toString();
                    if(spinner.getSelectedItem().equals("Buscar paradas")){
                        operacao = "buscarParadas";
                    }

                    if(spinner.getSelectedItem().equals("Buscar linhas")){
                        operacao = "buscarLinhas";
                    }
                    if(spinner.getSelectedItem().equals("Buscar localizações")){
                        operacao = "retornaPosicao";
                        parametro = "";
                    }


                    buscar.setEnabled(false);


                    new Buscar().execute();




                }else{
                    Toast.makeText(Principal.this, "Sem conexão com a internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    private boolean temRede() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void volleyGet(String operacao, String parametro) {

        if(operacao.equals("retornaPosicao") == false){
        String url = "http://ec2-52-67-68-31.sa-east-1.compute.amazonaws.com:8080/SPTrans/" + operacao + "/" + parametro;
        List<String> jsonResponses = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequestRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {


                    //JSONObject jsonArray = response.getJSONObject(1);
                    if (operacao.equals("buscarParadas")) {
                        spTrans.clear();
                        for (int i = 0; i < response.length(); i++) {
                            SPTrans spTransResult = new SPTrans(response.getJSONObject(i).getInt("cp"), response.getJSONObject(i).getString("np"), response.getJSONObject(i).getString("ed"),
                                    response.getJSONObject(i).getDouble("py"), response.getJSONObject(i).getDouble("px"));
                            spTrans.add(spTransResult);
                        }
                    }

                    if (operacao.equals("buscarLinhas")) {
                        spTrans.clear();
                        for (int i = 0; i < response.length(); i++) {
                            SPTrans spTransResult = new SPTrans(response.getJSONObject(i).getInt("cl"), response.getJSONObject(i).getBoolean("lc"), response.getJSONObject(i).getString("lt"),
                                    response.getJSONObject(i).getInt("sl"), response.getJSONObject(i).getInt("tl"), response.getJSONObject(i).getString("tp"), response.getJSONObject(i).getString("ts"));
                            spTrans.add(spTransResult);
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonArrayRequestRequest);

            if(operacao.equals("buscarParadas")){
                resultados.clear();
                for(int i = 0; i < spTrans.size(); i++){
                    resultados.add("Identificador parada: " + spTrans.get(i).getIdentificadorParada() +
                            "\nNome parada: " + spTrans.get(i).getNomeParada() +
                            "\nEndereço parada: " + spTrans.get(i).getEnderecoParada() +
                            "\nLatitude Parada: " + spTrans.get(i).getLatitudeParada() +
                            "\nLongitude Parada: " + spTrans.get(i).getLongitudeParada()
                    );
                }
            }

            if(operacao.equals("buscarLinhas")){
                resultados.clear();
                for(int i = 0; i < spTrans.size(); i++){
                    resultados.add("Identificador linha: " + spTrans.get(i).getIdentificadorLinha() +
                            "\nModo circular: " + spTrans.get(i).isModoCircular() +
                            "\nLetreiro 1: " + spTrans.get(i).getLetreiro1() +
                            "\nLetreiro 2: " + spTrans.get(i).getLetreiro2() +
                            "\nSentido linha: " + spTrans.get(i).getSentidoLinha() +
                            "\nTerminal principal: " + spTrans.get(i).getTerminalPrincipal() +
                            "\nTerminal secundário: " + spTrans.get(i).getTerminalSecundario()
                    );
                }
            }

    }else{
            String url = "http://ec2-52-67-68-31.sa-east-1.compute.amazonaws.com:8080/SPTrans/retornaPosicao";
            List<String> jsonResponses = new ArrayList<>();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {


                       // JSONObject jsonArray = response.getJSONObject("");

                            spTrans.clear();
                            ArrayList<SPTrans.linhasLocalizadas> arrayListLinhas = new ArrayList<>();
                            ArrayList<SPTrans.linhasLocalizadas.relacaoVeiculos> arrayListRelacao = new ArrayList<>();

                     //   System.out.println(response.getJSONArray("l").toString());

                        for(int i = 0; i < response.getJSONArray("l").length(); i++){
                            for(int j = 0; j < response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").length(); j++){

                                SPTrans.linhasLocalizadas.relacaoVeiculos relacaoVeiculos = new SPTrans.linhasLocalizadas.relacaoVeiculos(
                                        response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").getJSONObject(j).getInt("p"),
                                        response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").getJSONObject(j).getBoolean("a"),
                                        response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").getJSONObject(j).getString("ta"),
                                        response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").getJSONObject(j).getDouble("py"),
                                        response.getJSONArray("l").getJSONObject(i).getJSONArray("vs").getJSONObject(j).getDouble("px")
                                );

                                arrayListRelacao.add(relacaoVeiculos);
                            }

                            SPTrans.linhasLocalizadas linhasLocalizadas = new SPTrans.linhasLocalizadas(response.getJSONArray("l").getJSONObject(i).getString("c"),
                                    response.getJSONArray("l").getJSONObject(i).getInt("cl"),
                                    response.getJSONArray("l").getJSONObject(i).getInt("sl"),
                                    response.getJSONArray("l").getJSONObject(i).getString("lt0"),
                                    response.getJSONArray("l").getJSONObject(i).getString("lt1"),
                                    response.getJSONArray("l").getJSONObject(i).getInt("qv"), arrayListRelacao);
                            arrayListLinhas.add(linhasLocalizadas);
                        }




                           SPTrans spTransResult = new SPTrans(response.getString("hr"), arrayListLinhas);
                           spTrans.add(spTransResult);



                        resultados.clear();
                        String stringao;
                        for(int i = 0; i < spTrans.get(0).linhasLocalizadas.size(); i++){
                            stringao = "Horário referência: " +  spTrans.get(0).getHorario() +
                                    "\nLetreiro Completo: " + spTrans.get(0).linhasLocalizadas.get(i).getLetreiroCompleto() +
                                    "\nIdentificador Linha: " + spTrans.get(0).linhasLocalizadas.get(i).getIdentificadorLinha() +
                                    "\nSentido Linha: " + spTrans.get(0).linhasLocalizadas.get(i).getSentidoLinha() +
                                    "\nLetreiro Destino: " + spTrans.get(0).linhasLocalizadas.get(i).getLetreiroDestino() +
                                    "\nLetreiro Origem: " + spTrans.get(0).linhasLocalizadas.get(i).getLetreiroOrigem() +
                                    "\nQuantidade veículos: " + spTrans.get(0).linhasLocalizadas.get(i).getQuantidadeVeiculos();//+
                                 //   "\n\nVeículos localizados:";

                        /*    for(int j = 0; j < spTrans.get(0).linhasLocalizadas.get(i).relacaoVeiculos.size(); j++){
                                       stringao += "\n\nVeículo " + (j + 1) + ":" +
                                        "\nPrefixo veículo: " + spTrans.get(0).linhasLocalizadas.get(i).getRelacaoVeiculos().get(j).getPrefixoVeiculo() +
                                        "\nAcessível deficientes: " + spTrans.get(0).linhasLocalizadas.get(i).getRelacaoVeiculos().get(j).isAcessivelDeficientes() +
                                        "\nHorário UTC: " + spTrans.get(0).linhasLocalizadas.get(i).getRelacaoVeiculos().get(j).getHorarioUTC() +
                                        "\nLatitude veículo: " + spTrans.get(0).linhasLocalizadas.get(i).getRelacaoVeiculos().get(j).getLatitudeVeiculo() +
                                        "\nLongitude veículo: " + spTrans.get(0).linhasLocalizadas.get(i).getRelacaoVeiculos().get(j).getLongitudeVeiculo()
                                ;

                            }*/
                            resultados.add(stringao);
                            stringao = "";

                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(jsonObjectRequest);
        }


    }


    class Buscar extends AsyncTask<Void, Void, Void> {



        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Principal.this);
            SpannableString ss2=  new SpannableString("Buscando");
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
            pDialog.setMessage(ss2);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {


            volleyGet(operacao, parametro);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Principal.this, android.R.layout.simple_list_item_1, resultados);
            lista.setAdapter(arrayAdapter);
            buscar.setEnabled(true);
        }
    }

}