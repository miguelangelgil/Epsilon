package com.puerto7070.epsilonv2;

public class Usuario {
    private String nombre;
    private String clave;
    private String correo;
    private int minutos;
    private long horas;
    public Usuario(String nombre, String clave, String correo)
    {
        this.nombre = nombre;
        this.clave = clave;
        this.correo = correo;

    }

    public Usuario(String nombre, String clave, String correo, long horas, int minutos)
    {
        this.nombre = nombre;
        this.clave = clave;
        this.correo = correo;
        this.horas = horas;
        this.minutos = minutos;

    }
    public boolean Check_nombre(String nombre)
    {
        return this.nombre.equalsIgnoreCase(nombre);
    }
    public boolean Check_clave(String clave)
    {
        return this.clave.equals(clave);
    }
    public boolean Check_correo(String correo)
    {
        return this.correo.equals(correo);
    }

    public String Get_nombre()
    {
        return nombre;
    }
    public String Get_correo()
    {
        return correo;
    }
    public int Get_minutos()
    {
        return minutos;
    }
    public long Get_horas()
    {
        return horas;
    }
    public void Sumar_minutos(int minutos)
    {
        this.minutos += minutos;
    }
    public void Sumar_horas(int horas)
    {
        this.horas += horas;
    }
    public void Set_tiempo(long horas, int minutos)
    {
        this.horas = horas;
        this.minutos = minutos;
    }
    public void Reset_tiempo()
    {
        minutos=0;
        horas=0;
    }
}
