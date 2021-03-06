package com.example.pc.bandsnarts.FragmentsTabLayoutsInicio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterLocales;
import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterSalas;
import com.example.pc.bandsnarts.BBDD.BDBAA;
import com.example.pc.bandsnarts.Objetos.Local;
import com.example.pc.bandsnarts.Objetos.Sala;
import com.example.pc.bandsnarts.R;

import java.util.ArrayList;

/**
 * CLASE PARA INFLAR EL FRAGMENT DE LA VENTANA DE INICIO EN EL TAB DE LOCALES
 */
public class FragmentLocalesTabInicio extends Fragment {
    RecyclerView recyclerViewLocal;
    View vista;
    ArrayList<Local> listaLocal = new ArrayList<>();

    public FragmentLocalesTabInicio() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.tab_locales_fragment, container, false);

        recyclerViewLocal = vista.findViewById(R.id.recyclerLocal);
        if (listaLocal.isEmpty()) {
             BDBAA.cargarDatos(listaLocal, recyclerViewLocal, getActivity(),"locales");
        }


        return vista;
    }


}
