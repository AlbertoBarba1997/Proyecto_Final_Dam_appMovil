package ADAPTER;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoappgimnasio.InfoClaseActivity;
import com.example.proyectoappgimnasio.InfoEjercicioActivity;
import com.example.proyectoappgimnasio.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import Modelos.Ejercicio;
import Modelos.HorarioClase;

public class EjerciciosAdapter extends RecyclerView.Adapter<EjerciciosAdapter.ViewHolder> implements Serializable {

    private ArrayList<Ejercicio> ejercicios;
    private int layout;
    private Context context;



    public EjerciciosAdapter(ArrayList<Ejercicio> ejercicios, int layout, Context context) {
        this.ejercicios = ejercicios;
        this.layout = layout;
        this.context = context;


    }

    @NonNull
    @Override
    public EjerciciosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ejercicio,viewGroup,false);  //este layout seria el item row
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //LLena cada uno de los elementos ViewHolder y les asigna a cada uno un ClickListener.

        Ejercicio ejercicio= ejercicios.get(i);
        //Log.e("OnBindViewHolder ejercicio cargado:", ejercicio.toString());
        int idEjercicio=ejercicio.getIdEjercicio();
        int dia=ejercicio.getDia();
        String nombre=ejercicio.getNombre();
        String descripcion=ejercicio.getDescripcion();
        String tipo= ejercicio.getTipo();
        String rutaImg= ejercicio.getRutaImgCompleta();
        int series=ejercicio.getSeries();
        int repeticiones= ejercicio.getRepeticiones();
        String tiempo=ejercicio.getTiempo();

        viewHolder.nombreEjercicio_tv.setText(nombre);
        viewHolder.seriesEjercicio_tv.setText("Series: "+series);
        viewHolder.repeticionesEjercicio_tv.setText("Repeticiones: "+ repeticiones);
        viewHolder.tiempoEjercicio_tv.setText("Tiempo: "+tiempo);

        if(!rutaImg.equals("")) {
            Picasso.get()
                    .load(rutaImg)
                    .error(R.drawable.icons8_bench_press_125px)
                    .placeholder(R.drawable.icons8_bench_press_125px)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.imagen);
        }else{

            Picasso.get()
                    .load("https://i.pinimg.com/originals/f0/ee/cf/f0eecff8f557c63bca90b7b738c3df73.png")
                    .error(R.drawable.icons8_bench_press_125px)
                    .placeholder(R.drawable.icons8_bench_press_125px)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.imagen);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, InfoEjercicioActivity.class);
                intent.putExtra("ejercicio",ejercicio);

                context.startActivity(intent);


                

                //listener.onClick(horario, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ejercicios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen;
        TextView nombreEjercicio_tv;
        TextView seriesEjercicio_tv;
        TextView repeticionesEjercicio_tv;
        TextView tiempoEjercicio_tv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.imagenEjercicios);
            nombreEjercicio_tv=itemView.findViewById(R.id.nombreEjercicio_tv);
            seriesEjercicio_tv=itemView.findViewById(R.id.seriesEjercicio_tv);
            repeticionesEjercicio_tv=itemView.findViewById(R.id.repeticionesEjercicio_tv);
            tiempoEjercicio_tv=itemView.findViewById(R.id.tiempoEjercicios_tv);

        }

    }

    public interface OnClickListener{
        public void onClick(HorarioClase horarioClase, int pos);
    }
}

