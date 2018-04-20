package com.example.pc.bandsnarts.BBDD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.pc.bandsnarts.Activities.RegistarRedSocial;
import com.example.pc.bandsnarts.Activities.VentanaInicialApp;
import com.example.pc.bandsnarts.Activities.VentanaSliderParteDos;

import com.example.pc.bandsnarts.FragmentsPerfil.FragmentVerMiPerfil;
import com.example.pc.bandsnarts.Objetos.Grupo;
import com.example.pc.bandsnarts.Objetos.Musico;
import com.example.pc.bandsnarts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BDBAA extends AppCompatActivity {
    DatabaseReference bd;

    public BDBAA() {
    }

    public void agregarMusico(final Context context, final EditText edtnombre, final String imagen, final String nombre, final String sexo, final String estilo, final String instrumento, final String descripcion) {
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
                    encontrado = true;
                }
                if (!encontrado) {
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

    public void agregarGrupo(final Context context, final EditText edtnombre, final String imagen, final String nombre, final String estilo, final String descripcion) {

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
                    encontrado = true;
                }
                if (!encontrado) {
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

    public void comprobarUID(final Context cont, String uid) {
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
                    ((Activity) cont).startActivity(new Intent(cont, RegistarRedSocial.class));
                } else {
                    ((Activity) cont).startActivity(new Intent(cont, VentanaInicialApp.class));
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
                            Musico musico =  data.getValue(Musico.class);
                            // Recuperamos y cargamos los datos del Musico
                            // nombre
                            ((TextView)vista.findViewById(R.id.txtNombUsuarioVVerMiPerfil)).setText(musico.getNombre());
                            // FotoPerfil
                            Glide.with(getApplicationContext()).load(FirebaseAuth.getInstance().getCurrentUser()
                                    .getPhotoUrl()).override(200,200).into(((ImageView)vista.findViewById(R.id.imgPerfilVPerfil)));

                            break;
                        case "grupo":
                            Grupo grupo = data.getValue(Grupo.class);
                            // Recuperamos y cargamos los datos del Musico
                            // nombre
                            ((TextView)vista.findViewById(R.id.txtNombUsuarioVVerMiPerfil)).setText(grupo.getNombre());
                            // FotoPerfil
                            Glide.with(context).load(FirebaseAuth.getInstance().getCurrentUser()
                                    .getPhotoUrl()).override(200,200).into(((ImageView)vista.findViewById(R.id.imgPerfilVPerfil)));
                            // Estilo
                            ((TextView)vista.findViewById(R.id.txtEstiloVVerMiPerfil)).setText(grupo.getEstilo());
                            // Provincia
                            ((TextView)vista.findViewById(R.id.txtProvinciaVVerMiPerfil)).setText(grupo.getProvincia());
                            // Localidad
                            ((TextView)vista.findViewById(R.id.txtLocalidadVVerMiPerfil)).setText(grupo.getLocalidad());
                            // Sexo....
                            ((LinearLayout)vista.findViewById(R.id.llSexoVVerMiPerfil)).setVisibility(View.GONE);
                            // eMail
                            ((TextView)vista.findViewById(R.id.txtEmailVVerMiPerfil)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                            // Descripcion
                            ((TextView)vista.findViewById(R.id.txtDescripcionVVerPerfil)).setText(grupo.getDescripcion());

                            // Ocultamos los Instrumentos por tratarse de un grupo
                            vista.findViewById(R.id.appBarLayout4).setVisibility(View.GONE);
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
