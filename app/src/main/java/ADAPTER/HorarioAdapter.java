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
import com.example.proyectoappgimnasio.R;
import com.example.proyectoappgimnasio.SingUpActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import Modelos.HorarioClase;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> implements Serializable {

    private ArrayList<HorarioClase> horarios;
    private int layout;
    private Context context;
    private OnClickListener listener;
    private int idCliente;


    public HorarioAdapter(ArrayList<HorarioClase> horarios, int layout, Context context,
                          OnClickListener listener, int idCliente) {
        this.horarios = horarios;
        this.layout = layout;
        this.context = context;
        this.listener = listener;
        this.idCliente=idCliente;
    }

    @NonNull
    @Override
    public HorarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_clase,viewGroup,false);  //este layout seria el item row
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //LLena cada uno de los elementos ViewHolder y les asigna a cada uno un ClickListener.

        final HorarioClase horario= horarios.get(i);

        String url=horario.getRutaImgCompleta();
        String nombre=horario.getNombreClase();
        String hora=horario.getHora();
        viewHolder.nombreClase_tv.setText(nombre);
        viewHolder.horaClase_tv.setText(hora);

        if(!url.equals("")) {
            Picasso.get()
                    .load(url)
                    .error(R.drawable.clase_default)
                    .placeholder(R.drawable.clase_default)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.imagen);
        }else{

            Picasso.get()
                    .load("https://cdn4.iconfinder.com/data/icons/aerobic-class-at-gym-room-with-instructor/336/class-lesson-003-512.png")
                    .error(R.drawable.clase_default)
                    .placeholder(R.drawable.clase_default)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.imagen);
        }




        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("HORARIOADAPTER() horario seleccionado",horario.toString());
                Intent intent=new Intent(context, InfoClaseActivity.class);
                intent.putExtra("horario",horario);
                intent.putExtra("idCliente", idCliente);

                context.startActivity(intent);
                

                //listener.onClick(horario, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen;
        TextView nombreClase_tv;
        TextView horaClase_tv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.ItemCarta);
            nombreClase_tv=itemView.findViewById(R.id.nombreClase_tv);
            horaClase_tv=itemView.findViewById(R.id.horaClase_tv);


        }

    }

    public interface OnClickListener{
        public void onClick(HorarioClase horarioClase, int pos);
    }
}

