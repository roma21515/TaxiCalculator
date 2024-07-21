package ar.com.lrusso.taxicalculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class ThreadEditar extends AsyncTask<String, Void, Bitmap>
	{
	ProgressDialog myDialog;
    String paraguardar;
    Activity actividad;
    
    public ThreadEditar(String a, Activity b)
    	{
    	paraguardar = a;
    	actividad = b;
    	}
    
    @Override protected void onPreExecute()
    	{
    	super.onPreExecute();
    	myDialog = new ProgressDialog(actividad);
    	myDialog.setMessage(actividad.getResources().getString(R.string.textoActualizando));
        myDialog.setCancelable(false);
        myDialog.show();
    	}
    
    public void onPostExecute(Bitmap result)
    	{
  	   	GlobalVars.actualizar=true;
        myDialog.dismiss();
        actividad.finish();
    	}
    
	public Bitmap doInBackground(String... urls)
    	{
    	eliminarPago(GlobalVars.paraEditarPosition);
    	nuevoPago(paraguardar);
    	return  null;
    	}
	
	public void eliminarPago(int position)
		{
		final String aeliminar = GlobalVars.devolverPosicion(position);
		int borrar = -1;
		for(int i=0;i<GlobalVars.listado.size();i++)
			{
			if (GlobalVars.listado.get(i)==aeliminar)
				{
				borrar=i;
				i=GlobalVars.listado.size();
				}
			}
		if (borrar>-1)
			{
			GlobalVars.listado.remove(borrar);
			}
		guardarListaEnBase();
		}

	public void guardarListaEnBase()
		{
		escribirArchivo("base.cfg","");
		String aGuardar = "";
		for (int i=0;i<GlobalVars.listado.size();i++)
			{
			if (GlobalVars.listado.get(i).length()>3)
				{
				if (aGuardar=="")
					{
					aGuardar = GlobalVars.listado.get(i);
					}
					else
					{
					aGuardar = aGuardar + "\r\n" + GlobalVars.listado.get(i);
					}
				}
			}
		nuevoPago(aGuardar);
		}

	public String leerArchivo(String archivo) {
			FileInputStream fis = null;
			DataInputStream dis = null;
			StringBuilder resultado = new StringBuilder();
			try {
				fis = actividad.openFileInput(archivo);
				dis = new DataInputStream(fis);
				while (dis.available() > 0) {
					resultado.append(dis.readUTF());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (dis != null) dis.close();
					if (fis != null) fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return resultado.toString();
		}

	public void escribirArchivo(String archivo, String texto) {
			FileOutputStream fos = null;
			DataOutputStream dos = null;
			try {
				fos = actividad.openFileOutput(archivo, Context.MODE_PRIVATE);
				dos = new DataOutputStream(fos);
				final int CHUNK_SIZE = 1024; // Размер фрагмента (например, 16 КБ)
				int length = texto.length();
				for (int i = 0; i < length; i += CHUNK_SIZE) {
					int end = Math.min(length, i + CHUNK_SIZE);
					dos.writeUTF(texto.substring(i, end));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (dos != null) dos.close();
					if (fos != null) fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	public void nuevoPago(String valor)
		{
		String original = leerArchivo("base.cfg");
		if (original==null)
			{
			escribirArchivo("base.cfg",valor);
			}
			else
			{
			escribirArchivo("base.cfg", original + "\r\n" + valor);
			}
		}
	}
