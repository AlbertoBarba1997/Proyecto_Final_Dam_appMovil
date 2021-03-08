package ADAPTER;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoappgimnasio.EjerciciosActivity;
import com.example.proyectoappgimnasio.InfoReservaActivity;
import com.example.proyectoappgimnasio.R;

import java.io.Serializable;
import java.util.ArrayList;

import Modelos.HorarioClase;
import Modelos.Tabla;

public class TablasAdapter extends RecyclerView.Adapter<TablasAdapter.ViewHolder> implements Serializable {

    private ArrayList<Tabla> tablas;
    private int layout;
    private Context context;


    public TablasAdapter(ArrayList<Tabla> tablas, int layout, Context context) {
        this.tablas = tablas;
        this.layout = layout;
        this.context = context;


    }

    @NonNull
    @Override
    public TablasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);  //este layout seria el item row
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //LLena cada uno de los elementos ViewHolder y les asigna a cada uno un ClickListener.

        Tabla tabla= tablas.get(i);

        int idTabla=tabla.getIdTabla();
        String nombreTabla=tabla.getNombre();
        int nDias=tabla.getnDias();

        viewHolder.nombreTabla_tv.setText(nombreTabla);
        viewHolder.nDiasTabla_tv.setText("Numero de dias: "+nDias);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, EjerciciosActivity.class);

                intent.putExtra("idTabla", idTabla);
                intent.putExtra("nDias", tabla.getnDias());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tablas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView nombreTabla_tv;
        TextView nDiasTabla_tv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTabla_tv =itemView.findViewById(R.id.nombreTabla_tv);
            nDiasTabla_tv =itemView.findViewById(R.id.nDiasTabla_tv);


        }

    }

    public interface OnClickListener{
        public void onClick(HorarioClase horarioClase, int pos);
    }
}

