package es.upm.dit.adsw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import static android.content.Intent.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String ADMIN_EMAIL = "Admin <cifadsw@gmail.com>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner motivoSpinner = (Spinner) findViewById(R.id.tipo);
        final Spinner destinoSpinner = (Spinner) findViewById(R.id.destino);
        String[] valores = getResources().getStringArray(R.array.receptores);
        final ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, valores);
        destinoSpinner.setAdapter(adaptador);

        // Se incluye como demostración, no hace falta
        destinoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final EditText quejaEdit = (EditText) findViewById(R.id.motivo);
        final RadioGroup urgenciaRadioGrp = (RadioGroup) findViewById(R.id.radioGrpUrgencia);

        // Se incluye como demostración, no hace falta

        urgenciaRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Pulsado ");
                int id = group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioAlta: {
                        Log.d(TAG, "alta");
                        break;
                    }
                    case R.id.radioMedia: {
                        Log.d(TAG, "media");
                        break;
                    }
                    case R.id.radioBaja: {
                        Log.d(TAG, "baja");
                        break;
                    }
                    default: {
                        Log.e(TAG, "Inesperado " + id);
                    }
                }
            }
        });


        final CheckBox mandarCorreoCheckBox = (CheckBox) findViewById(R.id.correo);
        final EditText emailEditText = (EditText) findViewById(R.id.emailRespuesta);

        mandarCorreoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "checkBox seleccionado");
                if (mandarCorreoCheckBox.isChecked()) {
                    emailEditText.setVisibility(View.VISIBLE);
                } else {
                    emailEditText.setVisibility(View.GONE);

                }
            }
        });

        Button boton = (Button) findViewById(R.id.boton);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String queja = quejaEdit.getText().toString();

                String motivo = motivoSpinner.getSelectedItem().toString();
                String receptores = destinoSpinner.getSelectedItem().toString();

                int urgenciaInt = urgenciaRadioGrp.getCheckedRadioButtonId();
                String urgencia = ((RadioButton) findViewById(urgenciaInt)).getText().toString();

                boolean responder = mandarCorreoCheckBox.isChecked();

                String email = "";
                if (responder) {
                    email = emailEditText.getText().toString();
                }

                Log.d(TAG, "Receptores " + receptores + " motivo " + motivo + " texto " + queja);
                Intent intent = new Intent(ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(EXTRA_EMAIL, new String [] {ADMIN_EMAIL});
                intent.putExtra(EXTRA_SUBJECT, "Nueva " + motivo);
                String body = "<h1>" + motivo + "</h1>"; // Mejor usar StringBuffer
                body += "<p>Receptores: " + receptores + "</p>";
                body += "<p>Texto: " + queja + "</p>";
                body += "<p>Mandar respuesta: " + urgencia + "</p>";
                body += "<p>Responder por email " + responder + "</p>";
                if (responder) {
                    body += "<p>Correo " + email + "</p>";
                }
                intent.putExtra(EXTRA_TEXT, Html.fromHtml(body));

                startActivity(createChooser(intent, "Enviar correo electrónico"));

            }
        });
    }
}
