package mapped.wolfox.com.alocadinganothersmsgps;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.PendingIntent;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private CameraUpdate mCamera;

    private Marker marker;//cariable para controlar el marcador

    private SMSReceiver smsReceiver;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        smsReceiver = new SMSReceiver();
        //smsReceiver.setMapActivity(this);
        SMSReceiver.MAPS_ACTIVITY= this;

        //Agrego un marcador
        //addMarker(mMap.getMyLocation());
        setShowLocation(mMap.getMyLocation());
    }
    public Location getMyLocation(){
        return mMap.getMyLocation();
    }
    public LatLng getMyPosition(){

        return latLng;
    }


    public void setShowLocation(Location loc){
        if(loc == null){
            new LatLng(0, 0);
            addMarker(null);
        }else{
            latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            SMSReceiver.LATLON = new LatLng(loc.getLatitude(), loc.getLongitude());
            addMarker(new LatLng(loc.getLatitude(), loc.getLongitude()));
        }
    }
    // Metodo para agregar un marcador
    public void addMarker(LatLng position){

        String title ="Persona espiada";
        String info ="Ubicacion de la persona espiada";
        float color = BitmapDescriptorFactory.HUE_CYAN;
        int zoom = 14;
        if(position == null){//mejor mostrar ultima ubicacion conocida
            Toast.makeText(this," loc: es null", Toast.LENGTH_SHORT).show();
            position =  new LatLng( 0, 0);
            title = "ES UN NINJA";
            info ="Se desconoce Ubicacion";
            color = BitmapDescriptorFactory.HUE_RED;
            zoom = 1;
            //return;
        }
        if(marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                            .position(position)     // Posicion del marcador
                            .title(title)           // Agrega titulo al marcador
                            .snippet(info)          // Agrega información detalle relacionada con el marcador
                            .alpha(0.6f)         // Opacidad del icono
                                    //.anchor(dimension1, dimension2)     // Tamaño del icono (alto y ancho)
                                    //.icon(BitmapDescriptorFactory.fromResource(icon)));
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
        );
        //Camera
        mCamera = CameraUpdateFactory.newLatLngZoom(position, zoom);
        mMap.animateCamera(mCamera);
    }

    // agregar un menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home/up button, so long
        // as you specify a parent activity in AndroidManiferst.xml.
        // define items in menu/option_menu.xml
        switch(item.getItemId()){
            case R.id.MenuOption1:
                //Enviar mensaje al otro celular
                smsReceiver.sendSMS("+51969637038", SMSReceiver.SMS_SEND_ASK, this);

                return true;
            case R.id.MenuOption2:
                Toast.makeText(MapsActivity.this, "Ubicandome..", Toast.LENGTH_LONG);
                //addMarker(mMap.getMyLocation());
                setShowLocation(mMap.getMyLocation());
                return true;
            case R.id.MenuOption3:
                Toast.makeText(MapsActivity.this, "Opcion 3", Toast.LENGTH_LONG).show();
                //finish();// Close app
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Others methods
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);//tipo de mapa a mostrar
        /*
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Establecemos el mapa normal
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // Establecemos el mapa satelite
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // Establecemos el mapa terrestre
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // Establecemos el mapa hibrido
        */

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(40.070823, -2.13760), 14);
        //mMap.animateCamera(mCamera);
        mMap.setMyLocationEnabled(true);
    }


}
