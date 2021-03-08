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
import com.example.proyectoappgimnasio.InfoReservaActivity;
import com.example.proyectoappgimnasio.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import Modelos.HorarioClase;
import Modelos.Reserva;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ViewHolder> implements Serializable {

    private ArrayList<Reserva> reservas;
    private int layout;
    private Context context;

    private int idCliente;
    private String nombreCliente;


    public ReservasAdapter(ArrayList<Reserva> reservas, int layout, Context context, int idCliente, String nombreCliente) {
        this.reservas = reservas;
        this.layout = layout;
        this.context = context;

        this.idCliente=idCliente;
        this.nombreCliente=nombreCliente;
    }

    @NonNull
    @Override
    public ReservasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_reserva,viewGroup,false);  //este layout seria el item row
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //LLena cada uno de los elementos ViewHolder y les asigna a cada uno un ClickListener.

        Reserva reserva= reservas.get(i);

        int idReserva=reserva.getIdReserva();
        String nombreClase=reserva.getNombreClase();
        String hora=reserva.getHora();
        String dia=reserva.getDia();

        viewHolder.nombreClase_tv.setText(nombreClase);
        viewHolder.horaClase_tv.setText(dia+" "+hora);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(context, InfoReservaActivity.class);
                intent.putExtra("horaClase",hora);
                intent.putExtra("idReserva", idReserva);
                intent.putExtra("diaClase",dia);
                intent.putExtra("nombreClase",nombreClase);

                context.startActivity(intent);

                //listener.onClick(horario, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView nombreClase_tv;
        TextView horaClase_tv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreClase_tv=itemView.findViewById(R.id.nombreClaseReserva_tv);
            horaClase_tv=itemView.findViewById(R.id.diaHoraReserva_tv);


        }

    }

    public interface OnClickListener{
        public void onClick(HorarioClase horarioClase, int pos);
    }
}

