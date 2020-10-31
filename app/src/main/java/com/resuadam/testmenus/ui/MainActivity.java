package com.resuadam.testmenus.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.resuadam.testmenus.R;
import com.resuadam.testmenus.core.Calculadora;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        this.items = new ArrayList<String>();

        FloatingActionButton btAdd = (FloatingActionButton) this.findViewById( R.id.btAdd );
        ListView lvItems = (ListView) this.findViewById( R.id.lvItems );

        lvItems.setLongClickable( true );
        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter( this.itemsAdapter );
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l){
                final EditText editText = new EditText(MainActivity.this);
                String texto = MainActivity.this.items.toString();

                editText.setText(MainActivity.this.items.get(pos).toString());
                if (pos>=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Modificar");
                    builder.setView(editText);
                    builder.setNegativeButton("Cancelar",null);
                    builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.items.set(pos,editText.getText().toString());
                            MainActivity.this.itemsAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            }});
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                if ( pos >= 0 ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Borrar Elemento");
                    builder.setMessage("Seguro que quieres borrar el elemento: '" + MainActivity.this.items.get(pos).toString() +"'?");
                    builder.setPositiveButton("Borrar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.items.remove( pos );
                            MainActivity.this.itemsAdapter.notifyDataSetChanged();
                            MainActivity.this.updateStatus();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.create().show();

                }
                return true;
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
            super.onCreateContextMenu(contextMenu, v, menuInfo);
            this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );
            contextMenu.setHeaderTitle( R.string.app_name );
            AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)menuInfo;
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.ver:

                toret = true;
                break;
            case R.id.mod:

                toret = true;
            case R.id.borrar:

                toret = true;
                break;
        }

        return toret;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate(R.menu.actions_menu, menu );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.addEntrenamiento:
                toret = true;
                onAdd();
                break;
            case R.id.stats:
                toret = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ERROR");
                builder.setMessage("En este momento las estadisticas no estan disponibles, intentelo mañana");
                break;
        }

        return toret;
    }
    private static String getTimeStamp() {
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy HH:mm");
        Calendar cal = Calendar.getInstance();
        String fecha = formatoFecha.format(cal.getTime());
        return fecha;
    }

    private void onAdd() {

        // Texto "Distancia:"
        TextView dist = new TextView(this);
        dist.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dist.setText("Distancia:");
        dist.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        // cuadro para distancia "metros"
        final EditText metros = new EditText(this);
        metros.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                300));
        metros.setInputType(InputType.TYPE_CLASS_NUMBER);
        metros.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        metros.setHint("metros ");

        // Texto "Tiempo:"
        TextView time = new TextView(this);
        time.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        time.setText("Tiempo:");
        time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        // cuadro para tiempo "horas"
        final EditText horas = new EditText(this);
        horas.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                9));
        horas.setInputType(InputType.TYPE_CLASS_NUMBER);
        horas.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        horas.setHint("horas");
        // cuadro para tiempo "minutos"
        final EditText minutos = new EditText(this);
        minutos.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                9));
        minutos.setInputType(InputType.TYPE_CLASS_NUMBER);
        minutos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        minutos.setHint("minutos");
        // cuadro para tiempo "segundos"
        final EditText segundos = new EditText(this);
        segundos.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                9));
        segundos.setInputType(InputType.TYPE_CLASS_NUMBER);
        segundos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        segundos.setHint("segundos");

        LinearLayout tiempo = new LinearLayout(this);
        tiempo.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        tiempo.setBaselineAligned(true);
        tiempo.setOrientation(LinearLayout.HORIZONTAL);

        tiempo.addView(horas);
        tiempo.addView(minutos);
        tiempo.addView(segundos);

        LinearLayout elpapa = new LinearLayout(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(30, 20, 30, 5);
        elpapa.setOrientation(LinearLayout.VERTICAL);
        elpapa.addView(dist);
        elpapa.addView(metros);
        elpapa.addView(time);
        elpapa.addView(tiempo);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ENTRENAMIENTO");
        builder.setView(elpapa);

        if (metros.getText()==null){
            metros.setText("0");
        }
        if (horas.getText()==null){
            horas.setText("0");
        }
        if (minutos.getText()==null){
            minutos.setText("0");
        }
        if (segundos.getText()==null){
            segundos.setText("0");
        }




        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txm = metros.getText().toString();
                String txh = horas.getText().toString();
                String txmin = minutos.getText().toString();
                String txseg = segundos.getText().toString();

                double m = Double.valueOf(txm);
                double h = Double.valueOf(txh);
                double min = Double.valueOf(txmin);
                double seg = Double.valueOf(txseg);
                double tiempo=h+(min/60)+(seg/3600);
                double kmh = (m/1000)/tiempo;
                BigDecimal bd = new BigDecimal(kmh);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                String kmhbd=String.valueOf(bd);
                //double numkm=Double.valueOf(kmhbd);
                //listakmm.add(numkm);
                if(m==0){
                    txm="";
                }else{
                    txm=txm+"m ,";
                }
                if(h==0){
                    txh="";
                } else{
                    txh=txh+"h ";
                }
                if(min==0){
                    txmin="";
                }else{
                    txmin=txmin+"min ";
                }
                if(seg==0){
                    txseg="";
                }else{
                    txseg=txseg+"s ";
                }


                MainActivity.this.itemsAdapter.add(MainActivity.getTimeStamp()+" | "+txm+txh+txmin+txseg+", "+kmhbd+"km/h");
                MainActivity.this.updateStatus();
            }



        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();

    }

    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById( R.id.vm );
        //double b=0;
        //for(Double a:listakmm){
        //    b=b+a;
        //}
        //double j=this.listakmm.size();
        //txtNum.setText(Double.toString(b/j));
        txtNum.setText( Integer.toString( this.itemsAdapter.getCount() ) );
    }
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    //private ArrayList<Double> listakmm;


}