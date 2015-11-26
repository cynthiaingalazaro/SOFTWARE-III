package mapped.wolfox.com.alocadinganothersmsgps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by MIPC on 17/07/2015.
 * basado en https://www.youtube.com/watch?v=DsBTQ4F6n7s
 */
public class MyLocationServiceListener extends Service implements LocationListener {

    private LocationManager locationManager;
    private final Context ctx;

    Location location = null;
    boolean gpsActivo;

    //Construct
    public MyLocationServiceListener(){
        super();
        this.ctx = this.getApplicationContext();
    }

    public MyLocationServiceListener(Context ctx){
        super();
        this.ctx = ctx;
    }

    //obtener ubicacion
    public Location getLocation(){
        /*
        try{
            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            //gpsActivo = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//para internet
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);//para internet
        }catch(Exception e){
            Toast.makeText(ctx, "Problems con tu GPS", Toast.LENGTH_LONG).show();
        }

        if(gpsActivo){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    1000,//cada tantos segundos
                    10,//cada tantos metros
                    this//lña interfaz, el servicio
                    );
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            //Toast.makeText(ctx, "lat: "+location.getLatitude()+"\n Long: "+location.getLongitude(), Toast.LENGTH_LONG).show();
            Toast.makeText(ctx, "deberias tener coordenadas ", Toast.LENGTH_LONG).show();
        }*/
        return location;
    }
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
