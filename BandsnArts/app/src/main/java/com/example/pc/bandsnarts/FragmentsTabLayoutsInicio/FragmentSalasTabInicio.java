package com.example.pc.bandsnarts.FragmentsTabLayoutsInicio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterMusico;
import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterSalas;
import com.example.pc.bandsnarts.Objetos.Musico;
import com.example.pc.bandsnarts.Objetos.Sala;
import com.example.pc.bandsnarts.R;

import java.util.ArrayList;

/**
 * CLASE PARA INFLAR EL FRAGMENT DE LA VENTANA DE INICIO EN EL TAB DE SALAS
 */
public class FragmentSalasTabInicio extends Fragment{
    RecyclerView recyclerViewSalas;
    View vista;
    ArrayList<Sala> listaSalas;
    public FragmentSalasTabInicio(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.tab_salas_fragment,container,false);
        recyclerViewSalas = vista.findViewById(R.id.recyclerSalas);


        listaSalas=new ArrayList<>();

        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        listaSalas.add(new Sala("SALA UNO","29983","C/LUNA 12","MADRID",""));
        RecyclerAdapterSalas adapterSalas = new RecyclerAdapterSalas(getContext(), listaSalas);
        recyclerViewSalas.setNestedScrollingEnabled(false);
        recyclerViewSalas.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSalas.setAdapter(adapterSalas);

        return vista;
    }
}
