package mapped.wolfox.com.alocadinganothersmsgps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.security.MessageDigest;

/**
 * Created by MIPC on 17/07/2015.
 */
public class SMSReceiver extends BroadcastReceiver{

    public static final String SMS_SEND_ASK = "¿Oe on tas?";
    public static final String SMS_SEND_RESPOND = "Aqui estoy: ";
    public static final String SMS_QUESTIONER_RESPONDER_NUMS = "+51969637038+51931022193+51969637038+51988207748";//numero pregun
    //public static final String SMS_QUESTIONER_NUM = "+51963510139";

    public static final String SMS_LAT = "lat: ";
    public static final String SMS_LON = "lon: ";
    //public static final String SMS_QUESTIONER_NUM = "+51963510139";

    //private MapsActivity mapsActivity;
    public static MapsActivity MAPS_ACTIVITY;
    /*
    public void setMapActivity(MapsActivity mapsActivity ) {
        this.mapsActivity = mapsActivity;
    }*/

    public static LatLng LATLON;

    /*
    public SMSReceiver(MapsActivity mapsActivity ){
        super();
        this.mapsActivity = mapsActivity;
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "llego un mensaje\n ¿oe on Tas?", Toast.LENGTH_SHORT).show();
        try{
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage sms = null;//guardara el mensage que llego
            if(bundle != null){//se evalua si llegó mensaje
                for(Object aPdu : pdus){
                    sms = SmsMessage.createFromPdu((byte[]) aPdu);
                }
                String sender = sms.getOriginatingAddress();
                //if(!sender.equals(SMS_QUESTIONER_NUMS) ){//salir si es otro numero
                if(SMS_QUESTIONER_RESPONDER_NUMS.indexOf(sender)== -1){//salir si no esta en la lista
                    return;
                }
                String message = sms.getMessageBody();
                //Averiguo que tipo de pregunta me hicieron
                if(message.equals(SMS_SEND_ASK)){//Quieren saber mi ubicacion
                    giveLocationSendSMS(sender, context);
                }else if(message.indexOf(SMS_SEND_RESPOND) != -1){//me mando su ubicacion
                    //mostrar ubicacion en mapa

                    showLocation(message, context);
                }else{//es un mnesaje cualquiera
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Send SMS
    //Metodo para enviar mensajes a otro dispositivo
    public void sendSMS(String phoneNumber, String message, Context context){
        Toast.makeText(context, "Enviando mensaje...", Toast.LENGTH_SHORT).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void giveLocationSendSMS(String sender, Context context){//enviar un mensaje con la ubicacion
        String message;
        Toast.makeText(context, "Dando ubicacion", Toast.LENGTH_SHORT).show();
        //Location location = mapsActivity.getMyLocation();

        if(LATLON != null) {
            //message = SMS_SEND_RESPOND + SMS_LAT + location.getLatitude() + SMS_LON + location.getLongitude();
            message = SMS_SEND_RESPOND + SMS_LAT + LATLON.latitude + SMS_LON + LATLON.longitude;
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            sendSMS(sender,message, context);
        }else{
            Toast.makeText(context, "Tu localizacion por alguna razon es nula", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLocation(String message, Context context){
        String lat, lon;lat = lon = "0";
        int iLa = message.indexOf(SMS_LAT);
        if(iLa == -1)return;
        int iLo = message.indexOf(SMS_LON);
        if(iLo == -1)return;
        //obteniendo latitud
        lat = message.substring(iLa + SMS_LAT.length(), iLo);
        lon = message.substring(iLo + SMS_LON.length(),message.length());
        //Log.e("AUFERrrrrrrRRRRRRRRRRR","iLa: "+lat);
        //Log.e("VICTORIANOOOOOOOOOOOOO","iLo: "+lon);
        Toast.makeText(context, "mostrando ubicacion...\n lat: "+lat+"\nlon"+lon, Toast.LENGTH_SHORT).show();
        LatLng position = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        //Toast.makeText(context, "Lat: "+ position.latitude + "\n lon: "+position.longitude, Toast.LENGTH_SHORT).show();
        if(MAPS_ACTIVITY!=null)
            MAPS_ACTIVITY.addMarker(position);
        else
            Toast.makeText(context, "Activity null ", Toast.LENGTH_SHORT).show();
    }
}
