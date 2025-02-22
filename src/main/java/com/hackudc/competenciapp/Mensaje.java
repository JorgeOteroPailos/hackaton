package com.hackudc.competenciapp;

public class Mensaje{
    public final String texto;
    public final int remitente; //0 para yo, 1 para el server

    public Mensaje(String texto, int remitente){
        this.remitente=remitente;
        this.texto=texto;
    }
}
