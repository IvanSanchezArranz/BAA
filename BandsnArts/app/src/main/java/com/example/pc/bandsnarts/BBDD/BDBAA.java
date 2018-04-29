package com.example.pc.bandsnarts.BBDD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.pc.bandsnarts.Activities.RegistarRedSocial;
import com.example.pc.bandsnarts.Activities.VentanaInicialApp;
import com.example.pc.bandsnarts.Activities.VentanaSliderParteDos;

import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterGrupo;
import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterLocales;
import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterMusico;
import com.example.pc.bandsnarts.Adaptadores.RecyclerAdapterSalas;
import com.example.pc.bandsnarts.Objetos.Grupo;
import com.example.pc.bandsnarts.Objetos.Local;
import com.example.pc.bandsnarts.Objetos.Musico;
import com.example.pc.bandsnarts.Objetos.Sala;
import com.example.pc.bandsnarts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BDBAA extends AppCompatActivity {
    DatabaseReference bd;

    public BDBAA() {
    }

    public void agregarMusico(final Context context, final View view, final EditText edtnombre, final String imagen, final String nombre, final String sexo, final String estilo, final ArrayList<String> instrumento, final String descripcion) {
        // Nos posicionamos
        bd = FirebaseDatabase.getInstance().getReference("musico");

        Query q = bd.orderByChild("nombre").equalTo(nombre.toString());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("insert", "No pudo insertar");
                    Toast.makeText(context, "Ya existe un usuario con con el nombre " + nombre, Toast.LENGTH_SHORT).show();
                    edtnombre.setError("EL nombre Ya existe pruebe con otro", context.getDrawable(android.R.drawable.stat_notify_error));
                    // limpiar campo !!!!!
                    view.setVisibility(View.VISIBLE);
                    encontrado = true;
                }
                if (!encontrado) {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("tipo", "musico").commit();
                    Log.d("insert", "Insertado con exito");
                    DatabaseReference bd = FirebaseDatabase.getInstance().getReference("musico");
                    Musico mus = new Musico(FirebaseAuth.getInstance().getCurrentUser().getUid(), imagen, nombre, sexo, estilo, instrumento, descripcion);
                    bd.child(bd.push().getKey()).setValue(mus);
                    FirebaseDatabase.getInstance().getReference("uids").child(bd.push().getKey()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Toast.makeText(context, "Añadido con exito", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, VentanaSliderParteDos.class));
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "No se pudo agregar con exito", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void agregarGrupo(final Context context, final View view, final EditText edtnombre, final String imagen, final String nombre, final String estilo, final String descripcion) {

        bd = FirebaseDatabase.getInstance().getReference("grupo");
        Query q = bd.orderByChild("nombre").equalTo(nombre.toString());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("INSERt", "No insertado ");
                    Toast.makeText(context, "Ya existe un grupo con con el nombre " + nombre, Toast.LENGTH_SHORT).show();
                    edtnombre.setError("EL nombre Ya existe pruebe con otro", context.getDrawable(android.R.drawable.stat_notify_error));
                    view.setVisibility(View.VISIBLE);
                    encontrado = true;
                }
                if (!encontrado) {

                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("tipo", "grupo").commit();

                    Log.d("INSERt", "Insertado ");
                    DatabaseReference bd = FirebaseDatabase.getInstance().getReference("grupo");
                    Grupo gru = new Grupo(FirebaseAuth.getInstance().getCurrentUser().getUid(), imagen, nombre, estilo, descripcion);
                    bd.child(bd.push().getKey()).setValue(gru);
                    FirebaseDatabase.getInstance().getReference("uids").child(bd.push().getKey()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Toast.makeText(context, "Añadido con exito", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, VentanaSliderParteDos.class));
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR BD", "\n\nonCancelled: " + databaseError.getMessage() + "\n\n");
                Toast.makeText(context, "No se pudo agregar con exito", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void borrarPerfil(final String uid) {
        bd = FirebaseDatabase.getInstance().getReference("uids");
        Query q = bd.orderByChild("uid").equalTo(uid);
        Log.d("UID", "onDataChange: " + uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    bd.child(data.getKey()).removeValue();
                    encontrado = true;
                }
                if (!encontrado) {
                    Log.d("Encontrado", "onDataChange: " + encontrado);

                } else {
                    Log.d("Encontrado", "onDataChange: " + encontrado);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void eliminarNodo(String type, String uid) {
        bd = FirebaseDatabase.getInstance().getReference(type);
        Query q = bd.orderByChild("uid").equalTo(uid);
        Log.d("UID", "onDataChange: " + uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    bd.child(data.getKey()).removeValue();
                    encontrado = true;
                }
                if (!encontrado) {
                    Log.d("Encontrado2", "onDataChange: " + encontrado);
                } else {
                    Log.d("Encontrado2", "onDataChange: " + encontrado);
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void comprobarUID(final Context cont, final String uid) {
        bd = FirebaseDatabase.getInstance().getReference("uids");
        Query q = bd.orderByChild("uid").equalTo(uid);
        Log.d("UID", "onDataChange: " + uid);

        /* Query q = FirebaseDatabase.getInstance().getReference("uid").equalTo(uid);*/
        q.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    encontrado = true;
                }
                if (!encontrado) {
                    Log.d("Encontrado", "onDataChange: " + encontrado);
                    ((Activity) cont).startActivityForResult(new Intent(cont, RegistarRedSocial.class), 111);
                } else {
                    comprobarTipo(cont, uid);
                    ((Activity) cont).startActivityForResult(new Intent(cont, VentanaInicialApp.class), 222);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void comprobarTipo(final Context cont, String uid) {
        bd = FirebaseDatabase.getInstance().getReference("musico");
        Query q = bd.orderByChild("uid").equalTo(uid);

        q.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean encontrado = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    encontrado = true;
                    Log.d("", "onDataChange: PEPE");
                }
                if (encontrado) {
                    // Es musico, lo guardamos en preferencias
                    PreferenceManager.getDefaultSharedPreferences(cont).edit().putString("tipo", "musico").commit();
                } else {
                    // Es grupo, lo guardamos en preferencias
                    PreferenceManager.getDefaultSharedPreferences(cont).edit().putString("tipo", "grupo").commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cargarDrawerPerfil(final Context context, final String tipo, final ImageView fotoPerfil, final TextView nombre) {
        bd = FirebaseDatabase.getInstance().getReference(tipo);
        Query q = bd.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (tipo) {
                        case "musico":
                            nombre.setText(data.getValue(Musico.class).getNombre());
                            accesoFotoPerfil("musico", '1', fotoPerfil, context);

                            break;
                        case "grupo":
                            nombre.setText(data.getValue(Grupo.class).getNombre());
                            accesoFotoPerfil("grupo", '1', fotoPerfil, context);
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void cargarDatosPerfil(final View vista, final String tipo, final Context context) {
        bd = FirebaseDatabase.getInstance().getReference(tipo);
        Query q = bd.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (tipo) {
                        case "musico":
                            Musico musico = data.getValue(Musico.class);
                            // Recuperamos y cargamos los datos del Musico
                            // nombre
                            ((TextView) vista.findViewById(R.id.txtNombUsuarioVVerMiPerfil)).setText(musico.getNombre());
                            // FotoPerfil
                            accesoFotoPerfil("musico", '1', ((ImageView) vista.findViewById(R.id.imgPerfilVPerfil)), context);
                            // Estilo
                            ((TextView) vista.findViewById(R.id.txtEstiloVVerMiPerfil)).setText(musico.getEstilo());
                            // Provincia
                            ((TextView) vista.findViewById(R.id.txtProvinciaVVerMiPerfil)).setText(musico.getProvincia());
                            // Localidad
                            ((TextView) vista.findViewById(R.id.txtLocalidadVVerMiPerfil)).setText(musico.getLocalidad());
                            // Sexo....
                            ((TextView) vista.findViewById(R.id.txtSexoVVerMiPerfil)).setText(musico.getSexo());
                            // Descripcion
                            ((TextView) vista.findViewById(R.id.txtDescripcionVVerMiPerfil)).setText(musico.getDescripcion());
                            //Instrumentos
                            ((TextView) vista.findViewById(R.id.txtInstrumentoVVerMiPerfil1)).setText(musico.getInstrumento().get(0));
                            //Buscando

                            if (musico.getBuscando().equalsIgnoreCase("si")) {
                                ((ImageView) vista.findViewById(R.id.imgBuscandoVerMiPerfil)).setImageDrawable(getDrawable(R.drawable.yes));
                            } else {
                                ((ImageView) vista.findViewById(R.id.imgBuscandoVerMiPerfil)).setImageDrawable(getDrawable(R.drawable.no));
                            }

                            try {
                                ((TextView) vista.findViewById(R.id.txtInstrumentoVVerMiPerfil2)).setText(musico.getInstrumento().get(1));
                                ((TextView) vista.findViewById(R.id.txtInstrumentoVVerMiPerfil3)).setText(musico.getInstrumento().get(2));
                                ((TextView) vista.findViewById(R.id.txtInstrumentoVVerMiPerfil4)).setText(musico.getInstrumento().get(3));
                            } catch (IndexOutOfBoundsException e) {
                                // En caso de que solo tenga el instrumento principal
                                System.out.println("Si me salgo de rango");
                            }
                            break;
                        case "grupo":

                            Grupo grupo = data.getValue(Grupo.class);
                            // Recuperamos y cargamos los datos del Musico
                            // nombre
                            ((TextView) vista.findViewById(R.id.txtNombUsuarioVVerMiPerfil)).setText(grupo.getNombre());
                            // FotoPerfil
                            accesoFotoPerfil("grupo", '1', ((ImageView) vista.findViewById(R.id.imgPerfilVPerfil)), context);
                            // Estilo
                            ((TextView) vista.findViewById(R.id.txtEstiloVVerMiPerfil)).setText(grupo.getEstilo());
                            // Provincia
                            ((TextView) vista.findViewById(R.id.txtProvinciaVVerMiPerfil)).setText(grupo.getProvincia());
                            // Localidad
                            ((TextView) vista.findViewById(R.id.txtLocalidadVVerMiPerfil)).setText(grupo.getLocalidad());
                            // Sexo....
                            ((LinearLayout) vista.findViewById(R.id.llSexoVVerMiPerfil)).setVisibility(View.GONE);

                            // Descripcion
                            ((TextView) vista.findViewById(R.id.txtDescripcionVVerMiPerfil)).setText(grupo.getDescripcion());
                            //Buscando

                            if (grupo.getBuscando().equalsIgnoreCase("si")) {
                                ((ImageView) vista.findViewById(R.id.imgBuscandoVerMiPerfil)).setImageDrawable(vista.getResources().getDrawable(R.drawable.yes));
                            } else {
                                ((ImageView) vista.findViewById(R.id.imgBuscandoVerMiPerfil)).setImageDrawable(vista.getResources().getDrawable(R.drawable.no));
                            }

                            // Ocultamos los Instrumentos por tratarse de un grupo
                            vista.findViewById(R.id.appBarLayoutInstrumentos).setVisibility(View.GONE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void cargarDatosPerfilEditar(final View vista, final String tipo, final Context context) {
        bd = FirebaseDatabase.getInstance().getReference(tipo);
        Query q = bd.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int posicion;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (tipo) {
                        case "musico":
                            Musico musico = data.getValue(Musico.class);
                            // Recuperamos y cargamos los datos del Musico
                            // FotoPerfil
                            accesoFotoPerfil("musico", '1', ((ImageView) vista.findViewById(R.id.imgPerfilVPerfil)), context);
                            // Estilo
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.estiloMusical), musico.getEstilo());
                            ((Spinner) vista.findViewById(R.id.spEstiloVVerMiPerfil)).setSelection(posicion);
                            Toast.makeText(context, "" + posicion, Toast.LENGTH_SHORT).show();
                            // Sexo....
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.sexo), musico.getSexo());
                            ((Spinner) vista.findViewById(R.id.spSexoVVerMiPerfil)).setSelection(posicion);
                            // Provincia
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.provincias), musico.getProvincia());
                            ((Spinner) vista.findViewById(R.id.spProvinVVerMiPerfil)).setSelection(posicion);
                            // Localidad
                            CharSequence[] localidades1;
                            TypedArray arrayLocalidades1 = vista.getResources().obtainTypedArray(
                                    R.array.array_provincia_a_localidades);
                            localidades1 = arrayLocalidades1.getTextArray(posicion);
                            arrayLocalidades1.recycle();
                            // Create an ArrayAdapter using the string array and a default
                            // spinner layout
                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                                    context, R.layout.spinner_item,
                                    localidades1);
                            // Specify the layout to use when the list of choices appears
                            adapter.setDropDownViewResource(R.layout.spinner_item);
                            // Apply the adapter to the spinner
                            ((Spinner) vista.findViewById(R.id.spLocaliVVerMiPerfil)).setAdapter(adapter);
                            posicion = posicionSpinner(localidades1, musico.getLocalidad());
                            System.out.println(posicion);
                            ((Spinner) vista.findViewById(R.id.spLocaliVVerMiPerfil)).setSelection(posicion);
                            // Descripcion
                            ((TextView) vista.findViewById(R.id.txtDescripcionVVerMiPerfil)).setText(musico.getDescripcion());
                            //Buscando

                            if (musico.getBuscando().equalsIgnoreCase("si")) {
                                ((Switch) vista.findViewById(R.id.swBuscando)).setChecked(true);
                            } else {
                                ((Switch) vista.findViewById(R.id.swBuscando)).setChecked(false);
                            }

                            //Instrumentos
                            // Instumento Principal
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.instrumentos), musico.getInstrumento().get(0));
                            ((Spinner) vista.findViewById(R.id.spInstrumentoVVerMiPerfil1)).setSelection(posicion);

                            try {
                                posicion = posicionSpinner(vista.getResources().getStringArray(R.array.instrumentos), musico.getInstrumento().get(1));
                                ((Spinner) vista.findViewById(R.id.spInstrumentoVVerMiPerfil2)).setSelection(posicion);
                                posicion = posicionSpinner(vista.getResources().getStringArray(R.array.instrumentos), musico.getInstrumento().get(2));
                                ((Spinner) vista.findViewById(R.id.spInstrumentoVVerMiPerfil3)).setSelection(posicion);
                                posicion = posicionSpinner(vista.getResources().getStringArray(R.array.instrumentos), musico.getInstrumento().get(3));
                                ((Spinner) vista.findViewById(R.id.spInstrumentoVVerMiPerfil4)).setSelection(posicion);
                            } catch (IndexOutOfBoundsException e) {
                                // En caso de que solo tenga el instrumento principal
                            }

                            break;
                        case "grupo":
                            Log.d("PENE", "onDataChange: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                            Grupo grupo = data.getValue(Grupo.class);
                            // Recuperamos y cargamos los datos del Musico
                            // FotoPerfil
                            accesoFotoPerfil("grupo", '1', ((ImageView) vista.findViewById(R.id.imgPerfilVPerfil)), context);
                            // Estilo
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.estiloMusical), grupo.getEstilo());
                            ((Spinner) vista.findViewById(R.id.spEstiloVVerMiPerfil)).setSelection(posicion);
                            // Provincia
                            posicion = posicionSpinner(vista.getResources().getStringArray(R.array.provincias), grupo.getProvincia());
                            ((Spinner) vista.findViewById(R.id.spProvinVVerMiPerfil)).setSelection(posicion);
                            // Localidad
                            CharSequence[] localidades;
                            TypedArray arrayLocalidades = vista.getResources().obtainTypedArray(
                                    R.array.array_provincia_a_localidades);
                            localidades1 = arrayLocalidades.getTextArray(posicion);
                            arrayLocalidades.recycle();
                            // Create an ArrayAdapter using the string array and a default
                            // spinner layout
                            ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(
                                    context, R.layout.spinner_item,
                                    localidades1);
                            // Specify the layout to use when the list of choices appears
                            adapter1.setDropDownViewResource(R.layout.spinner_item);
                            // Apply the adapter to the spinner
                            ((Spinner) vista.findViewById(R.id.spLocaliVVerMiPerfil)).setAdapter(adapter1);
                            posicion = posicionSpinner(localidades1, grupo.getLocalidad());
                            System.out.println(posicion);
                            ((Spinner) vista.findViewById(R.id.spLocaliVVerMiPerfil)).setSelection(posicion);
                            // Sexo....
                            ((LinearLayout) vista.findViewById(R.id.llSexoVVerMiPerfil)).setVisibility(View.GONE);
                            // Descripcion
                            ((TextView) vista.findViewById(R.id.txtDescripcionVVerMiPerfil)).setText(grupo.getDescripcion());
                            //Buscando
                            if (grupo.getBuscando().equalsIgnoreCase("si")) {
                                ((Switch) vista.findViewById(R.id.swBuscando)).setChecked(true);
                            } else {
                                ((Switch) vista.findViewById(R.id.swBuscando)).setChecked(false);
                            }
                            // Ocultamos los Instrumentos por tratarse de un grupo
                            vista.findViewById(R.id.appBarLayoutInstrumentos).setVisibility(View.GONE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void modificarDatosUsuario(final String tipo, final Context context, final String imagen, final String sexo, final String estilo, final ArrayList<String> instrumento, final String descripcion, final String provincia, final String localidad, final String buscando) {
        // Nos posicionamos
        bd = FirebaseDatabase.getInstance().getReference(tipo);
        // Recuperamos al usuario a través de su UID
        Query q = bd.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    switch (tipo) {

                        case ("musico"):

                            Musico mus = ds.getValue(Musico.class);
                            mus.setImagen(imagen);
                            mus.setSexo(sexo);
                            mus.setEstilo(estilo);
                            mus.setInstrumento(instrumento);
                            mus.setDescripcion(descripcion);
                            mus.setProvincia(provincia);
                            mus.setLocalidad(localidad);
                            mus.setBuscando(buscando);

                            bd.child(ds.getKey()).setValue(mus);
                            Log.d("insert", "Insertado con exito");
                            Toast.makeText(context, "Actualizado músico con exito", Toast.LENGTH_SHORT).show();
                            break;
                        case ("grupo"):
                            Grupo gr = ds.getValue(Grupo.class);
                            gr.setImagen(imagen);
                            gr.setEstilo(estilo);
                            gr.setDescripcion(descripcion);
                            gr.setProvincia(provincia);
                            gr.setLocalidad(localidad);
                            gr.setBuscando(buscando);

                            bd.child(ds.getKey()).setValue(gr);
                            Log.d("insert", "Actualizado grupo con exito");
                            Toast.makeText(context, "Actualizado grupo con exito", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "No se pudo agregar con exito", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void cargarDatos(final ArrayList lista, final RecyclerView recyclerView, final Activity activity, final String tipo) {
        DatabaseReference bd = FirebaseDatabase.getInstance().getReference(tipo);
        Query q = bd.orderByChild("nombre");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (tipo) {
                        case "grupo":
                            Grupo grp = data.getValue(Grupo.class);
                            lista.add(grp);
                            break;
                        case "musico":
                            Musico mus = data.getValue(Musico.class);
                            lista.add(mus);
                            break;
                        case "locales":
                            Local loc = data.getValue(Local.class);
                            lista.add(loc);
                            break;
                        case "salas":
                            Sala sal = data.getValue(Sala.class);
                            lista.add(sal);
                            break;
                    }
                }
                switch (tipo) {
                    case "grupo":
                        RecyclerAdapterGrupo adapterG = new RecyclerAdapterGrupo(activity.getApplicationContext(), lista);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(adapterG);
                        break;
                    case "musico":
                        RecyclerAdapterMusico adapterM = new RecyclerAdapterMusico(activity.getApplicationContext(), lista);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(adapterM);
                        break;
                    case "locales":
                        RecyclerAdapterLocales adapterL = new RecyclerAdapterLocales(activity.getApplicationContext(), lista);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(adapterL);
                        break;
                    case "salas":
                        RecyclerAdapterSalas adapterS = new RecyclerAdapterSalas(activity.getApplicationContext(), lista);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(adapterS);
                        break;
                }
                recyclerView.setNestedScrollingEnabled(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public int posicionSpinner(String[] lista, String sp) {
        try {
            for (int i = 0; i < lista.length; i++) {
                if (sp.equals(lista[i])) {
                    return i;
                }
            }
        } catch (NullPointerException ex) {

        }
        return 0;
    }

    public int posicionSpinner(CharSequence[] lista, String sp) {
        try {
            for (int i = 0; i < lista.length; i++) {

                if (sp.equalsIgnoreCase(lista[i].toString())) {
                    System.out.println(lista[i]);
                    return i;
                }
            }
        } catch (NullPointerException ex) {

        }
        return 0;
    }

    ///////////////////////////////////////////////////////////////STORAGE/////////////////////////////////////////////////////////////////////////////////
    public void accesoFotoPerfil(final String tipo, char cantidad, final ImageView vista, final Context context) {

        DatabaseReference bd = FirebaseDatabase.getInstance().getReference(tipo);
        Query q = null;
        switch (cantidad) {
            case '1':
                q = bd.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case 'n':
                q = bd.orderByChild("nombre");
                break;
        }


        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String img;
                StorageReference ref = FirebaseStorage.getInstance().getReference("imagenes");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (tipo) {
                        case "musico":
                            img = data.getValue(Musico.class).getImagen();
                            ref.child(img).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Glide.with(context).load(task.getResult()).override(200, 200).into(vista);
                                }
                            });
                            break;
                        case "grupo":
                            img = data.getValue(Grupo.class).getImagen();
                            ref.child(img).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Glide.with(context).load(task.getResult()).override(200, 200).into(vista);
                                }
                            });
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
